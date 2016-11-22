package nsu.fit.shamova.messages.impl;

import nsu.fit.shamova.messages.Message;
import nsu.fit.shamova.messages.MessageType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;

public class ParentMessage extends Message {
    private InetAddress parentAddr;
    private int parentPort;

    public ParentMessage(int port, InetAddress receiver, InetAddress parentAddr, int parentPort) {
        super(UUID.randomUUID(), port, MessageType.PARENT, receiver);
        this.parentAddr = parentAddr;
        this.parentPort = parentPort;
    }

    @Override
    public byte[] getByteMessage() {
        byte[] result = null;
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(makeHeader());
            outputStream.write(parentAddr.getAddress());
            outputStream.write(parentPort);
            result = outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
