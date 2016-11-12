package nsu.fit.shamova.messages;

import java.net.InetAddress;
import java.util.UUID;

public interface Message {
    String geyType();
    InetAddress getSender();
    int getPort();
    UUID getId();
}
