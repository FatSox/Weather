package com.example.weather.viewmodel;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.weather.model.WeatherDataService;
import com.example.weather.model.entity.CityEntity;
import com.example.weather.model.entity.current.WeatherEntity;
import com.example.weather.model.entity.forecast.ForecastEntity;

import java.util.ArrayList;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

public class WeatherDataVM extends ViewModel {
    public WeatherDataService service;
    private Double lat;
    private Double lon;

    private final BehaviorSubject<WeatherResult> _dataStream = BehaviorSubject.create();
    public Observable<WeatherResult> dataStream;

    private final BehaviorSubject<ForecastResult> _forecastStream = BehaviorSubject.create();
    public Observable<ForecastResult> forecastStream;

    private final BehaviorSubject<CityEntity> _selectedCityStream = BehaviorSubject.create();
    public Observable<CityEntity> selectedCityStream;


    public WeatherDataVM() {
        service = new WeatherDataService();
        dataStream = _dataStream;
        forecastStream = _forecastStream;
        selectedCityStream = _selectedCityStream;
        _selectedCityStream.onNext(CityEntity.getLocationEntity());
    }

    @SuppressLint("CheckResult")
    public void setLocation(Double lat, Double lon) {
        this.lat = lat;
        this.lon = lon;

        Disposable d = service.getData(lat, lon, false)
            .subscribe(e -> {
                _dataStream.onNext(new WeatherResult.Success(e));
            }, error -> {
                _dataStream.onNext(new WeatherResult.Failure(error));
            });

        service.getForecastData(lat, lon, false)
            .subscribe(e -> {
                _forecastStream.onNext(new ForecastResult.Success(e));
            }, error -> {
                _forecastStream.onNext(new ForecastResult.Failure(error));
            });
    }

    @SuppressLint("CheckResult")
    public void setLocation(String cityName) {
        lat = 0.0;
        lon = 0.0;

        if (_selectedCityStream.getValue() != null) {
            if (!_selectedCityStream.getValue().name.equals(cityName)) {
                CityEntity ce = new CityEntity();
                ce.name = cityName;
                _selectedCityStream.onNext(ce);

                if (!cityName.equals(CityEntity.getLocationEntity().name)) {
                    Disposable d = service.getData(cityName, false)
                        .subscribe(e -> {
                            _dataStream.onNext(new WeatherResult.Success(e));
                        }, error -> {
                            _dataStream.onNext(new WeatherResult.Failure(error));
                        });

                    service.getForecastData(cityName, false)
                        .subscribe(e -> {
                            _forecastStream.onNext(new ForecastResult.Success(e));
                        }, error -> {
                            _forecastStream.onNext(new ForecastResult.Failure(error));
                        });
                }
            }
        }

    }


    @SuppressLint("CheckResult")
    public void refreshData() {
        if (lat != 0.0 && lon != 0.0) {
            service.getData(lat, lon, true)
                .subscribe(e -> {
                    _dataStream.onNext(new WeatherResult.Success(e));
                }, error -> {
                    _dataStream.onNext(new WeatherResult.Failure(error));
                });

            service.getForecastData(lat, lon, true)
                .subscribe(e -> {
                    _forecastStream.onNext(new ForecastResult.Success(e));
                }, error -> {
                    _forecastStream.onNext(new ForecastResult.Failure(error));
                });
        } else if (_selectedCityStream.getValue() != null){
            service.getData(_selectedCityStream.getValue().name, true)
                .subscribe(e -> {
                    _dataStream.onNext(new WeatherResult.Success(e));
                }, error -> {
                    _dataStream.onNext(new WeatherResult.Failure(error));
                });

            service.getForecastData(_selectedCityStream.getValue().name, true)
                .subscribe(e -> {
                    _forecastStream.onNext(new ForecastResult.Success(e));
                }, error -> {
                    _forecastStream.onNext(new ForecastResult.Failure(error));
                });
        }
    }

    public Observable<ArrayList<String>> getCitiesList() {
        ArrayList<String> cities = new ArrayList<>();
        cities.add(CityEntity.getLocationEntity().name);
        cities.add("Лондон");
        cities.add("Париж");
        cities.add("Токио");
        cities.add("Нью-Йорк");
        return Observable.just(cities);
    }
}
