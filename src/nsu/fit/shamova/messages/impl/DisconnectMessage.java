package nsu.fit.shamova.messages.impl;

import nsu.fit.shamova.messages.Message;
import nsu.fit.shamova.messages.MessageType;

import java.net.InetAddress;
import java.util.UUID;

public class DisconnectMessage extends Message {

    public DisconnectMessage(InetAddress receiver, int port) {
        super(UUID.randomUUID(), port, MessageType.DISCONNECTED, receiver);
    }
    }
