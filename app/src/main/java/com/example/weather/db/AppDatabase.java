package com.example.weather.db;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.weather.App;
import com.example.weather.model.entity.WeatherApiEntity;
import com.example.weather.model.entity.current.WeatherEntity;
import com.example.weather.model.entity.forecast.ForecastEntity;

@Database(
    entities = {WeatherEntity.class, ForecastEntity.class},
    version = 1
)
@TypeConverters(
    value = {DailyWeatherConverter.class, WeatherTypeConverter.class, ForecastEntryConverter.class}
)
public abstract class AppDatabase extends RoomDatabase {
    public abstract WeatherDao weatherDao();
    public abstract ForecastDao forecastDao();

    public static AppDatabase build() {
        return Room
            .databaseBuilder(App.getAppContext(), AppDatabase.class, "data")
            .build();
    }
}
