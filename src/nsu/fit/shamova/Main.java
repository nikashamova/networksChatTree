package nsu.fit.shamova;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

public class Main {

    public static void main(String[] args) throws SocketException, UnknownHostException {
        Node node;
        ByteBuffer a = ByteBuffer.allocate(Long.BYTES);
        a.putLong((long) 7);
        if (args.length == 2){
            node = new Node(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        }
        else {
            node = new Node(
                    Integer.parseInt(args[0]),
                    InetAddress.getByName(args[1]),
                    Integer.parseInt(args[2]),
                    Integer.parseInt(args[3]));
        }
        Runtime.getRuntime().addShutdownHook(new Thread(new DisconnectHook(node)));
        node.run();
    }
}
