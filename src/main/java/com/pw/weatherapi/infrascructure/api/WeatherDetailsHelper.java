package com.pw.weatherapi.infrascructure.api;

import com.pw.weatherapi.model.darksky.WeatherDetails;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class WeatherDetailsHelper {

    private static final long TIME_CONSTANT = 86400000;
    private static final long TIME_FACTOR = 1000;
    private static final int TEMPERATURE_FAHNRENHEIT_HIGH_LIMIT = 95;
    private static final int TEMPERATURE_FAHNRENHEIT_LOW_LIMIT = 41;
    private static final int WIND_SPEED_HIGH_LIMIT = 18;
    private static final int WIND_SPEED_LOW_LIMIT = 5;

    public static boolean weatherConditionValidation(WeatherDetails weatherDetails) {
        var averageTemperature = (weatherDetails.getTemperatureHigh() + weatherDetails.getTemperatureLow()) / 2;
        return temperatureValid(averageTemperature) && windValid(weatherDetails.getWindSpeed());
    }

    private static boolean windValid(float windSpeed) {
        return (windSpeed < WIND_SPEED_HIGH_LIMIT && windSpeed > WIND_SPEED_LOW_LIMIT);
    }

    private static boolean temperatureValid(double averageTemperature) {
        return (averageTemperature < TEMPERATURE_FAHNRENHEIT_HIGH_LIMIT && averageTemperature > TEMPERATURE_FAHNRENHEIT_LOW_LIMIT);
    }

    public static int weatherConditionComparator(WeatherDetails weatherDetails, WeatherDetails otherWeatherDetails) {
        var calculation = weatherDetails.getWindSpeed() * 3 + (weatherDetails.getTemperatureHigh() + weatherDetails.getTemperatureLow()) / 2;
        var otherCalculation = otherWeatherDetails.getWindSpeed() * 3 + (otherWeatherDetails.getTemperatureHigh() + otherWeatherDetails.getTemperatureLow()) / 2;
        return (int) (otherCalculation - calculation);
    }

    public static boolean isGivenDate(LocalDate date, WeatherDetails weatherDetails) {
        return date.equals(calculateDate(weatherDetails.getTime()));
    }

    private static LocalDate calculateDate(long time) {
        return Instant.ofEpochMilli(time * TIME_FACTOR - TIME_CONSTANT).atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
