package nsu.fit.shamova;

import java.net.InetAddress;

public class Host {
    private InetAddress address;
    private int port;

    public Host(InetAddress address, int port) {
        this.address = address;
        this.port = port;
    }

    public InetAddress getAddress() {
        return address;
    }

    public void setAddress(InetAddress address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public boolean equals(Object obj) {
        return (this.address.equals(((Host)obj).getAddress()) && (this.port == ((Host) obj).getPort()));
    }
}
