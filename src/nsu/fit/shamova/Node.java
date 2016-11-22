package nsu.fit.shamova;

import nsu.fit.shamova.messages.Message;
import nsu.fit.shamova.messages.impl.AckMessage;
import nsu.fit.shamova.messages.impl.InputMessage;
import nsu.fit.shamova.messages.impl.ParentMessage;
import nsu.fit.shamova.messages.impl.TextMessage;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static nsu.fit.shamova.messages.Type.ACK;

public class Node {

    private DatagramSocket socket;
    private int port;
    private Controller controller;
    private Host parent;
    private boolean root = false;
    private final Set<Host> children = new HashSet<>();

    public Node(int port, InetAddress parentAddr, int parentPort) throws SocketException {
        this.port = port;
        socket = new DatagramSocket(port);
        socket.setSoTimeout(50);
        parent.setAddress(parentAddr);
        parent.setPort(parentPort);
        new Thread(controller).start();
        start();
    }

    public Node(int port) throws SocketException {
        this.port = port;
        socket = new DatagramSocket(port);
        socket.setSoTimeout(50);
        System.out.println("helloooooo");
        root = true;
        controller = new Controller(this);
        new Thread(controller).start();
        start();
    }

    void start() {
        while (true) {
            //read from terminal
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

    public boolean isRoot() {
        return root;
    }

    public void setParent(boolean parent) {
        root = parent;
    }

    public Set<Host> getChildren() {
        return children;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public Host getParent() {
        return parent;
    }

    public void setParent(Host parent) {
        this.parent = parent;
    }

    public void setRoot(boolean root) {
        this.root = root;
    }
}
