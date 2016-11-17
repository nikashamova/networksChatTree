package nsu.fit.shamova;

import nsu.fit.shamova.messages.Type;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Sender implements Runnable {
    private List<Tuple> messageToSend = new ArrayList();
   // private List<IMessage> waitingMessages = new ArrayList();
    private List<Tuple> waitingMessages = new ArrayList();
    private Node node;
    private DatagramSocket socket = node.getSocket();

    Sender(Node node) {
        this.node = node;
    }

    void send() throws IOException {
        for (Tuple tmp : messageToSend) {
            byte[] msg = tmp.message.getByteMessage();
            tmp.countOfTry++;
            DatagramPacket packet = new DatagramPacket(msg, msg.length);
            socket.send(packet);
            // waitingMessages.add(tmp.message);
            waitingMessages.add(tmp);
            if (tmp.message.getType() == Type.MSG && tmp.countOfTry < 5)
                waitingMessages.add(tmp);
            messageToSend.remove(tmp);
        }

    }

    public List<Tuple> getMessageToSend() {
        return messageToSend;
    }

    public List<Tuple> getWaitingMessages() {
        return waitingMessages;
    }

    /*public void setWaitingMessages(List<IMessage> waitingMessages) {
        this.waitingMessages = waitingMessages;
    }

    public List<IMessage> getWaitingMessages() {
        return waitingMessages;
    }
*/
    @Override
    public void run() {

    }
}
