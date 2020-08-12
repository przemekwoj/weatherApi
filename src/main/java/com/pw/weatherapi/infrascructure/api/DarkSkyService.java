package com.pw.weatherapi.infrascructure.api;

import com.pw.weatherapi.model.api.CityPosition;
import com.pw.weatherapi.model.darksky.DarkSkyLocationInformation;

public interface DarkSkyService {
    DarkSkyLocationInformation getLocationInformation(CityPosition cityPosition);
}
