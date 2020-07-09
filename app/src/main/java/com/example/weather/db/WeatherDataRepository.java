package com.example.weather.db;

import android.annotation.SuppressLint;
import android.util.Log;

import com.example.weather.api.WeatherDataApiService;
import com.example.weather.model.entity.current.WeatherEntity;
import com.example.weather.model.entity.forecast.ForecastEntity;
import com.example.weather.model.entity.forecast.ForecastEntry;

import java.util.Calendar;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Maybe;

public class WeatherDataRepository {
    private static long UPDATE_TIME_INTERVAL = 1000 * 60 * 10;

    private WeatherDataApiService apiService;
    private WeatherDao dao;
    private ForecastDao forecastDao;

    @Inject
    WeatherDataRepository(WeatherDataApiService service, WeatherDao dao, ForecastDao forecastDao) {
        this.apiService = service;
        this.dao = dao;
        this.forecastDao = forecastDao;
    }

    public Flowable<WeatherEntity> getCurrentData(double lat, double lon, boolean refresh) {
        Log.d("MainLog", "get current by coords");
        return Maybe.concat(getFromDb(), getFromServer(lat, lon))
            .takeUntil(data -> {
                long currentTime = Calendar.getInstance().getTimeInMillis();
                long timeDiff = currentTime - data.lastUpdateTime;
                Log.d("MainLog", "timeDiff " + timeDiff + " for " + data.name);

                if (refresh) {
                    return false;
                } else {
                    return timeDiff < UPDATE_TIME_INTERVAL && data.coord.lat == lat && data.coord.lon == lon;  // 10 min has passed
                }
            });
    }

    public Flowable<WeatherEntity> getCurrentData(String cityName, boolean refresh) {
        Log.d("MainLog", "get current by name");
        return Maybe.concat(getFromDb(), getFromServer(cityName))
            .takeUntil(data -> {
                long currentTime = Calendar.getInstance().getTimeInMillis();
                long timeDiff = currentTime - data.lastUpdateTime;
                Log.d("MainLog", "timeDiff " + timeDiff);

                if (refresh) {
                    return false;
                } else {
                    return timeDiff < UPDATE_TIME_INTERVAL && data.name.equals(cityName);  // 10 min has passed
                }
            });
    }

    public Flowable<ForecastEntity> getForecastData(double lat, double lon, boolean refresh) {
        Log.d("MainLog", "get forecast by coords");
        return Maybe.concat(getForecastFromDb(), getForecastFromServer(lat, lon))
            .takeUntil(data -> {
                long currentTime = Calendar.getInstance().getTimeInMillis();
                long timeDiff = currentTime - data.lastUpdateTime;
                Log.d("MainLog", "timeDiff " + timeDiff);

                if (refresh) {
                    return false;
                } else {
                    return timeDiff < UPDATE_TIME_INTERVAL && data.city.coord.lat == lat && data.city.coord.lon == lon;  // 10 min has passed
                }
            });
    }

    public Flowable<ForecastEntity> getForecastData(String cityName, boolean refresh) {
        Log.d("MainLog", "get forecast by name");
        return Maybe.concat(getForecastFromDb(), getForecastFromServer(cityName))
            .takeUntil(data -> {
                long currentTime = Calendar.getInstance().getTimeInMillis();
                long timeDiff = currentTime - data.lastUpdateTime;
                Log.d("MainLog", "timeDiff " + timeDiff);

                if (refresh) {
                    return false;
                } else {
                    return timeDiff < UPDATE_TIME_INTERVAL && data.city.name.equals(cityName);  // 10 min has passed
                }
            });
    }

    private Maybe<ForecastEntity> getForecastFromDb() {
        return forecastDao.getForecastData();
    }

    private Maybe<ForecastEntity> getForecastFromServer(double lat, double lon) {
        return apiService.getForecastByLocation(lat, lon, "5351d25693c69eaece33a7ac606f35aa", "hourly", "metric", "ru")
            .doOnSuccess(this::saveForecastToDb);
    }

    private Maybe<ForecastEntity> getForecastFromServer(String cityName) {
        return apiService.getForecastByCity(cityName, "5351d25693c69eaece33a7ac606f35aa", "hourly", "metric", "ru")
            .doOnSuccess(this::saveForecastToDb);
    }

    @SuppressLint("CheckResult")
    private void saveForecastToDb(ForecastEntity fe) {
        fe.lastUpdateTime = Calendar.getInstance().getTimeInMillis();
        forecastDao.insertOrUpdate(fe)
            .subscribe(() -> {

            }, (error) -> {
                Log.e("MainLog", "Error updating data", error);
            });
    }

    private Maybe<WeatherEntity> getFromDb() {
        return dao.getWeatherData();
    }

    private Maybe<WeatherEntity> getFromServer(Double lat, Double lon) {
        return apiService.get(lat, lon, "5351d25693c69eaece33a7ac606f35aa", "metric", "ru")
            .doOnSuccess(this::saveToDb);

    }

    private Maybe<WeatherEntity> getFromServer(String cityName) {
        return apiService.getByCityName(cityName, "5351d25693c69eaece33a7ac606f35aa", "metric", "ru")
            .doOnSuccess(this::saveToDb);

    }

    @SuppressLint("CheckResult")
    private void saveToDb(WeatherEntity e) {
        Log.d("MainLog", "saveToDb call()");
        e.lastUpdateTime = Calendar.getInstance().getTimeInMillis();
//        dao.deleteAll()
//            .
        dao.insertOrUpdate(e)
            .subscribe(() -> {

            }, (error) -> {
                Log.e("MainLog", "Error updating data", error);
            });
    }
}
