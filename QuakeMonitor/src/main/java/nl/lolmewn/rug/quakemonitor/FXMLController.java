package nl.lolmewn.rug.quakemonitor;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MapOptions;
import com.lynden.gmapsfx.javascript.object.MapTypeIdEnum;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ResourceBundle;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import nl.lolmewn.rug.quakecommon.Settings;
import nl.lolmewn.rug.quakemonitor.mq.QuakeDataConsumer;
import nl.lolmewn.rug.quakemonitor.net.SocketServer;
import nl.lolmewn.rug.quakemonitor.rest.RestServer;

public class FXMLController implements Initializable, MapComponentInitializedListener {

    private static final NumberFormat FORMATTER = new DecimalFormat("#0.000000");
    private GoogleMap map;

    private Settings settings;
    private SocketServer server;

    /**
     * FXML variables, these get loaded by JavaFX
     */
    @FXML
    private GoogleMapView mapView;
    @FXML
    private Label longCoord;
    @FXML
    private Label latCoord;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        mapView.addMapInializedListener(this);
        try {
            this.settings = new Settings("config.properties");
        } catch (IOException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Couldn't load settings; shutting down QuakeServer...");
            System.exit(1);
        }
        this.server = new SocketServer(5000, settings);
        try {
            this.server.start();
        } catch (IOException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Couldn't start server; shutting down QuakeServer...");
            System.exit(1);
        }
        new RestServer(server);
        try {
            new QuakeDataConsumer();
        } catch (IOException | TimeoutException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Could not start quake data consumer; shutting down QuakeServer...");
            System.exit(1);
        }
    }

    @Override
    public void mapInitialized() {

        MapOptions mapOptions = new MapOptions();

        mapOptions.center(new LatLong(53.22, 6.5665))
                .mapType(MapTypeIdEnum.TERRAIN)
                .overviewMapControl(false)
                .panControl(false)
                .rotateControl(false)
                .scaleControl(false)
                .streetViewControl(false)
                .zoomControl(false)
                .zoom(12);

        map = mapView.createMap(mapOptions);

        //Add a marker to the map
        MarkerOptions markerOptions = new MarkerOptions();

        markerOptions.position(new LatLong(47.6, -122.3))
                .visible(Boolean.TRUE)
                .title("My Marker");

        Marker marker = new Marker(markerOptions);

        map.addMarker(marker);
        mapView.addEventHandler(EventType.ROOT, (e) -> {
            mouseMoveMap();
        });
        mouseMoveMap();
    }

    private void mouseMoveMap() {
        LatLong coords = mapView.getMap().getCenter();
        latCoord.setText(FORMATTER.format(coords.getLatitude()));
        longCoord.setText(FORMATTER.format(coords.getLongitude()));
    }
}
