package nl.lolmewn.rug.quakemonitor.rest;

import nl.lolmewn.rug.quakemonitor.net.SocketServer;
import static spark.Spark.*;

/**
 * @author Sybren
 */
public class RestServer {

    private final SocketServer server;

    public RestServer(SocketServer server) {
        this.server = server;
        get("/status", (req, res)-> server.getPort());
    }

}
