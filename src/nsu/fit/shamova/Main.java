package nsu.fit.shamova;

import nsu.fit.shamova.messages.Acknowledge;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Main {

    public static void main(String[] args) {
        Acknowledge a = null;
        a = new Acknowledge(14203, null);
        System.out.println(a.getId() + " " + a.geyType() + " " + a.getSender() + " " + a.getPort());
    }
}
