package nsu.fit.shamova.messages;

import java.net.InetAddress;
import java.util.UUID;

public interface OutputMessage {
    MessageType getType();
    //InetAddress getSender();
    int getReceiverPort();
    UUID getId();
    //byte[] getByteMessage();
    InetAddress getReceiverAddress();
}
