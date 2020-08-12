package com.pw.weatherapi.model.api;

public enum CityPosition {
    JASTARNIA(54.6957333, 18.6788396),
    WŁADYSŁAWOWO(54.759159, 18.508961);

    private final double latitude;
    private final double longitude;


    CityPosition(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
