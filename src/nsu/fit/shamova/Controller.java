package nsu.fit.shamova;

import nsu.fit.shamova.messages.MessageCreator;
import nsu.fit.shamova.messages.impl.AckMessage;
import nsu.fit.shamova.messages.impl.InputMessage;
import nsu.fit.shamova.messages.impl.TextMessage;

import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;


import static nsu.fit.shamova.messages.MessageType.ACK;

public class Controller implements Runnable {
    private Queue<MessageHolder> messageToSend = new ArrayBlockingQueue<>(1000);
    private List<MessageHolder> waitingMessages = new ArrayList();
    private DatagramSocket socket;
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
        //}
        //for (MessageHolder tmp : messageToSend) {
            if (tmp.countOfTry > MAXCOUNTOFSENDINGTRY || System.currentTimeMillis() - tmp.firstSendTime > MAXWAITINGTIME) {
                //messageToSend.remove(tmp);
                iter.remove();
                continue;
            }
            byte[] msg = tmp.message.getByteMessage();
            DatagramPacket packet = new DatagramPacket(msg, msg.length);
            packet.setPort(tmp.message.getPort());
            packet.setAddress(tmp.message.getReceiver());
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

    public InputMessage receive() {
        byte buf[] = new byte[BUFSIZE];
        DatagramPacket packet = new DatagramPacket(buf, BUFSIZE);
        InputMessage inputMessage = null;
        try {
            //receive message from socket
            socket.receive(packet);
            //create input message
            inputMessage = MessageCreator.createMessage(packet.getData());
            if (inputMessage.getType() != ACK) {
                if(receivedMessages.contains(inputMessage.getId())) {
                    receivedMessages.add(inputMessage.getId());
                    handle(inputMessage);
                }
            }
        } catch (IOException e) {
            return null;
        }
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
        return inputMessage;
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

    private void connectHandler(InputMessage message) throws SocketException {
        node.getChildren().add(new Host(socket.getInetAddress(), socket.getPort()));
    }

    private void rootHandler(InputMessage message) {
        node.setRoot(true);
        node.setParent(null);
    }


    private void parentHandler(InputMessage message) {
        node.setParent(new Host(socket.getInetAddress(), socket.getPort()));
    }

    private void textHandler(InputMessage message) {
        System.out.println(message.getTxt());
        for (Host child : node.getChildren()) {
            if (child.equals(new Host(socket.getInetAddress(), socket.getPort()))) {
                continue;
            }
            TextMessage childMessage = new TextMessage(message.getId(), child.getPort(), message.getTxt(), child.getAddress());
            messageToSend.add(new MessageHolder(childMessage, 0, System.currentTimeMillis()));
        }
        if (!node.isRoot() && !node.getParent().equals(new Host(socket.getInetAddress(), socket.getPort()))) {
            TextMessage parentMessage = new TextMessage(message.getId(), node.getParent().getPort(), message.getTxt(), node.getParent().getAddress());
            messageToSend.add(new MessageHolder(parentMessage, 0, System.currentTimeMillis()));
        }
    }


    //добавить отправку сообщения о том, что у нод новый батя
    private void disconnectHandler(InputMessage message) {
        //???????????????????
        InetAddress discAddr = socket.getInetAddress();
        int discPort = socket.getPort();
        Host discHost = new Host(discAddr, discPort);
        Iterator<Host> iterator = node.getChildren().iterator();
        while (iterator.hasNext()) {
            Host child = iterator.next();
        //for (Host child : node.getChildren()) {
            if (child.equals(discHost)) {
                node.getChildren().remove(child);
                break;
            }
        }
    }

    void handle(InputMessage message) throws IOException {
        if (message.getType() != ACK) {
            messageToSend.add(new MessageHolder(new AckMessage(message.getId(), socket.getPort(), socket.getInetAddress()), 0, System.currentTimeMillis()));
        }
        switch (message.getType()) {
            case MSG:
                textHandler(message);
                break;
            case PARENT:
                parentHandler(message);
                break;
            case DISCONNECTED:
                disconnectHandler(message);
                break;
            case ROOT:
                rootHandler(message);
                break;
            case CONNECTED:
                connectHandler(message);
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
            try {
                InputMessage inputMessage = receive();
                if (inputMessage != null) {
                    handle(inputMessage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            shuffle();
        }
    }
}
