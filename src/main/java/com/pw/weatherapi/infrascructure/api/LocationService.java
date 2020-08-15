package com.pw.weatherapi.infrascructure.api;

import com.pw.weatherapi.exceptions.LocationNotFound;
import com.pw.weatherapi.model.api.CityPosition;
import com.pw.weatherapi.model.darksky.DarkSkyLocationInformation;
import com.pw.weatherapi.model.darksky.WeatherDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.pw.weatherapi.infrascructure.api.WeatherDetailsHelper.isGivenDate;
import static com.pw.weatherapi.infrascructure.api.WeatherDetailsHelper.weatherConditionValidation;

@Service
public class LocationService {
    private final DarkSkyService darkSkyService;

    public LocationService(DarkSkyService darkSkyService) {
        this.darkSkyService = darkSkyService;
    }

    public WeatherDetails getBestWeatherLocation(LocalDate date) throws LocationNotFound {
        var locations = getAllLocations();
        var locationsWeatherDetails = locationsInformationAtGivenDay(locations, date);
        return chooseBestLocation(locationsWeatherDetails);
    }

    private List<WeatherDetails> locationsInformationAtGivenDay(List<DarkSkyLocationInformation> locations, LocalDate date) {
        var locationsInformationAtGivenDay = new ArrayList<WeatherDetails>();
        locations.forEach(location -> {
            for (var weatherDetails : location.getDaily().getData()) {
                if (isGivenDate(date, weatherDetails)) {
                    addLocationDetails(locationsInformationAtGivenDay, weatherDetails, location.getLocation().name());
                    break;
                }
            }
        });
        return locationsInformationAtGivenDay;
    }

    private void addLocationDetails(List<WeatherDetails> weatherDetailsList, WeatherDetails weatherDetails, String locationName) {
        weatherDetails.setLocationName(locationName);
        weatherDetailsList.add(weatherDetails);
    }

    private List<DarkSkyLocationInformation> getAllLocations() {
        return Stream.of(CityPosition.values()).map(city -> darkSkyService.getLocationInformation(city)).collect(Collectors.toList());
    }

    private WeatherDetails chooseBestLocation(List<WeatherDetails> locations) throws LocationNotFound {
        return locations.stream()
                .filter(weatherDetails -> weatherConditionValidation(weatherDetails))
                .sorted(WeatherDetailsHelper::weatherConditionComparator)
                .findFirst().orElseThrow(() -> new LocationNotFound("Not found proper location"));
    }

}
