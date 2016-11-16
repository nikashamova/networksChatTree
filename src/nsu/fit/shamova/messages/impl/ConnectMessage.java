package nsu.fit.shamova.messages.impl;

import nsu.fit.shamova.messages.Message;
import nsu.fit.shamova.messages.Type;

import java.net.InetAddress;

public class ConnectMessage extends Message {

    public ConnectMessage(InetAddress addr, int port) {
        super(addr, port, Type.CONNECTED);
    }

    @Override
    public byte[] getByteMessage() {
        return makeHeader();
    }
}
