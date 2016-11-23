package nsu.fit.shamova.messages;

import nsu.fit.shamova.messages.impl.InputMessage;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.UUID;

public class MessageCreator {

    public static InputMessage createMessage(byte[] msg) throws UnknownHostException {
        int offset = 0;
        MessageType type = MessageType.getByBate(msg[0]);
        offset++;
        //System.out.println(type);

        ByteBuffer bb = ByteBuffer.wrap(msg, offset, 16);
        long high = bb.getLong();
        long low = bb.getLong();
        UUID id = new UUID(high, low);
        offset += Long.BYTES * 2;
      //  System.out.println(id.toString());

        byte[] receiverBytes = new byte[4];
        System.arraycopy(msg, offset, receiverBytes, 0, 4);
        InetAddress receiverAddress = InetAddress.getByAddress(receiverBytes);
        offset += 4;
       // System.out.println(receiverAddress);

        int receiverPort = ByteBuffer.wrap(msg, offset, 4).getInt();
        offset += 4;
        //System.out.println(receiverPort);

        InetAddress parentAddress = null;
        int parentPort = 0;
        if (type == MessageType.PARENT) {
            byte[] parentBytes = new byte[4];
            System.arraycopy(msg, offset, parentBytes, 0, 4);
            parentAddress = InetAddress.getByAddress(parentBytes);
            offset += 4;
            //System.out.println(parentAddress);

            parentPort = ByteBuffer.wrap(msg, offset, 4).getInt();
            offset += 4;
            //System.out.println(parentPort);
        }

        String txt = null;
        if (type == MessageType.TXT) {
            byte[] txtBytes = new byte[msg.length - offset];
            System.arraycopy(msg, offset, txtBytes, 0, msg.length - offset);
            txt = new String(txtBytes);
            //System.out.println(txt);
        }
        return new InputMessage(id, type, receiverAddress, receiverPort, parentAddress, parentPort, txt);
    }

}
