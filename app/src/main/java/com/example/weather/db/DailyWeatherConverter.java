package com.example.weather.db;

import androidx.room.TypeConverter;
import com.example.weather.model.entity.DailyWeather;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class DailyWeatherConverter {
    @TypeConverter
    public ArrayList<DailyWeather> jsonToList(String json) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        Gson gson = gsonBuilder.create();
        Type type = new TypeToken<ArrayList<DailyWeather>>(){}.getType();
        return gson.fromJson(json, type);
    }

    @TypeConverter
    public String listToJson(ArrayList<DailyWeather> data) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        Gson gson = gsonBuilder.create();
        Type type = new TypeToken<ArrayList<DailyWeather>>(){}.getType();
        return gson.toJson(data, type);
    }
}
