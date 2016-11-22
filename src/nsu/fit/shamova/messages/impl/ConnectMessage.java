package nsu.fit.shamova.messages.impl;

import nsu.fit.shamova.messages.Message;
import nsu.fit.shamova.messages.Type;

import java.net.InetAddress;
import java.util.UUID;

public class ConnectMessage extends Message {

    public ConnectMessage(InetAddress addr, int port, InetAddress receiver) {
        super(UUID.randomUUID(), port, Type.CONNECTED, receiver);
    }
}
