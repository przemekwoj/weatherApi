package com.pw.weatherapi.controllers;

import com.pw.weatherapi.exceptions.LocationNotFound;
import com.pw.weatherapi.infrascructure.api.LocationService;
import com.pw.weatherapi.model.darksky.WeatherDetails;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;

@RestController
@Validated
public class WeatherController {
    private final LocationService locationService;

    public WeatherController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping(value = "{date}"
            , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WeatherDetails> getBestWeatherLocation(@PathVariable
                                                                 @Valid @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) throws LocationNotFound {
        WeatherDetails bestLocation = locationService.getBestWeatherLocation(date);

        return ResponseEntity
                .ok(bestLocation);
    }
}
