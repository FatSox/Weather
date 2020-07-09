package com.example.weather.model.entity.current;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.weather.model.entity.WeatherApiDescription;
import com.example.weather.model.entity.forecast.ForecastEntryMain;
import com.example.weather.model.entity.forecast.ForecastWindEntry;

import java.util.ArrayList;

@Entity(tableName = "WeatherData")
public class WeatherEntity {
    @PrimaryKey
    public Long id = 0L;
    @Embedded
    public Coordinates coord;
    public ArrayList<WeatherApiDescription> weather;
    @Embedded
    public ForecastEntryMain main;
    @Embedded
    public ForecastWindEntry wind;
    public String name;
    public Long lastUpdateTime = 0L;

    public WeatherEntity() {

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
