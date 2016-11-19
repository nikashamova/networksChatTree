package nsu.fit.shamova.messages.impl;

import nsu.fit.shamova.messages.Message;
import nsu.fit.shamova.messages.Type;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;

public class ParentMessage extends Message {
    //private InetAddress parent;

    public ParentMessage(InetAddress addr, int port, InetAddress parent) {
        super(UUID.randomUUID(), addr, port, Type.PARENT);
      //  this.parent = parent;
    }

   /* @Override
    public byte[] getByteMessage() {
        *//*byte[] result = null;
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(makeHeader());
        //    outputStream.write(parent.getAddress());
            result = outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }*//*
        return makeHeader();
        //result;
    }*/
}
