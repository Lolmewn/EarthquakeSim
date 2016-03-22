package nl.lolmewn.rug.quakemonitor;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import nl.lolmewn.rug.quakemonitor.net.SensorInfo;

/**
 *
 * @author Lolmewn
 */
public class SensorTableData {

    private final SimpleStringProperty uuid;
    private final SimpleDoubleProperty latitude;
    private final SimpleDoubleProperty longitude;
    private final SimpleDoubleProperty acceleration;

    public SensorTableData(SensorInfo sensorInfo) {
        this.uuid = new SimpleStringProperty(sensorInfo.getUuid().toString());
        this.latitude = new SimpleDoubleProperty(sensorInfo.getLatitude());
        this.longitude = new SimpleDoubleProperty(sensorInfo.getLongitude());
        this.acceleration = new SimpleDoubleProperty(0);
    }

    public String getUuid() {
        return uuid.get();
    }

    public Double getLatitude() {
        return latitude.get();
    }

    public Double getLongitude() {
        return longitude.get();
    }

    public Double getAcceleration() {
        return acceleration.get();
    }

    public void setAcceleration(double acceleration) {
        this.acceleration.set(acceleration);
    }

}
