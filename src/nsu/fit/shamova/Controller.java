package nsu.fit.shamova;

import nsu.fit.shamova.messages.MessageCreator;
import nsu.fit.shamova.messages.impl.InputMessage;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Controller {
    private List<MessageHolder> messageToSend = new ArrayList();
    private List<MessageHolder> waitingMessages = new ArrayList();
    private DatagramSocket socket;
    private final static int BUFSIZE = 1024;
    private final static long MAXWAITINGTIME = 10000;
    private final static int MAXCOUNTOFSENDINGTRY = 5;


    Controller(DatagramSocket socket) {
        this.socket = socket;
    }

    void send() throws IOException {
        for (MessageHolder tmp : messageToSend) {
            if (tmp.countOfTry > MAXCOUNTOFSENDINGTRY || System.currentTimeMillis() - tmp.firstSendTime > MAXWAITINGTIME) {
                messageToSend.remove(tmp);
                continue;
            }
            byte[] msg = tmp.message.getByteMessage();
            DatagramPacket packet = new DatagramPacket(msg, msg.length);
            socket.send(packet);
            tmp.countOfTry++;
            switch (tmp.message.getType()) {
                case ACK:
                    break;
                default:
                    waitingMessages.add(tmp);
                    break;
            }
            messageToSend.remove(tmp);
        }
    }

    public void receive() throws IOException {
        byte buf[] = new byte[BUFSIZE];
        DatagramPacket packet = new DatagramPacket(buf, BUFSIZE);
        //receive message from socket
        socket.receive(packet);
        //create input message
        InputMessage inputMessage = MessageCreator.createMessage(packet.getData());

        //searching input message id in list of message, which are waiting for acknowledgement
        Iterator<MessageHolder> iterator = waitingMessages.iterator();
        while (iterator.hasNext()) {
            if ((iterator.next()).message.getId().equals(inputMessage.getId())) {
                iterator.remove();
            } else {
                long delta = (System.currentTimeMillis() - iterator.next().firstSendTime);
                if (delta > MAXWAITINGTIME) {
                    iterator.remove();
                }
            }
        }
    }

    public void shuffle() {
        for (MessageHolder tmp : waitingMessages) {
            messageToSend.add(tmp);
            waitingMessages.remove(tmp);
        }
    }

    public List<MessageHolder> getMessageToSend() {
        return messageToSend;
    }

    public List<MessageHolder> getWaitingMessages() {
        return waitingMessages;
    }

}
