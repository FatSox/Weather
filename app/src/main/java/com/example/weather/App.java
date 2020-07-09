package com.example.weather;

import android.content.Context;

import androidx.multidex.MultiDexApplication;

import com.example.weather.api.ApiComponent;
import com.example.weather.api.DaggerApiComponent;
import com.example.weather.db.DaggerDbComponent;
import com.example.weather.db.DbComponent;

public class App extends MultiDexApplication {
    private static ApiComponent apiComponent;
    private static DbComponent dbComponent;
    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();

        apiComponent = DaggerApiComponent.builder()
            .build();
        dbComponent = DaggerDbComponent.builder().build();

        appContext = getApplicationContext();
    }

    static public ApiComponent getApiComponent() {
        return apiComponent;
    }
    static public DbComponent getDbComponent() {
        return dbComponent;
    }

    static public Context getAppContext() {
        return appContext;
    }
}
