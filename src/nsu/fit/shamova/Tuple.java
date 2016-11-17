package nsu.fit.shamova;

import nsu.fit.shamova.messages.IMessage;

public class Tuple {
    public final IMessage message;
    public int countOfTry;
    public long firstSendTime;

    public Tuple(IMessage message, int countOfTry, long firstSendTime) {
        this.message = message;
        this.countOfTry = countOfTry;
        this.firstSendTime = firstSendTime;
    }
}
