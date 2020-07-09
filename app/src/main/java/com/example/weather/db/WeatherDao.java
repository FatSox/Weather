package com.example.weather.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.weather.model.entity.current.WeatherEntity;

import io.reactivex.Completable;
import io.reactivex.Maybe;

@Dao
public interface WeatherDao {
    public static String TABLE_NAME = "WeatherData";

    @Insert
    public Completable insert(WeatherEntity data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public Completable insertOrUpdate(WeatherEntity data);

    @Query("DELETE FROM " + TABLE_NAME)
    public Completable deleteAll();

    @Query("SELECT * FROM " + TABLE_NAME + " ORDER BY lastUpdateTime DESC LIMIT 1")
    Maybe<WeatherEntity> getWeatherData();
}
