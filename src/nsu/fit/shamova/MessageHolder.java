package nsu.fit.shamova;

import nsu.fit.shamova.messages.IMessage;

public class MessageHolder {
    public final IMessage message;
    public int countOfTry;
    public final long firstSendTime;

    public MessageHolder(IMessage message, int countOfTry, long firstSendTime) {
        this.message = message;
        this.countOfTry = countOfTry;
        this.firstSendTime = firstSendTime;
    }
}
