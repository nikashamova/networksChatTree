package nsu.fit.shamova.messages.impl;

import nsu.fit.shamova.messages.Message;
import nsu.fit.shamova.messages.Type;

import java.net.InetAddress;
import java.util.UUID;

public class ConnectMessage extends Message {

    public ConnectMessage(InetAddress addr, int port) {
        super(UUID.randomUUID(), addr, port, Type.CONNECTED);
    }
}
