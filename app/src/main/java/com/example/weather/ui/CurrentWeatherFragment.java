package com.example.weather.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.util.Range;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.weather.R;
import com.example.weather.model.entity.current.WeatherEntity;
import com.example.weather.viewmodel.WeatherDataVM;
import com.example.weather.viewmodel.WeatherResult;

import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CurrentWeatherFragment extends Fragment {
    public static final String TAG = "CURRENT_WEATHER_FRAGMENT_TAG";

    private WeatherDataVM viewModel;

    private TextView coordinatesTextView;
    private TextView temperatureTextView;
    private ImageView weatherTypeImageView;
    private TextView weatherTypeTextView;
    private TextView feelsLikeTemperatureTextView;
    private TextView windSpeedTextView;
    private TextView windDirectionTextView;
    private Button forecastButton;
    private Button cityButt;

    private CompositeDisposable d;

    public CurrentWeatherFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_current_weather, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        coordinatesTextView = view.findViewById(R.id.coordinatesTextView);
        temperatureTextView = view.findViewById(R.id.temperatureTextView);
        weatherTypeImageView = view.findViewById(R.id.weatherTypeImageView);
        weatherTypeTextView = view.findViewById(R.id.weatherTypeTextView);
        feelsLikeTemperatureTextView = view.findViewById(R.id.feelsLikeTemperatureTextView);
        windSpeedTextView = view.findViewById(R.id.windSpeedTextView);
        windDirectionTextView = view.findViewById(R.id.windDirectionTextView);
        forecastButton = view.findViewById(R.id.forecastButton);
        cityButt = view.findViewById(R.id.cityButt);

        forecastButton.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, new ForecastFragment(), ForecastFragment.TAG)
                .addToBackStack(null)
                .commit();
        });

        cityButt.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, new CitySelectionFragment(), CitySelectionFragment.TAG)
                .addToBackStack(null)
                .commit();
        });

        viewModel = new ViewModelProvider(Objects.requireNonNull(getActivity())).get(WeatherDataVM.class);

        d = new CompositeDisposable();
    }

    @Override
    public void onStart() {
        super.onStart();

        Disposable disposable = viewModel.dataStream
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(e -> {
                if (e instanceof WeatherResult.Success) {
                    displayWeatherData(((WeatherResult.Success) e).entity);
                } else {
                    Toast.makeText(getActivity(), "Error loading weather data", Toast.LENGTH_LONG)
                        .show();
                }
            }, e -> {
                Log.e("MainLog", "Error", e);
            });
        d.add(disposable);
    }

    @Override
    public void onStop() {
        super.onStop();
        d.clear();
    }

    @SuppressLint("SetTextI18n")
    private void displayWeatherData(WeatherEntity data) {
        coordinatesTextView.setText(getString(R.string.coordinates_text, data.name));

        String imageUrl = data.getIconUrls().get(0);
        Glide.with(this).load(imageUrl).into(weatherTypeImageView);
        temperatureTextView.setText(String.format("%.1f", data.main.temp));
        weatherTypeTextView.setText(data.weather.get(0).description);
        feelsLikeTemperatureTextView.setText(data.main.feelsLike.toString());
        windSpeedTextView.setText(getString(R.string.wind_speed_text, data.wind.speed));
        windDirectionTextView.setText(windDirectionString(data.wind.deg));
    }

    private String windDirectionString(Integer deg) {
        if (deg >= 0 && deg <= 22) {
            return getString(R.string.south_label);
        } else if (deg >= 23 && deg <= 67) {
            return getString(R.string.south_east_label);
        } else if (deg >= 68 && deg <= 112) {
            return getString(R.string.east_label);
        } else if (deg >= 113 && deg <= 157) {
            return getString(R.string.north_east_label);
        } else if (deg >= 158 && deg <= 202) {
            return getString(R.string.north_label);
        } else if (deg >= 203 && deg <= 247) {
            return getString(R.string.north_west_label);
        } else if (deg >= 248 && deg <= 292) {
            return getString(R.string.west_label);
        } else if (deg >= 293 && deg <= 337) {
            return getString(R.string.south_west_label);
        } else if (deg >= 338 && deg <= 360) {
            return getString(R.string.south_label);
        } else {
            return getString(R.string.unknown_direction_label);
        }
    }
}