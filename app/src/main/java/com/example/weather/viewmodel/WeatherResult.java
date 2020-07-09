package com.example.weather.viewmodel;

import com.example.weather.model.entity.current.WeatherEntity;

public class WeatherResult {
    public static class Success extends WeatherResult {
        public WeatherEntity entity;

        public Success(WeatherEntity e) {
            entity = e;
        }
    }
    public static class Failure extends WeatherResult {
        public Throwable error;

        public Failure(Throwable e) {
            error = e;
        }
    }
}
