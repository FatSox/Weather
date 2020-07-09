package com.example.weather.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.weather.R;
import com.example.weather.model.entity.forecast.ForecastEntry;
import com.example.weather.viewmodel.ForecastResult;
import com.example.weather.viewmodel.WeatherDataVM;

import java.util.ArrayList;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ForecastFragment extends Fragment {
    class Adapter extends RecyclerView.Adapter<ViewHolder> {
        ArrayList<ForecastEntry> dailies;

        public Adapter() {
            dailies = new ArrayList<>();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.daily_forecast_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.setValue(dailies.get(position));
        }

        @Override
        public int getItemCount() {
            return dailies.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView dayView;
        private TextView timeView;
        private TextView tempView;
        private TextView nightTempView;
        private ImageView iconView;
        private TextView weatherTypeView;
        private TextView windView;

        private ForecastEntry daily;

        public ViewHolder(View view) {
            super(view);

            dayView = view.findViewById(R.id.dayTextView);
            timeView = view.findViewById(R.id.timeTextView);
            tempView = view.findViewById(R.id.temperatureTextView);
            iconView = view.findViewById(R.id.weatherTypeImageView);
            weatherTypeView = view.findViewById(R.id.weatherTypeTextView);
            windView = view.findViewById(R.id.windSpeedTextView);
        }

        public void setValue(ForecastEntry newDaily) {
            daily = newDaily;
            Log.d("MainLog", "dt text " + daily.dtTxt);
            dayView.setText(daily.dtTxt.split(" ")[0]);
            timeView.setText(daily.dtTxt.split(" ")[1]);
            tempView.setText(String.format("%.0f", daily.main.temp));
            String url = daily.getIconUrls().get(0);
            Glide.with(ForecastFragment.this).load(url).into(iconView);
            weatherTypeView.setText(daily.weather.get(0).description);
            windView.setText(getString(R.string.wind_speed_text, daily.wind.speed));
        }
    }
    public static final String TAG = "FORECAST_FRAGMENT_TAG";

    private WeatherDataVM viewModel;
    private Adapter adapter;
    private RecyclerView forecastView;
    private CompositeDisposable disposable;

    public ForecastFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MainLog", "forecast fragment");
        disposable = new CompositeDisposable();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forecast, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        forecastView = view.findViewById(R.id.dailyForecast);
        viewModel = new ViewModelProvider(Objects.requireNonNull(getActivity())).get(WeatherDataVM.class);
        adapter = new Adapter();
        forecastView.setAdapter(adapter);
        forecastView.setHasFixedSize(true);
        forecastView.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        forecastView.addItemDecoration(decoration);
    }

    @Override
    public void onStart() {
        super.onStart();

        Disposable d = viewModel.forecastStream
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(e -> {
                Log.d("MainLog", "got forecast data + " + e);
                if (e instanceof ForecastResult.Success) {
                    ArrayList<ForecastEntry> list = ((ForecastResult.Success) e).entity.list;
                    if (list != null) {
                        adapter.dailies = list;
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.d("MainLog", "git a null forecast list");
                    }
                } else {
                    Toast.makeText(getActivity(), "Error loading forecast data", Toast.LENGTH_LONG)
                        .show();
                }
            }, e -> {
                Log.e("MainLog", "Error", e);
            });
        disposable.add(d);
    }

    @Override
    public void onStop() {
        super.onStop();
        disposable.clear();
    }
}