package com.example.weather.db;

import androidx.room.TypeConverter;

import com.example.weather.model.entity.WeatherApiDescription;
import com.example.weather.model.entity.WeatherApiEntity;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class WeatherTypeConverter {
    @TypeConverter
    public ArrayList<WeatherApiDescription> jsonToList(String json) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        Gson gson = gsonBuilder.create();
        Type type = new TypeToken<ArrayList<WeatherApiDescription>>(){}.getType();
        return gson.fromJson(json, type);
    }

    @TypeConverter
    public String listToJson(ArrayList<WeatherApiDescription> data) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        Gson gson = gsonBuilder.create();
        Type type = new TypeToken<ArrayList<WeatherApiDescription>>(){}.getType();
        return gson.toJson(data, type);
    }
}
