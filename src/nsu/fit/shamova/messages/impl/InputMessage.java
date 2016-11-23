package nsu.fit.shamova.messages.impl;

import nsu.fit.shamova.messages.OutputMessage;
import nsu.fit.shamova.messages.MessageType;

import java.net.InetAddress;
import java.util.UUID;

public class InputMessage implements OutputMessage {
    private MessageType type;
    private InetAddress receiverAddress;
    private int receiverPort;
    private UUID id;
    private InetAddress parentAdders;
    private int parentPort;
    private String txt;

   // private InetAddress parentAdders;

    public int getParentPort() {
        return parentPort;
    }

    public InputMessage(
            UUID id,
            MessageType type,
            InetAddress receiverAddress,
            int receiverPort,
            InetAddress parentAdders,
            int parentPort,
            String msg) {
        this.type = type;
        this.receiverAddress = receiverAddress;
        this.receiverPort = receiverPort;
        this.id = id;
        this.parentAdders = parentAdders;
        this.parentPort = parentPort;
        this.txt = msg;
    }

    @Override
    public MessageType getType() {
        return type;
    }

    public InetAddress getParentAdders() {
        return parentAdders;
    }
    public String getTxt() {
        return txt;
    }

    public int getReceiverPort() {
        return receiverPort;
    }

    @Override
    public UUID getId() {
        return id;
    }


    public void print() {
        System.out.println("------------------------------------");
        System.out.println(id.toString());
        System.out.println(type);
        System.out.println("receiver address: " + receiverAddress + " receiver port: " + receiverPort);
        if (parentAdders != null) {
            System.out.println("new parent is " + parentAdders + " " + parentPort);
        }
        if (txt != null && !txt.equals("")) {
            System.out.println(txt);
        }
        System.out.println("------------------------------------");

    }

    public InetAddress getReceiverAddress() {
        return receiverAddress;
    }
}
