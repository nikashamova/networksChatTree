package nsu.fit.shamova;

import nsu.fit.shamova.messages.IMessage;
import nsu.fit.shamova.messages.MessageCreator;
import nsu.fit.shamova.messages.impl.InputMessage;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Iterator;
import java.util.List;

public class Receiver implements Runnable {
    //private Node node;
    private Sender sender;
    public static int BUFSIZE = 1024;
    public static int MAXWAITINGTIME = 10000;
    //private List<IMessage> waitingMessage = sender.getWaitingMessages();
    private DatagramSocket socket; // = node.getSocket();
    private List<Tuple> waitingMessages;

    public Receiver(DatagramSocket socket, Sender sender, List<Tuple> waitingMessages) {
        this.socket = socket;
        this.sender = sender;
        this.waitingMessages = waitingMessages;
    }

    public void receive() throws IOException {
        byte buf[] = new byte[BUFSIZE];
        DatagramPacket packet = new DatagramPacket(buf, BUFSIZE);
        //receive message from socket
        socket.receive(packet);
        //create input message
        InputMessage inputMessage = MessageCreator.createMessage(packet.getData());

        //searching input message id in list of message, which are waiting for acknowledgement
        Iterator<Tuple> tupleIter = waitingMessages.iterator();
        while (tupleIter.hasNext()) {
            if ((tupleIter.next()).message.getId().equals(inputMessage.getId())) {
                tupleIter.remove();
            } else {
                long delta = (tupleIter.next()).firstSendTime - System.currentTimeMillis();
                if (delta > MAXWAITINGTIME) {
                    tupleIter.remove();
                    break;
                }
            }
        }

    }

    @Override
    public void run() {

    }
}
