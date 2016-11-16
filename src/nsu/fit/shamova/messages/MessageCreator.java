package nsu.fit.shamova.messages;

import nsu.fit.shamova.messages.impl.AckMessage;
import nsu.fit.shamova.messages.impl.InputMessage;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.UUID;

public class MessageCreator {
  //  private byte[] msg;
/*
    public MessageCreator() {
        this.msg = msg;
    }*/

    public static InputMessage createMessage(byte[] msg) throws UnknownHostException {
        int offset = 0;
        Type type = Type.getByBate(msg[0]);
        offset++;
        ByteBuffer bb = ByteBuffer.wrap(msg, offset, 16);
        long high = bb.getLong();
        long low = bb.getLong();
        UUID id = new UUID(high, low);
        System.out.println(id.toString());
        offset += Long.BYTES * 2;
        byte[] a = new byte[4];
        System.arraycopy(msg, offset, a, 0, 4);
        InetAddress addr = InetAddress.getByAddress(a);
        offset += 4;
        int port = ByteBuffer.wrap(msg, offset, 4).getInt();
        return new InputMessage(type, addr, port, id);
    }
}
