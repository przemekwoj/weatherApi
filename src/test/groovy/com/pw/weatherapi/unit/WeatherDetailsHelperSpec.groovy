package com.pw.weatherapi.unit

import com.pw.weatherapi.infrascructure.api.WeatherDetailsHelper

import com.pw.weatherapi.model.darksky.WeatherDetails
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDate


class WeatherDetailsHelperSpec extends Specification {

    WeatherDetails createWeatherDetails(float temperatureLow, float temperatureHigh, float windSpeed) {
        WeatherDetails weatherDetails = new WeatherDetails();
        weatherDetails.setTemperatureHigh(temperatureHigh);
        weatherDetails.setTemperatureLow(temperatureLow);
        weatherDetails.setWindSpeed(windSpeed)
        return weatherDetails;
    }

    @Unroll
    def "should validate weather condition"() {
        given:
         WeatherDetails weatherDetails = createWeatherDetails(temperatureLow, temperatureHigh, windSpeed)
        when:
            boolean isValid = WeatherDetailsHelper.weatherConditionValidation(weatherDetails)
            then:
            isValid == expectedValue
        where:
            temperatureLow | temperatureHigh | windSpeed || expectedValue
            35             | 40              | 4         || false
            35             | 40              | 6         || false
            50             | 55              | 36        || false
            50             | 55              | 17        || true
    }

    @Unroll
    def "should compare weather conditions"() {
        given:
            WeatherDetails weatherDetails = createWeatherDetails(temperatureLow, temperatureHigh, windSpeed);
            WeatherDetails otherWeatherDetails = createWeatherDetails(otherTemperatureLow, otherTemperatureHigh, otherWindSpeed)
        when:
            int compareResult = WeatherDetailsHelper.weatherConditionComparator(weatherDetails,otherWeatherDetails)
        then:
            compareResult == expectedValue
        where:
            temperatureLow | temperatureHigh | windSpeed | otherTemperatureLow | otherTemperatureHigh | otherWindSpeed || expectedValue
            35             | 40              | 4         | 50                  | 55                   | 4              || 15
            35             | 40              | 6         | 50                  | 55                   | 10             || 27
            50             | 55              | 36        | 35                  | 45                   | 15             || -75
            50             | 55              | 17        | 35                  | 45                   | 30             || 26
            50             | 55              | 17        | 50                  | 55                   | 17             || 0
    }

    @Unroll
    def "should if timestamp is a given date"() {
            WeatherDetails weatherDetails = new WeatherDetails();
            weatherDetails.setTime(timestamp)
        when:
            boolean isGivenDate = WeatherDetailsHelper.isGivenDate(givenDate, weatherDetails)
        then:
            isGivenDate == expectedValue
        where:
            givenDate                                         | timestamp   || expectedValue
            new LocalDate(2020,8,9)  | 1597010400  || true
            new LocalDate(2020,8,9)  | 1597096800  || false
            new LocalDate(2020,8,10) | 1597010400  || false
            new LocalDate(2020,8,10) | 1597096800  || true
    }
}
