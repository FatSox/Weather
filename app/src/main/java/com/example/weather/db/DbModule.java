package com.example.weather.db;

import com.example.weather.api.WeatherDataApiService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DbModule {
    @Provides
    @Singleton
    static AppDatabase provideDatabase() {
        return AppDatabase.build();
    }

    @Provides
    @Singleton
    static WeatherDao provideWeatherDao(AppDatabase db) {
        return db.weatherDao();
    }

    @Provides
    @Singleton
    static ForecastDao provideForecastDao(AppDatabase db) {
        return db.forecastDao();
    }
}
