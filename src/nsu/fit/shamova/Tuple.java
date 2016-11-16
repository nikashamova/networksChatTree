package nsu.fit.shamova;

import nsu.fit.shamova.messages.IMessage;

public class Tuple {
    public final IMessage x;
    public int y;
    public long z;

    public Tuple(IMessage x, int y, long z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
