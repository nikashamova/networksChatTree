package nsu.fit.shamova.messages.impl;


import nsu.fit.shamova.messages.Message;
import nsu.fit.shamova.messages.MessageType;

import java.net.InetAddress;
import java.util.UUID;

public class AckMessage extends Message {

    public AckMessage(UUID id, int port, InetAddress receiver) {
        super(id, port, MessageType.ACK, receiver);
        this.id = id;
    }

/*
    @Override
    public byte[] getByteMessage() {
        return makeHeader();
    }*/
}
