package com.example.weather.model.entity.forecast;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.weather.ui.ForecastFragment;

import java.util.ArrayList;

@Entity(tableName = "ForecastData")
public class ForecastEntity {
    @PrimaryKey
    public Long id = 0L;
    public ArrayList<ForecastEntry> list;
    @Embedded
    public ForecastCity city;
    public Long lastUpdateTime = 0L;

    public ForecastEntity() {

    }
}
