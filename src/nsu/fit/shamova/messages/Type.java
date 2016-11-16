package nsu.fit.shamova.messages;

public enum Type {
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

    public static Type getByBate(byte a) {
        switch (a) {
            case 0:
                return Type.ACK;
            case 1:
                return Type.MSG;
            case 2:
                return Type.PARENT;
            case 3:
                return Type.DISCONNECTED;
            case 4:
                return Type.ROOT;
            case 5:
                return Type.CONNECTED;
        }
        return null;
    }

    public byte getT() {
        return t;
    }
}
