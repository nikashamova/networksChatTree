package nsu.fit.shamova.messages.impl;

import nsu.fit.shamova.messages.Message;
import nsu.fit.shamova.messages.MessageType;

import java.net.InetAddress;
import java.util.UUID;

public class RootMessage extends Message{

    public RootMessage(int port, InetAddress receiver) {
        super(UUID.randomUUID(), port, MessageType.ROOT, receiver);
    }
}
