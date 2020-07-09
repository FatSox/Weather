package com.example.weather.db;

import com.example.weather.api.ApiModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApiModule.class, DbModule.class})
public interface DbComponent {
    public WeatherDataRepository repository();
}
