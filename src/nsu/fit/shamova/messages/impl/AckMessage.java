package nsu.fit.shamova.messages.impl;

import nsu.fit.shamova.messages.Message;
import nsu.fit.shamova.messages.Type;

import java.net.InetAddress;
import java.util.UUID;

public class AckMessage extends Message {

    public AckMessage(int port, InetAddress addr, UUID id) {
        super(id, addr, port, Type.ACK);
        this.id = id;
    }


    @Override
    public byte[] getByteMessage() {
        return makeHeader();
    }
}
