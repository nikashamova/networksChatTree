package nsu.fit.shamova.messages.impl;

import nsu.fit.shamova.messages.Message;
import nsu.fit.shamova.messages.Type;

import java.net.InetAddress;
import java.util.UUID;

public class ParentMessage extends Message {
    //private InetAddress parent;

    public ParentMessage(int port, InetAddress receiver) {
        super(UUID.randomUUID(), port, Type.PARENT, receiver);
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
