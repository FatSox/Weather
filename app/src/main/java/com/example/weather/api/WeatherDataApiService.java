package com.example.weather.api;


import com.example.weather.model.entity.current.WeatherEntity;
import com.example.weather.model.entity.forecast.ForecastEntity;

import io.reactivex.Maybe;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherDataApiService {
    @GET("/data/2.5/weather")
    Maybe<WeatherEntity> get(
            @Query("lat") double lat,
            @Query("lon")double lon,
            @Query("appId") String appId,
            @Query("units") String units,
            @Query("lang") String lang
    );

    @GET("/data/2.5/weather")
    Maybe<WeatherEntity> getByCityName(
        @Query("q") String q,
        @Query("appId") String appId,
        @Query("units") String units,
        @Query("lang") String lang
    );

    @GET("/data/2.5//forecast")
    Maybe<ForecastEntity> getForecastByLocation(
        @Query("lat") double lat,
        @Query("lon")double lon,
        @Query("appId") String appId,
        @Query("exclude") String exclude,
        @Query("units") String units,
        @Query("lang") String lang
    );

    @GET("/data/2.5//forecast")
    Maybe<ForecastEntity> getForecastByCity(
        @Query("q") String q,
        @Query("appId") String appId,
        @Query("exclude") String exclude,
        @Query("units") String units,
        @Query("lang") String lang
    );
}
