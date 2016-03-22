package nl.lolmewn.rug.quakemonitor;

import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.shapes.Circle;
import nl.lolmewn.rug.quakemonitor.net.SensorInfo;

/**
 *
 * @author Lolmewn
 */
public class MapMarkers {

    private final SensorInfo info;
    private Marker marker;
    private Circle quake;

    public MapMarkers(SensorInfo info) {
        this.info = info;
    }

    public void quake(double acceleration) {
        if(quake == null){
            return; // Map not initialized yer
        }
        quake.setRadius(acceleration * 50); // Gotta make it bigger so it's visible
    }

    public void activate(GoogleMap map) {
        /*LatLong location = new LatLong(info.getLatitude(), info.getLongitude());
        MarkerOptions options = new MarkerOptions();

        options.position(location);
        options.title(info.getUuid().toString());
        options.visible(true);
        this.marker = new Marker(options);

        CircleOptions opts = new CircleOptions();
        opts.center(location);
        opts.radius(0);
        opts.visible(true);
        quake = new Circle(opts);
        map.addMarker(marker);
        map.addMapShape(quake);*/
    }

}
