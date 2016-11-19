package nsu.fit.shamova;

import nsu.fit.shamova.messages.MessageCreator;
import nsu.fit.shamova.messages.impl.AckMessage;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Main {

    public static void main(String[] args) throws UnknownHostException {
        AckMessage a = null;
/*        a = new AckMessage(14203, InetAddress.getByName("192.168.0.1"));
        System.out.println(a.getId() + " " + a.getType() + " " + a.getSender() + " " + a.getPort());
        System.out.println(a.makeHeader());
        System.out.println(a.getId().toString());
        MessageCreator messageCreator = new MessageCreator(a.makeHeader());
        messageCreator.createMessage();
    */
    }
}
