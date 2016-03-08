package nl.lolmewn.rug.quakemonitor.rest;

import nl.lolmewn.rug.quakemonitor.net.Server;
import static spark.Spark.*;

/**
 * @author Sybren
 */
public class Status {

    private final Server server;

    public Status(Server server) {
        this.server = server;
        get("/status", (req, res)-> "");
    }

}
