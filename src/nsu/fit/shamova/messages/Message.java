package nsu.fit.shamova.messages;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.UUID;

public abstract class Message implements OutputMessage {
    protected UUID id;
    protected final MessageType type;
    protected final InetAddress receiverAddress;
    protected final int receiverPort;
    private int countOfTry;
    protected final long firstSendTime;


    public int getCountOfTry() {
        return countOfTry;
    }

    public long getFirstSendTime() {
        return firstSendTime;
    }

    public void incrementCountOfTry() {
        countOfTry++;
    }

    public InetAddress getReceiverAddress() {
        return receiverAddress;
    }


    public Message(UUID id, MessageType type, InetAddress receiverAddress, int receiverPort) {
        this.type = type;
        this.id = id;
        this.receiverAddress = receiverAddress;
        this.receiverPort = receiverPort;
        this.countOfTry = 0;
        this.firstSendTime = System.currentTimeMillis();
    }

    @Override
    public MessageType getType() {
        return type;
    }

 /*   @Override
    public InetAddress getSender() {
        return addr;
    }*/

    public int getReceiverPort() {
        return receiverPort;
    }

    @Override
    public UUID getId() {
        return id;
    }

    private byte[] idToByte() {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(getId().getMostSignificantBits());
        bb.putLong(getId().getLeastSignificantBits());
        return bb.array();
    }

    public byte[] getByteMessage() {
        return makeHeader();
    }

    protected byte[] makeHeader() {
        byte[] header = null;
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(type.getT());
            outputStream.write(idToByte());
            outputStream.write(receiverAddress.getAddress());
            outputStream.write(ByteBuffer.allocate(4).putInt(getReceiverPort()).array());
            header = outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return header;
    }
}
