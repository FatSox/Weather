package com.example.weather.model.entity;

import androidx.room.Entity;

@Entity(tableName = "City")
public class CityEntity {
    public Long id = 0L;
    public String name = "";
    public boolean selected = false;

    public CityEntity() {

    }

    public static CityEntity getLocationEntity() {
        CityEntity ce = new CityEntity();
        ce.name = "Текущие координаты";

        return ce;
    }
}
