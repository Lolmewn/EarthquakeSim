package nl.lolmewn.rug.quakemonitor;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MapOptions;
import com.lynden.gmapsfx.javascript.object.MapTypeIdEnum;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ResourceBundle;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class FXMLController implements Initializable, MapComponentInitializedListener {

    private static final NumberFormat FORMATTER = new DecimalFormat("#0.000000");
    
    @FXML
    private GoogleMapView mapView;
    @FXML
    private Label longCoord;
    @FXML
    private Label latCoord;
    private GoogleMap map;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        mapView.addMapInializedListener(this);
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
    }

    private void mouseMoveMap() {
        LatLong coords = mapView.getMap().getCenter();
        latCoord.setText(FORMATTER.format(coords.getLatitude()));
        longCoord.setText(FORMATTER.format(coords.getLongitude()));
    }
}
