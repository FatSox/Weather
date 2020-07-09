package com.example.weather.model.entity.forecast;

import com.example.weather.model.entity.WeatherApiDescription;

import java.util.ArrayList;

public class ForecastEntry {
    public Long dt = 0L;
    public ForecastEntryMain main;
    public ArrayList<WeatherApiDescription> weather;
    public ForecastWindEntry wind;
    public String dtTxt;

    public ForecastEntry() {

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
