package nsu.fit.shamova.messages.impl;

import nsu.fit.shamova.messages.Message;
import nsu.fit.shamova.messages.MessageType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;

public class TextMessage extends Message{
    String txt;
    public TextMessage(InetAddress receiverAddress, int receiverPort, String txt) {
        super(UUID.randomUUID(), MessageType.TXT, receiverAddress, receiverPort);
        this.txt = txt;
    }

    public TextMessage(UUID id, InetAddress receiverAddress, int receiverPort, String txt) {
        super(id, MessageType.TXT, receiverAddress, receiverPort);
        this.txt = txt;
    }

    byte[] getByteTxt() {
        return txt.getBytes();
    }

    @Override
    public byte[] getByteMessage() {
        byte[] result = null;
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(makeHeader());
            outputStream.write(txt.getBytes());
            result = outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}

