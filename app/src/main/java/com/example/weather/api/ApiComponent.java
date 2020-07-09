package com.example.weather.api;


import com.example.weather.model.WeatherDataService;
import com.example.weather.ui.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApiModule.class})
public interface ApiComponent {
    void inject(MainActivity activity);

    void inject(WeatherDataService activity);
}
