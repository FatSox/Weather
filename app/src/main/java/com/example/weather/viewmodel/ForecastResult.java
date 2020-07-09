package com.example.weather.viewmodel;

import com.example.weather.model.entity.forecast.ForecastEntity;

public class ForecastResult {
    public static class Success extends ForecastResult {
        public ForecastEntity entity;

        public Success(ForecastEntity e) {
            entity = e;
        }
    }
    public static class Failure extends ForecastResult {
        public Throwable error;

        public Failure(Throwable e) {
            error = e;
        }
    }
}
