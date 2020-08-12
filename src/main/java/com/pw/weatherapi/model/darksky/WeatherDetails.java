package com.pw.weatherapi.model.darksky;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherDetails implements Comparable<WeatherDetails> {
    private String locationName;
    private long time;
    private String summary;
    private String icon;
    private long sunriseTime;
    private long sunsetTime;
    private float moonPhase;
    private float precipIntensity;
    private float precipIntensityMax;
    private long precipIntensityMaxTime;
    private float precipProbability;
    private String precipType;
    private float temperatureHigh;
    private long temperatureHighTime;
    private float temperatureLow;
    private long temperatureLowTime;
    private float apparentTemperatureHigh;
    private long apparentTemperatureHighTime;
    private float apparentTemperatureLow;
    private long apparentTemperatureLowTime;
    private float dewPoint;
    private float humidity;
    private float pressure;
    private float windSpeed;
    private float windGust;
    private long windGustTime;
    private int windBearing;
    private float cloudCover;
    private int uvIndex;
    private long uvIndexTime;
    private int visibility;
    private float ozone;
    private float temperatureMin;
    private long temperatureMinTime;
    private float temperatureMax;
    private long temperatureMaxTime;
    private float apparentTemperatureMin;
    private long apparentTemperatureMinTime;
    private float apparentTemperatureMax;
    private long apparentTemperatureMaxTime;

    @Override
    public int compareTo(WeatherDetails otherWeatherDetails) {
        float calculation = windSpeed * 3 + (temperatureHigh + temperatureLow) / 2;
        float otherCalculation = otherWeatherDetails.getWindSpeed() * 3 + (otherWeatherDetails.getTemperatureHigh() + otherWeatherDetails.getTemperatureLow()) / 2;
        return (int) (otherCalculation - calculation);
    }
}
