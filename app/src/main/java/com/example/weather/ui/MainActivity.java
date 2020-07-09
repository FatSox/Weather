package com.example.weather.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.weather.R;
import com.example.weather.model.entity.CityEntity;
import com.example.weather.viewmodel.WeatherDataVM;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import io.reactivex.disposables.Disposable;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.google.android.gms.location.LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY;

public class MainActivity extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_REQUEST = 1000001;

    private WeatherDataVM viewModel;

    private boolean locationRequested = false;
    private final LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);

            if (locationResult != null) {
                Log.d("MainLog", "location result");
                Location loc = locationResult.getLastLocation();
                viewModel.setLocation(loc.getLatitude(), loc.getLongitude());
            }
        }
    };

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this).get(WeatherDataVM.class);

        viewModel.selectedCityStream
            .subscribe(ce -> {
                Log.d("MainLog", "ce name " + ce.name);
                if (ce.name.equals(CityEntity.getLocationEntity().name)) {
                    Log.d("MainLog", "found a location city");
                    if (hasGoogleServices()) {
                        getLocationData();
                    } else {
                        Toast.makeText(this, "No Google services installed", Toast.LENGTH_LONG)
                            .show();
                    }
                } else {
                    viewModel.setLocation(ce.name);
                }
            });

        setSupportActionBar(findViewById(R.id.toolbar_main));

        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.fragmentContainer, new CurrentWeatherFragment(), CurrentWeatherFragment.TAG)
            .commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationRequested) {
            FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
            client.removeLocationUpdates(locationCallback);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.sync) {
            viewModel.refreshData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getLocationData() {
        if (Build.VERSION.SDK_INT > 22) {
            if (!hasLocationPermissions()) {
                String[] perm = {ACCESS_COARSE_LOCATION};
                requestPermissions(perm, LOCATION_PERMISSION_REQUEST);
            } else {
                requestLocationUpdates();
            }
        } else {
            requestLocationUpdates();
        }
    }

    private boolean hasGoogleServices() {
        int isAvailable = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        return isAvailable == ConnectionResult.SUCCESS;
    }

    private boolean hasLocationPermissions() {
        return ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION)
            == PERMISSION_GRANTED;
    }

    @SuppressLint("MissingPermission")
    private void requestLocationUpdates() {
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
        LocationRequest request = new LocationRequest();
        request.setPriority(PRIORITY_BALANCED_POWER_ACCURACY);
        request.setFastestInterval(1000 * 60);         // 1 minute
        request.setInterval(1000 * 60 * 10);           // 10 minutes

        client.requestLocationUpdates(request, locationCallback, null);
        locationRequested = true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.length != 0 && (grantResults[0] == PERMISSION_GRANTED)) {
                Toast.makeText(this, "Location permission granted", Toast.LENGTH_LONG)
                    .show();

                requestLocationUpdates();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_LONG)
                    .show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}