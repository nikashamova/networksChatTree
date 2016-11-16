package nsu.fit.shamova.messages.impl;

import nsu.fit.shamova.messages.Message;
import nsu.fit.shamova.messages.Type;

import java.net.InetAddress;

public class DisconnectMessage extends Message {

    public DisconnectMessage(InetAddress addr, int port) {
        super(addr, port, Type.DISCONNECTED);
    }

    @Override
    public byte[] getByteMessage() {
        return makeHeader();
    }
}
