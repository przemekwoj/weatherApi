package com.pw.weatherapi.controllers;

import com.pw.weatherapi.exceptions.LocationNotFound;
import com.pw.weatherapi.infrascructure.api.LocationService;
import com.pw.weatherapi.model.darksky.WeatherDetails;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
public class WeatherController {
    private final LocationService locationService;

    private WeatherController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping(value = "{date}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WeatherDetails> getBestWeatherLocation(@PathVariable
                                                                 @DateTimeFormat(pattern = "yyyy-MM-dd")final LocalDate date) throws LocationNotFound {

        var bestLocation = locationService.getBestWeatherLocation(date);

        return ResponseEntity
                .ok(bestLocation);
    }
}
