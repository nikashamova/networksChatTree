package nsu.fit.shamova;

import nsu.fit.shamova.messages.Message;
import nsu.fit.shamova.messages.MessageCreator;
import nsu.fit.shamova.messages.impl.AckMessage;
import nsu.fit.shamova.messages.impl.InputMessage;
import nsu.fit.shamova.messages.impl.ParentMessage;
import nsu.fit.shamova.messages.impl.TextMessage;
import sun.plugin2.jvm.CircularByteBuffer;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static nsu.fit.shamova.messages.Type.ACK;

public class Controller implements Runnable{
    private List<MessageHolder> messageToSend = new ArrayList();
    private List<MessageHolder> waitingMessages = new ArrayList();
    private DatagramSocket socket;
    private final static int BUFSIZE = 1024;
    private final static long MAXWAITINGTIME = 10000;
    private final static int MAXCOUNTOFSENDINGTRY = 10;
    private final static int SEND = 5;
    Node node;


    Controller(Node node) {
        this.node = node;
        this.socket = node.getSocket();
    }

    void send() throws IOException {
        for (MessageHolder tmp : messageToSend) {
            if (tmp.countOfTry > MAXCOUNTOFSENDINGTRY || System.currentTimeMillis() - tmp.firstSendTime > MAXWAITINGTIME) {
                messageToSend.remove(tmp);
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
            handle(inputMessage);
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

    public void shuffle() {
        for (MessageHolder tmp : waitingMessages) {
            messageToSend.add(tmp);
            waitingMessages.remove(tmp);
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
            TextMessage childMessage = new TextMessage(message.getId(), child.getPort(), message.getTxt(), child.getAddress());
            messageToSend.add(new MessageHolder(childMessage, 0, System.currentTimeMillis()));
        }
    }


    //добавить отправку сообщения о том, что у нод новый батя
    private void disconnectHandler(InputMessage message) {
        //???????????????????
        InetAddress discAddr = socket.getInetAddress();
        int discPort = socket.getPort();
        Host discHost = new Host(discAddr, discPort);
        for (Host child : node.getChildren()) {
            if (child.equals(discHost)) {
                node.getChildren().remove(child);
                break;
            }
        }
    }

    void handle(InputMessage message) throws IOException {
        messageToSend.add(new MessageHolder(new AckMessage(message.getId(), socket.getPort(), socket.getInetAddress()), 0, System.currentTimeMillis()));
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
        if (message.getType() != ACK) {
        }
    }

    public List<MessageHolder> getMessageToSend() {
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
