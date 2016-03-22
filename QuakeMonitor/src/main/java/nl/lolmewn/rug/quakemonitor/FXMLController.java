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
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import nl.lolmewn.rug.quakecommon.Settings;
import nl.lolmewn.rug.quakecommon.net.ServerManager;
import nl.lolmewn.rug.quakecommon.net.ServerSyncer;
import nl.lolmewn.rug.quakemonitor.mq.QuakeDataConsumer;
import nl.lolmewn.rug.quakemonitor.net.SensorInfo;
import nl.lolmewn.rug.quakemonitor.net.SocketServer;
import nl.lolmewn.rug.quakemonitor.rest.RestServer;

public class FXMLController implements Initializable, MapComponentInitializedListener {

    private static final NumberFormat FORMATTER = new DecimalFormat("#0.000000");
    private GoogleMap map;

    private Settings settings;
    private ServerManager serverManager;
    private SocketServer server;
    private final HashMap<SensorInfo, MapMarkers> markers = new HashMap<>();
    private final HashMap<UUID, SensorInfo> sensors = new HashMap<>();
    private final ObservableList<SensorTableData> tableData = FXCollections.observableArrayList();

    /**
     * FXML variables, these get loaded by JavaFX
     */
    @FXML
    private GoogleMapView mapView;
    @FXML
    private Label longCoord;
    @FXML
    private Label latCoord;
    @FXML
    private TableView<SensorTableData> tableView;
    @FXML
    private TableColumn<SensorTableData, String> uuidColumn;
    @FXML
    private TableColumn<SensorTableData, Double> latitudeColumn;
    @FXML
    private TableColumn<SensorTableData, Double> longitudeColumn;
    @FXML
    private TableColumn<SensorTableData, Double> acccelerationColumn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        mapView.addMapInializedListener(this);
        loadSettings();
        loadServerManager();
        new ServerSyncer(settings, serverManager);
        this.server = new SocketServer(this);
        try {
            this.server.start();
        } catch (IOException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Couldn't start server; shutting down QuakeServer...");
            System.exit(1);
        }
        new RestServer(server);
        try {
            new QuakeDataConsumer(this);
        } catch (IOException | TimeoutException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Could not start quake data consumer; shutting down QuakeServer...");
            System.exit(1);
        }
        tableView.setItems(tableData);
        uuidColumn.setCellValueFactory(new PropertyValueFactory<>("uuid"));
        latitudeColumn.setCellValueFactory(new PropertyValueFactory<>("latitude"));
        longitudeColumn.setCellValueFactory(new PropertyValueFactory<>("longitude"));
        acccelerationColumn.setCellValueFactory(new PropertyValueFactory<>("acceleration"));
    }

    @Override
    public void mapInitialized() {
        MapOptions mapOptions = new MapOptions();
        mapOptions.center(new LatLong(53.22, 6.5665)) // groningen
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

        markers.values().stream().forEach((mapMarkers) -> {
            activate(mapMarkers);
        });
    }

    private void mouseMoveMap() {
        LatLong coords = mapView.getMap().getCenter();
        latCoord.setText(FORMATTER.format(coords.getLatitude()));
        longCoord.setText(FORMATTER.format(coords.getLongitude()));
    }

    private void loadSettings() {
        try {
            this.settings = new Settings("config.properties");
        } catch (IOException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Couldn't load settings; shutting down QuakeServer...");
            System.exit(1);
        }
    }

    private void loadServerManager() {
        this.serverManager = new ServerManager(settings);
    }

    public Settings getSettings() {
        return this.settings;
    }

    public GoogleMap getMap() {
        return map;
    }

    public HashMap<SensorInfo, MapMarkers> getMarkers() {
        return markers;
    }

    public void activate(MapMarkers markers) {
        if (map == null) {
            return; // not yet initialized
        }
        markers.activate(map);
    }

    public void quake(SensorInfo info, double acceleration) {
        // find row
        for (SensorTableData data : this.tableData) {
            if (data.getUuid().equals(info.getUuid().toString())) {
                // great
                data.setAcceleration(acceleration);
                tableView.refresh();
                return;
            }
        }
        // not in there
        addSensor(info);
    }

    public void addSensor(SensorInfo info) {
        sensors.put(info.getUuid(), info);
        this.tableData.add(new SensorTableData(info));
        tableView.refresh();
    }

    public SensorInfo getSensor(UUID uuid) {
        return sensors.get(uuid);
    }

    public void removeSensor(UUID uuid) {
        sensors.remove(uuid);
    }
}
