package nsu.fit.shamova;

import nsu.fit.shamova.messages.MessageHolder;
import nsu.fit.shamova.messages.impl.ConnectMessage;
import nsu.fit.shamova.messages.impl.TextMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashSet;
import java.util.Set;

public class Node implements Runnable{

    private final DatagramSocket socket;
    private final Controller controller;
    private int port;
    private Host parent;
    private boolean root;
    private final Set<Host> children = new HashSet<>();
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public Node(int port, InetAddress parentAddr, int parentPort) throws SocketException {
        this.port = port;
        socket = new DatagramSocket(port);
        socket.setSoTimeout(50);
        parent = new Host(parentAddr, parentPort);
        controller = new Controller(this);
        root = false;
        new Thread(controller).start();
        controller.getMessageToSend().add(new MessageHolder(new ConnectMessage(parentAddr, parentPort)));
        //start();
    }

    public Node(int port) throws SocketException {
        this.port = port;
        socket = new DatagramSocket(port);
        socket.setSoTimeout(50);
        parent = null;
        System.out.println("helloooooo");
        root = true;
        controller = new Controller(this);
        (new Thread(controller)).start();
//        start();
    }

    public void run() {
        while (true) {
            //read from terminal
            try {
                String txt = reader.readLine();
                if (txt == null || txt.equals("")) {
                    continue;
                }
                for (Host child : children) {
                    TextMessage message = new TextMessage(child.getAddress(), child.getPort(), txt);
                    controller.getMessageToSend().add(new MessageHolder(message));
                }
                if (!root) {
                    TextMessage message = new TextMessage(parent.getAddress(), parent.getPort(), txt);
                    controller.getMessageToSend().add(new MessageHolder(message));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Controller getController() {
        return controller;
    }

    public DatagramSocket getSocket() {
        return socket;
    }

    public boolean isRoot() {
        return root;
    }

    /*public void setParent(boolean youAreNewRoot) {
        root = youAreNewRoot;
    }*/

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
