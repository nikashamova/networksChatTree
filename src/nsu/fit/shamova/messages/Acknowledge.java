package nsu.fit.shamova.messages;

import java.net.InetAddress;

public class Acknowledge extends CommonMessage {

    public Acknowledge(int port, InetAddress addr) {
        super(addr, port, "ACK");
    }
}
