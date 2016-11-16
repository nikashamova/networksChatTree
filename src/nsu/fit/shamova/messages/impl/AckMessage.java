package nsu.fit.shamova.messages.impl;

import nsu.fit.shamova.messages.Message;
import nsu.fit.shamova.messages.Type;

import java.net.InetAddress;

public class AckMessage extends Message {

    public AckMessage(int port, InetAddress addr) {
        super(addr, port, Type.ACK);
    }


    @Override
    public byte[] getByteMessage() {
        return makeHeader();
    }
}
