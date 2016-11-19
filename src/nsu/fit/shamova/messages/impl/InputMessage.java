package nsu.fit.shamova.messages.impl;

import nsu.fit.shamova.messages.IMessage;
import nsu.fit.shamova.messages.Type;

import java.net.InetAddress;
import java.util.UUID;

/**
 * Created by Ника on 13.11.2016.
 */
public class InputMessage implements IMessage {
    private Type type;
    private InetAddress sender;
    private int port;
    private UUID id;
    private String txt;

   // private InetAddress parent;

    public InputMessage(UUID id, Type type, InetAddress sender, int port, /*InetAddress parent,*/ String msg) {
        this.type = type;
        this.sender = sender;
        this.port = port;
        this.id = id;
        //this.parent = parent;
        this.txt = msg;
    }

    @Override
    public Type getType() {
        return type;
    }

    /*public InetAddress getParent() {
        return parent;
    }
*/
    public String getTxt() {
        return txt;
    }

    @Override
    public InetAddress getSender() {
        return sender;
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
