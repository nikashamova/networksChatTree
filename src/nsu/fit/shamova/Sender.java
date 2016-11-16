package nsu.fit.shamova;

import nsu.fit.shamova.messages.IMessage;
import nsu.fit.shamova.messages.Message;
import nsu.fit.shamova.messages.Type;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Sender implements Runnable {
    private List<Tuple> messageToSend = new ArrayList();
   // private List<IMessage> waitingMessages = new ArrayList();
    private List<Tuple> newWaitingMessages = new ArrayList();
    private Node node;
    private DatagramSocket socket = node.getSocket();

    Sender(Node node) {
        this.node = node;
    }

    void send() throws IOException {
        Iterator iter = messageToSend.iterator();
        while (iter.hasNext()) {
            Tuple tmp = (Tuple) iter.next();
            byte[] msg = tmp.x.getByteMessage();
            tmp.y++;
            DatagramPacket packet = new DatagramPacket(msg, msg.length);
            socket.send(packet);
           // waitingMessages.add(tmp.x);
            newWaitingMessages.add(tmp);
            if (tmp.x.getType() == Type.MSG && tmp.y < 5)
                newWaitingMessages.add(tmp);
            messageToSend.remove(tmp);
            iter = (Iterator) iter.next();
        }
    }

    public List<Tuple> getMessageToSend() {
        return messageToSend;
    }

    public List<Tuple> getNewWaitingMessages() {
        return newWaitingMessages;
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
