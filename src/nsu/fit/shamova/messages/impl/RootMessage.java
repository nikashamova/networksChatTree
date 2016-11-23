package nsu.fit.shamova.messages.impl;

import nsu.fit.shamova.messages.Message;
import nsu.fit.shamova.messages.MessageType;

import java.net.InetAddress;
import java.util.UUID;

public class RootMessage extends Message{

    public RootMessage(InetAddress receiverAddress, int receiverPort) {
        super(UUID.randomUUID(), MessageType.ROOT, receiverAddress, receiverPort);
    }
}
