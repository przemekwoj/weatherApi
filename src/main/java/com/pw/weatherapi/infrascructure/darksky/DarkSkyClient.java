package com.pw.weatherapi.infrascructure.darksky;

import com.pw.weatherapi.infrascructure.api.DarkSkyService;
import com.pw.weatherapi.model.api.CityPosition;
import com.pw.weatherapi.model.darksky.DarkSkyLocationInformation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class DarkSkyClient implements DarkSkyService {

    private final String baseURL;
    private final String key;

    public DarkSkyClient(@Value("${darkSky.baseUrl}") String baseURL,
                         @Value("${darkSky.key}") String key) {
        this.baseURL = baseURL;
        this.key = key;
    }

    @Override
    public DarkSkyLocationInformation getLocationInformation(CityPosition cityPosition) {
        var url = buildURL(cityPosition).toString();
        var darkSkyLocationInformation = new RestTemplate().getForEntity(url, DarkSkyLocationInformation.class)
                .getBody();
        darkSkyLocationInformation.setLocation(cityPosition);
        return darkSkyLocationInformation;
    }

    UriComponents buildURL(CityPosition cityPosition) {
        return UriComponentsBuilder.fromUri(URI.create(baseURL))
                .pathSegment(key)
                .pathSegment(cityPosition.getLatitude() + "," + cityPosition.getLongitude())
                .build();
    }

}
