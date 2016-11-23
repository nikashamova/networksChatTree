package nsu.fit.shamova;

import nsu.fit.shamova.messages.Message;
import nsu.fit.shamova.messages.MessageCreator;
import nsu.fit.shamova.messages.MessageHolder;
import nsu.fit.shamova.messages.impl.AckMessage;
import nsu.fit.shamova.messages.impl.ConnectMessage;
import nsu.fit.shamova.messages.impl.InputMessage;
import nsu.fit.shamova.messages.impl.TextMessage;

import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;


import static nsu.fit.shamova.messages.MessageType.ACK;

public class Controller implements Runnable {
    private final Queue<MessageHolder> messageToSend = new ArrayBlockingQueue<>(1000);
    private final List<MessageHolder> waitingMessages = new ArrayList();
    private final DatagramSocket socket;
    public final static int BUFSIZE = 512;
    public final static long MAXWAITINGTIME = 10000;
    public final static int MAXCOUNTOFSENDINGTRY = 10;
    public final static int QUEUESIZE = 30;
    private final static int SEND = 5;
    private final Node node;
    private final CircularFifoQueue<UUID> receivedMessages = new CircularFifoQueue<>(QUEUESIZE);


    Controller(Node node) {
        this.node = node;
        receivedMessages.parallelStream();
        this.socket = node.getSocket();
    }

    void send() throws IOException {
        Iterator<MessageHolder> iter = messageToSend.iterator();
        while (iter.hasNext()) {
            MessageHolder tmp = iter.next();
            if (tmp.countOfTry > MAXCOUNTOFSENDINGTRY || System.currentTimeMillis() - tmp.firstSendTime > MAXWAITINGTIME) {
                iter.remove();
                continue;
            }
            byte[] msg = tmp.message.getByteMessage();
            DatagramPacket packet = new DatagramPacket(msg, msg.length);
            packet.setPort(tmp.message.getReceiverPort());
            packet.setAddress(tmp.message.getReceiverAddress());
            socket.send(packet);
            tmp.countOfTry++;
            switch (tmp.message.getType()) {
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

    public void receive() {
        byte buf[] = new byte[BUFSIZE];
        DatagramPacket packet = new DatagramPacket(buf, BUFSIZE);
        InputMessage inputMessage = null;
        try {
            //receive message from socket
            socket.receive(packet);
            //create input message
            //System.out.println("packet: " + packet);
            inputMessage = MessageCreator.createMessage(packet.getData());
            if (inputMessage.getType() != ACK) {
                if(!receivedMessages.contains(inputMessage.getId())) {
                    handle(inputMessage, packet);
                    receivedMessages.add(inputMessage.getId());
                }
            }
            else {
                inputMessage.print();
            }
        } catch (IOException e) {
            return;
        }
        //searching input message id in list of message, which are waiting for acknowledgement
        Iterator<MessageHolder> iterator = waitingMessages.iterator();
        while (iterator.hasNext()) {
            MessageHolder tmp = iterator.next();
            if (tmp.message.getId().equals(inputMessage.getId())) {
                iterator.remove();
            } else {
                long delta = (System.currentTimeMillis() - tmp.firstSendTime);
                if (delta > MAXWAITINGTIME) {
                    iterator.remove();
                }
            }
        }
       // return inputMessage;
    }

    private void shuffle() {
        Iterator<MessageHolder> iterator = waitingMessages.iterator();
        while (iterator.hasNext()) {
            MessageHolder tmp = iterator.next();
        //}
        //for (MessageHolder tmp : waitingMessages) {
            messageToSend.add(tmp);
            iterator.remove();
            //waitingMessages.remove(tmp);
        }
    }

    private void connectHandler(InputMessage message, DatagramPacket packet) throws SocketException {
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
        messageToSend.add(new MessageHolder(new ConnectMessage(parentAddress, parentPort)));
    }

    private void textHandler(InputMessage message, DatagramPacket packet) {
        //System.out.println(message.getTxt());
        for (Host child : node.getChildren()) {
            if (child.equals(new Host(packet.getAddress(), packet.getPort()))) {
                continue;
            }
            TextMessage childMessage = new TextMessage(message.getId(), child.getAddress(), child.getPort(), message.getTxt());
            messageToSend.add(new MessageHolder(childMessage));
        }
        if (!node.isRoot() && !node.getParent().equals(new Host(packet.getAddress(), packet.getPort()))) {
            TextMessage parentMessage = new TextMessage(message.getId(), node.getParent().getAddress(), node.getParent().getPort(), message.getTxt());
            messageToSend.add(new MessageHolder(parentMessage));
        }
    }


    //добавить отправку сообщения о том, что у нод новый батя
    private void disconnectHandler(InputMessage message, DatagramPacket packet) {
        //???????????????????
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

    void handle(InputMessage message, DatagramPacket packet) throws IOException {
        message.print();
        if (message.getType() != ACK) {
            messageToSend.add(new MessageHolder(new AckMessage(message.getId(), packet.getAddress(), packet.getPort())));
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

    public Queue<MessageHolder> getMessageToSend() {
        return messageToSend;
    }

    public List<MessageHolder> getWaitingMessages() {
        return waitingMessages;
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
            //try {
                /*InputMessage inputMessage = */receive();
                /*if (inputMessage != null) {
                    handle(inputMessage, packet);
                }*/
            /*} catch (IOException e) {
                e.printStackTrace();
            }*/
            shuffle();
        }
    }
}
