package nsu.fit.shamova.messages.impl;

import nsu.fit.shamova.messages.Message;
import nsu.fit.shamova.messages.Type;

import java.net.InetAddress;

public class RootMessage extends Message{

    public RootMessage(InetAddress addr, int port) {
        super(addr, port, Type.ROOT);
    }

    @Override
    public byte[] getByteMessage() {
        return makeHeader();
    }
}
