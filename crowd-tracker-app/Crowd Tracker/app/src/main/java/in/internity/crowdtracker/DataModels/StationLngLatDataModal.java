package in.internity.crowdtracker.DataModels;

public class StationLngLatDataModal {
    private double longitude ,latitude;
    private  String stationName;

    public StationLngLatDataModal(double longitude, double latitude, String stationName) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.stationName = stationName;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }
}
