package nsu.fit.shamova.messages;

import nsu.fit.shamova.messages.Message;

public class MessageHolder {
    public final Message message;
    public int countOfTry;
    public final long firstSendTime;

    public MessageHolder(Message message) {
        this.message = message;
        this.countOfTry = 0;
        this.firstSendTime = System.currentTimeMillis();
    }
}
