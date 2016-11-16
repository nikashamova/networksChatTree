package nsu.fit.shamova;

import nsu.fit.shamova.messages.IMessage;
import nsu.fit.shamova.messages.MessageCreator;
import nsu.fit.shamova.messages.impl.InputMessage;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Receiver implements Runnable {
    //private Node node;
    private Sender sender;
    //private List<IMessage> waitingMessage = sender.getWaitingMessages();
    private DatagramSocket socket; // = node.getSocket();
    private List<Tuple> newWaitingMessages;

    public Receiver(DatagramSocket socket, Sender sender, List<Tuple> newWaitingMessages) {
        this.socket = socket;
        this.sender = sender;
        this.newWaitingMessages = newWaitingMessages;
    }

    public void receive() throws IOException {
        byte buf[] = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buf, 1024);
        socket.receive(packet);
        InputMessage m = MessageCreator.createMessage(packet.getData());
        for (int i = 0; i < 5; i++) {
            Iterator newIter = newWaitingMessages.iterator();
            while (newIter.hasNext()) {
                if (((IMessage) newIter.next()).getId() == m.getId()) {
                    //waitingMessage.remove(iter.next());
                } else {
                    long delta = ((Tuple) newIter.next()).z - System.currentTimeMillis();
                    if (delta > 10000) {
                        //remove
                        break;
                    }
                    sender.getMessageToSend().add((IMessage) newIter.next());
                }
                newIter = (Iterator) newIter.next();
            }
        }
    }

    @Override
    public void run() {

    }
}
