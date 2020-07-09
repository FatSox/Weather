package com.example.weather.model;

import android.util.Log;

import com.example.weather.App;
import com.example.weather.db.WeatherDataRepository;
import com.example.weather.model.entity.current.WeatherEntity;
import com.example.weather.model.entity.forecast.ForecastEntity;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class WeatherDataService {
    private WeatherDataRepository repository;

    public WeatherDataService() {
        repository = App.getDbComponent().repository();
    }

    public Flowable<WeatherEntity> getData(double lat, double lon, boolean refresh) {
        return repository.getCurrentData(lat, lon, refresh)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<WeatherEntity> getData(String cityName, boolean refresh) {
        return repository.getCurrentData(cityName, refresh)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<ForecastEntity> getForecastData(double lat, double lon, boolean refresh) {
        Log.d("MainLog", "getForecastData");
        return repository.getForecastData(lat, lon, refresh)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<ForecastEntity> getForecastData(String cityName, boolean refresh) {
        Log.d("MainLog", "getForecastData");
        return repository.getForecastData(cityName, refresh)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());
    }
}