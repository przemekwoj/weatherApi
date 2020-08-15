package com.pw.weatherapi.model.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CityPosition {
    JASTARNIA(54.6957333, 18.6788396),
    WŁADYSŁAWOWO(54.759159, 18.508961);

    private final double latitude;
    private final double longitude;
}
