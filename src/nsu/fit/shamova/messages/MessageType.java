package nsu.fit.shamova.messages;

public enum MessageType {
    ACK, MSG, PARENT, DISCONNECTED, ROOT, CONNECTED;
    private byte t;

    static {
        ACK.t = 0;
        MSG.t = 1;
        PARENT.t = 2;
        DISCONNECTED.t = 3;
        ROOT.t = 4;
        CONNECTED.t = 5;
    }

    public static MessageType getByBate(byte a) {
        switch (a) {
            case 0:
                return MessageType.ACK;
            case 1:
                return MessageType.MSG;
            case 2:
                return MessageType.PARENT;
            case 3:
                return MessageType.DISCONNECTED;
            case 4:
                return MessageType.ROOT;
            case 5:
                return MessageType.CONNECTED;
        }
        return null;
    }

    public byte getT() {
        return t;
    }
}
