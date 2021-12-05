package dk.au.mad21fall.appproject.group3.Models;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import dk.au.mad21fall.appproject.group3.Other.TrackApplication;

public class UserLocation {

    private static UserLocation instance;
    //UserLocation services
    private LocationManager locationManager;
    private android.location.Location userLocation;
    private String provider;
    private LocationListener locationListener;
    private Criteria criteria = new Criteria();
    private Context context;


    public static UserLocation getInstance(){
        Log.d(TAG, "getInstance: Checkpoint 1");
        if(instance == null) {
            Log.d(TAG, "getInstance: Checkpoint 2");
            instance = new UserLocation();
        }
        return instance;
    }

    UserLocation(){
        Log.d(TAG, "getInstance: Checkpoint 3");
        instance = this;
        context = TrackApplication.getAppContext();
        Log.d(TAG, "getInstance: Checkpoint 4");
        setupLocationListener();
        Log.d(TAG, "getInstance: Checkpoint 5");
        getLocation();
        Log.d(TAG, "getInstance: Checkpoint 6");
    }

    public android.location.Location getCurrentLocation(){
        return userLocation;
    }

    private void setupLocationListener() {
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull android.location.Location location) {
                userLocation = location;

            }
        };
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);

        try {
            //get initial userLocation
            provider = locationManager.getBestProvider(criteria, false);
            userLocation = locationManager.getLastKnownLocation(provider);
            Log.d(TAG, "onLocationChanged: LOCATIOM = " + userLocation);

        } catch (Exception e) {
            Log.d(TAG, "getLocation: FUCK ME THIS DOENST WORK");
            e.printStackTrace();
        }
    }

}
