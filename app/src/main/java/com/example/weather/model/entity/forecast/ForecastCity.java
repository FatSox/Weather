package com.example.weather.model.entity.forecast;

import androidx.room.Embedded;

import com.example.weather.model.entity.current.Coordinates;

public class ForecastCity {
    public String name = "";
    public Long sunrise = 0L;
    public Long sunset = 0L;
    @Embedded
    public Coordinates coord;

    public ForecastCity() {

    }
}
