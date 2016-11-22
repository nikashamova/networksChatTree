package nsu.fit.shamova.messages;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.UUID;

public abstract class Message implements IMessage {
    protected UUID id;
    protected final int port;
    protected final MessageType type;
    protected InetAddress receiver;

    public InetAddress getReceiver() {
        return receiver;
    }

    public void setReceiver(InetAddress receiver) {
        this.receiver = receiver;
    }

    public Message(UUID id, int port, MessageType type, InetAddress receiver) {
        this.id = id;
        this.port = port;
        this.type = type;
        this.receiver = receiver;

    }

    @Override
    public MessageType getType() {
        return type;
    }

 /*   @Override
    public InetAddress getSender() {
        return addr;
    }*/

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public UUID getId() {
        return id;
    }

    protected byte[] idToByte() {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(getId().getMostSignificantBits());
        bb.putLong(getId().getLeastSignificantBits());
        return bb.array();
    }

    public byte[] getByteMessage() {
        return makeHeader();
    }

    public byte[] makeHeader() {
        byte[] header = null;
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(getType().getT());
            outputStream.write(idToByte());
            //outputStream.write(getSender().getAddress());
            outputStream.write(ByteBuffer.allocate(4).putInt(getPort()).array());
            header = outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return header;
    }
}
