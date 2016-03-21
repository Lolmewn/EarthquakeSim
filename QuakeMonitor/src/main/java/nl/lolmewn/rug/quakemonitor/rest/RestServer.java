package nl.lolmewn.rug.quakemonitor.rest;

import nl.lolmewn.rug.quakemonitor.net.SocketServer;
import static spark.Spark.*;

/**
 * Rest API server class Responsible for setting up the REST API Request
 * responses
 *
 * @author Sybren
 */
public class RestServer {

    /**
     * Constructor for the Rest API server
     *
     * @param server SocketServer used currently
     */
    public RestServer(SocketServer server) {
        get("/status", (req, res) -> server.getPort());
    }

}
