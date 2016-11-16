package nsu.fit.shamova.messages;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.UUID;

public abstract class Message implements IMessage {
    private final UUID id = UUID.randomUUID();
    private InetAddress addr;
    private int port;
    private final Type type;

    public Message(InetAddress addr, int port, Type type) {
        this.addr = addr;
        this.port = port;
        this.type = type;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public InetAddress getSender() {
        return addr;
    }

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

    public byte[] makeHeader() {
        byte[] header = null;
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(getType().getT());
            outputStream.write(idToByte());
            outputStream.write(getSender().getAddress());
            outputStream.write(ByteBuffer.allocate(4).putInt(getPort()).array());
            header = outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return header;
    }
}
