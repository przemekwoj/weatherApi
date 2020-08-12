package com.pw.weatherapi.model.darksky;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Daily {
    private List<WeatherDetails> data;
}
