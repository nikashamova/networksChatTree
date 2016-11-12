package nsu.fit.shamova.messages;

import java.net.InetAddress;
import java.util.UUID;

abstract class CommonMessage implements Message {
    private final UUID id = UUID.randomUUID();
    private InetAddress addr;
    private int port;
    private final String type;

    CommonMessage(InetAddress addr, int port, String type){
        this.addr = addr;
        this.port = port;
        this.type = type;
    }

    @Override
    public String geyType() {
        return type;
    }

    @Override
    public InetAddress getSender() {
        return addr;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public UUID getId() {
        return id;
    }
}
