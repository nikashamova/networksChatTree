package nsu.fit.shamova;
import nsu.fit.shamova.messages.impl.AckMessage;
import nsu.fit.shamova.messages.impl.InputMessage;
import nsu.fit.shamova.messages.impl.TextMessage;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashSet;
import java.util.Set;

import static nsu.fit.shamova.messages.Type.ACK;

public class Node {

    private DatagramSocket socket;
    private InetAddress addr;
    private int port;
    private Node parent;
    private boolean root = false;
    private final Set<Node> children = new HashSet<>();
    Controller controller = new Controller(socket);

    public Node(InetAddress addr, int port, Node parent) {
        this.addr = addr;
        this.port = port;
        this.parent = parent;
    }

    void handle(InputMessage message) throws IOException {
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
            controller.getMessageToSend().add(new MessageHolder(new AckMessage(message.getId(), port, addr), 0, System.currentTimeMillis()));
        }
    }

    private void connectHandler(InputMessage message) {
        InetAddress childAddr = message.getSender();
        int childPort = message.getPort();
        children.add(new Node(childAddr, childPort, this));

    }

    private void disconnectHandler(InputMessage message) {
        InetAddress discAddr = message.getSender();
        for (Node child : children) {
            if (child.getAddr().equals(discAddr)) {
                children.remove(child);
                break;
            }
        }
    }

    private void parentHandler(InputMessage message) {
        //WHAAAAAAAAAAAAAAAT
        InetAddress parent = message.getSender();
        int port = message.getPort();
        this.parent.setAddr(parent);
        this.parent.setPort(message.getPort());
        //this.parent.se
    }

    private void rootHandler(InputMessage message) throws IOException {
        this.root = true;
        controller.getMessageToSend().add(new MessageHolder(new AckMessage(message.getId(), port, addr), 0, System.currentTimeMillis()));
    }

    private void textHandler(InputMessage message) {
        System.out.println(addr + " " + message.getTxt());
        for (Node child : children) {
            TextMessage childMessage = new TextMessage(message.getId(), message.getSender(), message.getPort(), message.getTxt());
            child.getController().getMessageToSend().add(new MessageHolder(childMessage, 0, System.currentTimeMillis()));
        }
    }

    public Controller getController() {
        return controller;
    }

    public DatagramSocket getSocket() {
        return socket;
    }

    public void setSocket(DatagramSocket socket) {
        this.socket = socket;
    }

    public Node getParent() {
        return parent;
    }

    public void setRoot(Node root) {
        this.parent = root;
    }

    public boolean isRoot() {
        return root;
    }

    public void setParent(boolean parent) {
        root = parent;
    }

    public Set<Node> getChildren() {
        return children;
    }

    public InetAddress getAddr() {
        return addr;
    }

    public void setAddr(InetAddress addr) {
        this.addr = addr;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
