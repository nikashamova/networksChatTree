package nsu.fit.shamova.messages.impl;

import nsu.fit.shamova.messages.Message;
import nsu.fit.shamova.messages.Type;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;

public class TextMessage extends Message{
    String txt;
    public TextMessage(int port, String txt, InetAddress receiver) {
        super(UUID.randomUUID(), port, Type.MSG, receiver);
        this.txt = txt;
    }

    public TextMessage(UUID id, int port, String txt, InetAddress receiver) {
        super(id, port, Type.MSG, receiver);
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

