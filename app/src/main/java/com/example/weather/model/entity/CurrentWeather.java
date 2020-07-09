package com.example.weather.model.entity;

import java.util.ArrayList;

public class CurrentWeather {
    public Long dt = 0L;
    public Double temp = 0.0;
    public Double feelsLike = 0.0;
    public Double windSpeed = 0.0;
    public Integer windDeg = 0;
    public ArrayList<WeatherApiDescription> weather;

    public ArrayList<String> getIconUrls() {
        ArrayList<String> urls = new ArrayList<>();

        for (WeatherApiDescription w : weather) {
            String url = String.format("https://openweathermap.org/img/wn/%s.png", w.icon);
            urls.add(url);
        }

        return urls;
    }
}


