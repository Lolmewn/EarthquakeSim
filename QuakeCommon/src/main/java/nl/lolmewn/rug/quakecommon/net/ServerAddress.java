package nl.lolmewn.rug.quakecommon.net;

import java.util.Objects;

/**
 * Class holding the address and port of a Server.
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
    /**
     * {@inheritDoc }
     */
    public String toString() {
        return "ServerAddress{address=" + address + ":" + port + '}';
    }

    @Override
    /**
     * {@inheritDoc }
     */
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.address);
        hash = 79 * hash + this.port;
        return hash;
    }

    @Override
    /**
     * {@inheritDoc }
     */
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
