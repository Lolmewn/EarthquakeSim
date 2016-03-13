package nl.lolmewn.rug.quakecommon.net;

import java.util.Objects;

/**
 *
 * @author Lolmewn
 */
public class ServerAddress {

    private final String address;
    private final int port;

    public ServerAddress(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return "ServerAddress{address=" + address + ":" + port + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.address);
        hash = 79 * hash + this.port;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ServerAddress other = (ServerAddress) obj;
        if (this.port != other.port) {
            return false;
        }
        return Objects.equals(this.address, other.address);
    }

}
