package com.example.weather.model.entity;

import androidx.room.Embedded;

import java.util.ArrayList;

public class DailyWeather {
    public long dt = 0L;
    @Embedded
    public DailyTemperature temp;
    @Embedded
    public FeelsLikeTemperature feelsLike;
    public double windSpeed = 0.0;
    public int windDeg = 0;
    public ArrayList<WeatherApiDescription> weather;

    public DailyWeather() {

    }

    public ArrayList<String> getIconUrls() {
        ArrayList<String> urls = new ArrayList<>();

        for (WeatherApiDescription w : weather) {
            String url = String.format("https://openweathermap.org/img/wn/%s.png", w.icon);
            urls.add(url);
        }

        return urls;
    }
}
