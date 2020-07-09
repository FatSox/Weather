package com.example.weather.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


import com.example.weather.model.entity.forecast.ForecastEntity;

import io.reactivex.Completable;
import io.reactivex.Maybe;

@Dao
public interface ForecastDao {
    public static String TABLE_NAME = "WeatherData";

    @Insert
    public Completable insert(ForecastEntity data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public Completable insertOrUpdate(ForecastEntity data);

    @Query("SELECT * FROM " + TABLE_NAME + " ORDER BY lastUpdateTime DESC LIMIT 1")
    Maybe<ForecastEntity> getForecastData();
}
