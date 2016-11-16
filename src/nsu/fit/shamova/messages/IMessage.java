package nsu.fit.shamova.messages;

import java.net.InetAddress;
import java.util.UUID;

public interface IMessage {
    Type getType();
    InetAddress getSender();
    int getPort();
    UUID getId();
    byte[] getByteMessage();
}
