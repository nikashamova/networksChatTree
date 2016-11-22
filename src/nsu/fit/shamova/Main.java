package nsu.fit.shamova;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Main {

    public static void main(String[] args) throws SocketException, UnknownHostException {
        Node node;
        if (args.length == 1){
            node = new Node(Integer.parseInt(args[0]));
        }
        else {
            node = new Node(
                    Integer.parseInt(args[0]),
                    InetAddress.getByName(args[1]),
                    Integer.parseInt(args[2])
            );
        }
        Runtime.getRuntime().addShutdownHook(new Thread(new DisconnectHook(node)));
        node.run();
    }
}
