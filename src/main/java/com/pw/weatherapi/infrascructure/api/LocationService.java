package com.pw.weatherapi.infrascructure.api;

import com.pw.weatherapi.exceptions.LocationNotFound;
import com.pw.weatherapi.model.api.CityPosition;
import com.pw.weatherapi.model.darksky.DarkSkyLocationInformation;
import com.pw.weatherapi.model.darksky.WeatherDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

@Service
public class LocationService {
    private final DarkSkyService darkSkyService;

    public LocationService(DarkSkyService darkSkyService) {
        this.darkSkyService = darkSkyService;
    }

    public WeatherDetails getBestWeatherLocation(Date date) throws LocationNotFound {
        List<DarkSkyLocationInformation> locations = getAllLocations();
        List<WeatherDetails> locationsWeatherDetails = locationsInformationAtGivenDay(locations, date);
        return chooseBestLocation(locationsWeatherDetails);
    }

    private List<WeatherDetails> locationsInformationAtGivenDay(List<DarkSkyLocationInformation> locations, Date date) {
        List<WeatherDetails> locationsInformationAtGivenDay = new ArrayList<>();
        locations.forEach(location -> {
            for (WeatherDetails weatherDetails : location.getDaily().getData()) {
                if (isGivenDate(date, weatherDetails)) {
                    addLocationDetails(locationsInformationAtGivenDay, weatherDetails, location.getLocation().name());
                    break;
                }
            }
        });
        return locationsInformationAtGivenDay;
    }

    private boolean isGivenDate(Date date, WeatherDetails weatherDetails) {
        return date.equals(new Date(calculateDate(weatherDetails.getTime())));
    }

    long calculateDate(long time) {
        return time * 1000 - 86400000;
    }

    private void addLocationDetails(List<WeatherDetails> weatherDetailsList, WeatherDetails weatherDetails, String locationName) {
        weatherDetails.setLocationName(locationName);
        weatherDetailsList.add(weatherDetails);
    }

    private List<DarkSkyLocationInformation> getAllLocations() {
        List<DarkSkyLocationInformation> locations = new ArrayList<>();
        EnumSet.allOf(CityPosition.class)
                .forEach(city -> locations.add(darkSkyService.getLocationInformation(city)));
        return locations;
    }

    private WeatherDetails chooseBestLocation(List<WeatherDetails> locations) throws LocationNotFound {
        return locations.stream()
                .filter(this::selectLocationWithGoodWeatherCondition)
                .sorted()
                .findFirst().orElseThrow(() -> new LocationNotFound("Not found proper location"));
    }

    private boolean selectLocationWithGoodWeatherCondition(WeatherDetails location) {
        float windSpeed = location.getWindSpeed();
        double temperatureHigh = (location.getTemperatureHigh() - 32) / 1.8;
        double temperatureLow = (location.getTemperatureLow() - 32) / 1.8;
        double averageTemperature = (temperatureHigh + temperatureLow) / 2;
        return !((averageTemperature > 35 || averageTemperature < 5) && (windSpeed > 18 || windSpeed < 5));
    }
}
