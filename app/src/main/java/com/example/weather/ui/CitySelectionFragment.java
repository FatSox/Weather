package com.example.weather.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.weather.R;
import com.example.weather.model.entity.CityEntity;
import com.example.weather.viewmodel.WeatherDataVM;

import java.util.ArrayList;
import java.util.Objects;

public class CitySelectionFragment extends Fragment {
    public static final String TAG = "CITY_SELECTION_FRAGMENT";

    class Adapter extends RecyclerView.Adapter<ViewHolder> {
        ArrayList<String> cities;

        public Adapter() {
            cities = new ArrayList<>();
        }

        @NonNull
        @Override
        public CitySelectionFragment.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.cities_list_item, parent, false);
            return new CitySelectionFragment.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.setValue(cities.get(position));
        }

        @Override
        public int getItemCount() {
            return cities.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameView;

        private String city;

        public ViewHolder(View view) {
            super(view);

            nameView = view.findViewById(R.id.citiesListItem);
        }

        public void setValue(String newCity) {
            city = newCity;
            nameView.setText(city);

            nameView.setOnClickListener(v -> {
                viewModel.setLocation(city);
                CitySelectionFragment.this.getActivity().getSupportFragmentManager().popBackStack();
            });
        }
    }

    private WeatherDataVM viewModel;
    private Adapter adapter;
    private RecyclerView citiesListView;

    public CitySelectionFragment() {
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
        return inflater.inflate(R.layout.fragment_city_selection, container, false);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        citiesListView = view.findViewById(R.id.citiesListView);
        viewModel = new ViewModelProvider(Objects.requireNonNull(getActivity())).get(WeatherDataVM.class);
        adapter = new Adapter();
        citiesListView.setAdapter(adapter);
        citiesListView.setHasFixedSize(true);
        citiesListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        citiesListView.addItemDecoration(decoration);

        viewModel.getCitiesList()
            .subscribe(cities -> {
                adapter.cities = cities;
                adapter.notifyDataSetChanged();
            });
    }
}