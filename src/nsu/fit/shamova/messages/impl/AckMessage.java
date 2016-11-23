package nsu.fit.shamova.messages.impl;


import nsu.fit.shamova.messages.Message;
import nsu.fit.shamova.messages.MessageType;

import java.net.InetAddress;
import java.util.UUID;

public class AckMessage extends Message {

    public AckMessage(UUID id, InetAddress receiverAddress, int receiverPort) {
        super(
                id,
                MessageType.ACK,
                receiverAddress,
                receiverPort
        );
        this.id = id;
    }

}
