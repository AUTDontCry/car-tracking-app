package at.wifi.swdev.cartrackingapp.models;

public class Driver {
    private String driverID;
    private double latitude;
    private double longitude;

    public String getDriverID() {
        return driverID;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Driver(String driverID, double latitude, double longitude) {
        this.driverID = driverID;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
