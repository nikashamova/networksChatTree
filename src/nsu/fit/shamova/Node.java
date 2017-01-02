package nsu.fit.shamova;

import nsu.fit.shamova.messages.impl.ConnectMessage;
import nsu.fit.shamova.messages.impl.DisconnectMessage;
import nsu.fit.shamova.messages.impl.TextMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

class Node implements Runnable {

    private DatagramSocket socket;
    private Controller controller;
    private int port;
    private Host parent;
    private boolean root;
    private final Set<Host> children = new HashSet<>();
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private int lossLimit;
    private NodeState state;

    public Node(int port, InetAddress parentAddr, int parentPort, int lossLimit) throws SocketException {
        /*this.port = port;
        socket = new DatagramSocket(port);
        this.lossLimit = lossLimit;
        socket.setSoTimeout(1000);
        controller = new Controller(this);
        new Thread(controller).start();*/
        initialize(port, lossLimit);
        parent = new Host(parentAddr, parentPort);
        root = false;
        state = NodeState.CONNECTING;
        ConnectMessage connection = new ConnectMessage(parentAddr, parentPort);
        controller.getMessageToSend().add(connection);
        UUID connectionId = connection.getId();
        controller.setConnectionId(connectionId);
    }

    Thread controllerThread;


    public Node(int port, int lossLimit) throws SocketException {
        /*this.port = port;
        socket = new DatagramSocket(port);
        this.lossLimit = lossLimit;
        controller = new Controller(this);*/
        System.out.println("helloooooo");
        initialize(port, lossLimit);
        parent = null;
        root = true;
        state = NodeState.WORKING;
    }

    private void initialize(int port, int lossLimit) throws SocketException {
        this.port = port;
        this.lossLimit = lossLimit;
        socket = new DatagramSocket(port);
        socket.setSoTimeout(1000);
        controller = new Controller(this);
        controllerThread = new Thread(controller);
        controllerThread.start();
    }

    public void run() {
        long time = System.currentTimeMillis();
        while (System.currentTimeMillis() - time < 10000) {

        }
        if (state == NodeState.CONNECTING) {
            System.out.println("-----------cannot connect");
            controller.clearAll();
            controller.getMessageToSend().add(new DisconnectMessage(parent.getAddress(), parent.getPort()));
            parent = null;
            root = true;
            System.out.println("cannot connect");
        }
        System.out.println("start reading");
        while (true) {
            //read from terminal
            try {
                String txt = reader.readLine();
                if (txt == null || txt.equals("")) {
                    continue;
                }
                for (Host child : children) {
                    TextMessage message = new TextMessage(child.getAddress(), child.getPort(), txt);
                    controller.getMessageToSend().add(message);
                }
                if (!root) {
                    TextMessage message = new TextMessage(parent.getAddress(), parent.getPort(), txt);
                    controller.getMessageToSend().add(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Controller getController() {
        return controller;
    }

    public BufferedReader getReader() {
        return reader;
    }

    public NodeState getState() {
        return state;
    }

    public void setState(NodeState state) {
        this.state = state;
    }

    public int getLossLimit() {
        return lossLimit;
    }

    public DatagramSocket getSocket() {
        return socket;
    }

    public boolean isRoot() {
        return root;
    }

    public void setParent(Host parent) {
        this.parent = parent;
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

    public Host getParent() {
        return parent;
    }

    public void setRoot(boolean root) {
        this.root = root;
    }
}
