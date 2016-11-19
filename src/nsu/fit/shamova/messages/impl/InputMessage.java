package nsu.fit.shamova.messages.impl;

import nsu.fit.shamova.messages.IMessage;
import nsu.fit.shamova.messages.Message;
import nsu.fit.shamova.messages.Type;

import java.net.InetAddress;
import java.util.UUID;

/**
 * Created by Ника on 13.11.2016.
 */
public class InputMessage implements IMessage {
    private Type type;
    private InetAddress addr;
    private int port;
    private UUID id;

    public InputMessage(UUID id, Type type, InetAddress addr, int port) {
        this.type = type;
        this.addr = addr;
        this.port = port;
        this.id = id;
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

    @Override
    public byte[] getByteMessage() {
        return null;
    }
}
