package nl.lolmewn.rug.quakemonitor.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import nl.lolmewn.rug.quakemonitor.net.Server;

/**
 * @author Sybren
 */
@Path("status")
public class Status {

    private final Server server;

    public Status(Server server) {
        this.server = server;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getPort() {
        return Integer.toString(server.getPort());
    }

}
