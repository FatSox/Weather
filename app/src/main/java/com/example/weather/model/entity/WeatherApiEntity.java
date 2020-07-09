package com.example.weather.model.entity;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity(tableName = "WeatherData")
public class WeatherApiEntity {
    @PrimaryKey
    public Long id = 0L;
    public Double lon = 0.0;
    public Double lat = 0.0;
    public String timezone = "";
    public long timezoneOffset = 0L;
    @Embedded
    public CurrentWeather current;
    public ArrayList<DailyWeather> daily;
    public Long lastUpdateTime = 0L;

    public WeatherApiEntity() {

    }

//    companion object {
//        fun toWeatherData(e: WeatherApiEntity): WeatherData =
//                WeatherData(
//                        lon = e.lon, lat = e.lat,
//                        timezone = e.timezone,
//                        timezoneOffset = e.timezoneOffset,
//                        temp = e.current.temp,
//                        feelsLike = e.current.feelsLike,
//                        windSpeed = e.current.windSpeed,
//                        windDeg = e.current.windDeg,
//                        weather = e.current.weather.map { w -> WeatherType(w.description, w.icon) },
//        daily = e.daily
//            )
//    }
}

