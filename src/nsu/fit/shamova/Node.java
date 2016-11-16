package nsu.fit.shamova;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashSet;
import java.util.Set;

public class Node {

    private DatagramSocket socket;
    private InetAddress addr;
    private int port;
    private Node parent;
    private boolean isParent = true;
    private final Set<Node> children = new HashSet<>();

    public Node(InetAddress addr, int port) {
        this.addr = addr;
        this.port = port;
        new Thread(new Sender(this)).run();
        new Thread(new Receiver(this)).run();
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

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public boolean isParent() {
        return isParent;
    }

    public void setParent(boolean parent) {
        isParent = parent;
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
