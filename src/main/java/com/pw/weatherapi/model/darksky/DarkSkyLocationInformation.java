package com.pw.weatherapi.model.darksky;

import com.pw.weatherapi.model.api.CityPosition;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DarkSkyLocationInformation {
    private CityPosition location;
    private Daily daily;
}
