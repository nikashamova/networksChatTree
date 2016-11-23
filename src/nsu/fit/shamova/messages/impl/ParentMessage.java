package nsu.fit.shamova.messages.impl;

import nsu.fit.shamova.messages.Message;
import nsu.fit.shamova.messages.MessageType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.UUID;

public class ParentMessage extends Message {
    private final InetAddress parentAddr;
    private final int parentPort;

    public ParentMessage(InetAddress receiverAddress, int receiverPort, InetAddress parentAddress, int parentPort) {
        super(UUID.randomUUID(), MessageType.PARENT, receiverAddress, receiverPort);
        this.parentAddr = parentAddress;
        this.parentPort = parentPort;
    }

    @Override
    public byte[] getByteMessage() {
        byte[] result = null;
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(makeHeader());
            outputStream.write(parentAddr.getAddress());
            outputStream.write(ByteBuffer.allocate(4).putInt(parentPort).array());
            result = outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
