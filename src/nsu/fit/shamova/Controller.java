package nsu.fit.shamova;

import nsu.fit.shamova.collection.CircularFifoQueue;
import nsu.fit.shamova.messages.Message;
import nsu.fit.shamova.messages.MessageCreator;
import nsu.fit.shamova.messages.impl.AckMessage;
import nsu.fit.shamova.messages.impl.ConnectMessage;
import nsu.fit.shamova.messages.impl.InputMessage;
import nsu.fit.shamova.messages.impl.TextMessage;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Iterator;
import java.util.Queue;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;

import static nsu.fit.shamova.messages.MessageType.ACK;
import static nsu.fit.shamova.messages.MessageType.CONNECTED;

public class Controller implements Runnable {
    private final Queue<Message> messageToSend = new ArrayBlockingQueue<>(1000);
    private final Queue<Message> waitingMessages = new ArrayBlockingQueue<>(1000);
    private final CircularFifoQueue<UUID> receivedMessages = new CircularFifoQueue<>(QUEUESIZE);

    private final DatagramSocket socket;
    public final static int BUFSIZE = 512;
    public final static long MAXWAITINGTIME = 10000;
    public final static int MAXCOUNTOFSENDINGTRY = 50;
    public final static int QUEUESIZE = 30;
    private final static int SEND = 5;
    private final Node node;
    private UUID connectionId;
    private final Random random = new Random();

    public void setConnectionId(UUID connectionId) {
        this.connectionId = connectionId;
    }


    Controller(Node node) {
        this.node = node;
        this.socket = node.getSocket();
        receivedMessages.parallelStream();
        waitingMessages.parallelStream();
    }

    private void send() throws IOException {
        Iterator<Message> iter = messageToSend.iterator();
        while (iter.hasNext()) {
            Message tmp = iter.next();
            if (
                    (node.getState() == NodeState.WORKING) ||
                            (node.getState() == NodeState.CONNECTING && tmp.getType() == CONNECTED) ||
                            (node.getState() == NodeState.DISCONNECTING)
                    )
                if (tmp.getCountOfTry() > MAXCOUNTOFSENDINGTRY && System.currentTimeMillis() - tmp.getFirstSendTime() > MAXWAITINGTIME) {
                    iter.remove();
                    continue;
                }
            byte[] msg = tmp.getByteMessage();
            DatagramPacket packet = new DatagramPacket(msg, msg.length);
            packet.setPort(tmp.getReceiverPort());
            packet.setAddress(tmp.getReceiverAddress());
            socket.send(packet);
            tmp.incrementCountOfTry();
            switch (tmp.getType()) {
                case ACK:
                    break;
                default:
                    waitingMessages.add(tmp);
                    break;
            }
            //messageToSend.remove(tmp);
            iter.remove();
        }
    }

    private void receive() {
        byte buf[] = new byte[BUFSIZE];
        DatagramPacket packet = new DatagramPacket(buf, BUFSIZE);
        InputMessage inputMessage;
        try {
            //receive message from socket
            socket.receive(packet);

            //create input message
            inputMessage = MessageCreator.createMessage(packet.getData());

            //loss
            int randomResult = random.nextInt(100);

            if ((randomResult < node.getLossLimit()) || (node.getState() == NodeState.CONNECTING && inputMessage.getType() != ACK)) {
                return;
            }

            if (inputMessage.getId().equals(connectionId) && node.getState() == NodeState.CONNECTING) {
                System.out.println("Now I'm working");
                node.setState(NodeState.WORKING);
            }

            if ((inputMessage.getType() != ACK && node.getState() != NodeState.DISCONNECTING)) {
                if (!receivedMessages.contains(inputMessage.getId())) {
                    handle(inputMessage, packet);
                    receivedMessages.add(inputMessage.getId());
                }
            }
            inputMessage.print();

        } catch (IOException e) {
            return;
        }

        if (inputMessage.getType() == ACK) {
            //searching input message id in list of message, which are waiting for acknowledgement
            Iterator<Message> iterator = waitingMessages.iterator();

            while (iterator.hasNext()) {
                Message tmp = iterator.next();
                if (tmp.getId().equals(inputMessage.getId())) {
                    iterator.remove();
                }/* else {
                long delta = (System.currentTimeMillis() - tmp.firstSendTime);
                if (delta > MAXWAITINGTIME) {
                    iterator.remove();
                }
            }*/
            }
        }
        // return inputMessage;
    }

    private void shuffle() {
        Iterator<Message> iterator = waitingMessages.iterator();
        while (iterator.hasNext()) {
            Message tmp = iterator.next();
            //}
            messageToSend.add(tmp);
            iterator.remove();
            //waitingMessages.remove(tmp);
        }
    }

    private void connectHandler(InputMessage message, DatagramPacket packet) {
        //System.out.println("new connection");
        node.getChildren().add(new Host(packet.getAddress(), packet.getPort()));
    }

    private void rootHandler(InputMessage message) {
        node.setRoot(true);
        node.setParent(null);
    }


    private void parentHandler(InputMessage message) {
        InetAddress parentAddress = message.getParentAdders();
        int parentPort = message.getParentPort();
        node.setParent(new Host(parentAddress, parentPort));
        messageToSend.add(new ConnectMessage(parentAddress, parentPort));
    }

    private void textHandler(InputMessage message, DatagramPacket packet) {
        //System.out.println(message.getTxt());
        for (Host child : node.getChildren()) {
            if (child.equals(new Host(packet.getAddress(), packet.getPort()))) {
                continue;
            }
            TextMessage childMessage = new TextMessage(message.getId(), child.getAddress(), child.getPort(), message.getTxt());
            messageToSend.add(childMessage);
        }
        if (!node.isRoot() && !node.getParent().equals(new Host(packet.getAddress(), packet.getPort()))) {
            TextMessage parentMessage = new TextMessage(message.getId(), node.getParent().getAddress(), node.getParent().getPort(), message.getTxt());
            messageToSend.add(parentMessage);
        }
    }


    private void disconnectHandler(InputMessage message, DatagramPacket packet) {
        InetAddress discAddr = packet.getAddress();
        int discPort = packet.getPort();
        Host discHost = new Host(discAddr, discPort);
        Iterator<Host> iterator = node.getChildren().iterator();
        while (iterator.hasNext()) {
            Host child = iterator.next();
            if (child.equals(discHost)) {
                node.getChildren().remove(child);
                break;
            }
        }
    }

    private void handle(InputMessage message, DatagramPacket packet) throws IOException {
        //message.print();
        if (message.getType() != ACK) {
            messageToSend.add(new AckMessage(message.getId(), packet.getAddress(), packet.getPort()));
        }
        switch (message.getType()) {
            case TXT:
                textHandler(message, packet);
                break;
            case PARENT:
                parentHandler(message);
                break;
            case DISCONNECTED:
                disconnectHandler(message, packet);
                break;
            case ROOT:
                rootHandler(message);
                break;
            case CONNECTED:
                connectHandler(message, packet);
                break;
        }
    }

    public Queue<Message> getMessageToSend() {
        return messageToSend;
    }


    @Override
    public void run() {
        while (true) {
            for (int i = 0; i < SEND; i++) {
                try {
                    send();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            for (int i = 0; i < SEND; i++) {
                receive();
            }
            shuffle();
        }
    }

    public void clearAll() {
        messageToSend.clear();
        waitingMessages.clear();
    }

    public boolean isClear() {
        return (messageToSend.size() == 0 && waitingMessages.size() == 0);
    }
}
