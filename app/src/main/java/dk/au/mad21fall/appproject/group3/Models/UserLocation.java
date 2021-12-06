package dk.au.mad21fall.appproject.group3.Models;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import dk.au.mad21fall.appproject.group3.Other.TrackApplication;

public class UserLocation {

    //A class implemented with a singleton pattern for getting live data of the current location.

    private static UserLocation instance;

    //UserLocation services
    private LocationManager locationManager;
    private android.location.Location userLocation;
    private String provider;
    private LocationListener locationListener;
    private Criteria criteria = new Criteria();
    private Context context;
    private MutableLiveData<android.location.Location> locationLiveData;


    //Singleton pattern
    public static UserLocation getInstance(){
        if(instance == null) {
            instance = new UserLocation();
        }
        return instance;
    }

    UserLocation(){
        instance = this;
        context = TrackApplication.getAppContext();
        setupLocationListener();
        locationLiveData = new MutableLiveData<android.location.Location>();
        getLocation();
    }

    //Return the live data
    public LiveData<android.location.Location> getLocationLive(){
        Log.d(TAG, "getLocationLive: " + userLocation.toString());
        return locationLiveData;
    }

    //Get the current location
    public android.location.Location getCurrentLocation(){
        return userLocation;
    }

    //Set a listener on the location and update the live data along with it.
    private void setupLocationListener() {
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull android.location.Location location) {
                userLocation = location;
                locationLiveData.setValue(location);
            }
        };
    }

    //Starts the listening on the loaction.
    @SuppressLint("MissingPermission")
    private void getLocation() {
        try {
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            //Check every 10 seconds/10000 milliseconds
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, locationListener);
            //get initial userLocation
            provider = locationManager.getBestProvider(criteria, false);
            userLocation = locationManager.getLastKnownLocation(provider);
            //Store the location
            locationLiveData.setValue(userLocation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
