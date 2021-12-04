package dk.au.mad21fall.appproject.group3.ui.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.LayerDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import dk.au.mad21fall.appproject.group3.Models.Bar;
import dk.au.mad21fall.appproject.group3.Models.Constants;
import dk.au.mad21fall.appproject.group3.R;
import dk.au.mad21fall.appproject.group3.databinding.FragmentMapBinding;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "2";
    private Context context;
    private LiveData<ArrayList<Bar>> bars;
    private Geocoder mGeocoder;

    //Variables for implementation of maps
    private MapViewModel mapViewModel;
    private FragmentMapBinding binding;
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;


    //Variables for location
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private Location lastLocation = null;


    private Bitmap markerIconSick;
    private Bitmap markerIconHealthy;
    private Bitmap markerIconToiletPaper;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mapViewModel = new ViewModelProvider(this).get(MapViewModel.class);
        binding = FragmentMapBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        bars = mapViewModel.getBars();

        //initialize map
        initMap();
        mGeocoder = new Geocoder(this.getContext());

        //checking if we have user permission to track location in app, if not,
        //checkPermissions();


        //init location tracking with Fused Location Provider
        //initLocationFramework();


       return root;
    }

    private void initMap() {
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(this);

        //load some custom icon markers using helper method
       // markerIconSick = makeMarkerIcon(R.drawable.circle_red, 100, 80);
        //markerIconHealthy = makeMarkerIcon(R.drawable.circle_green, 100, 80);
        //markerIconToiletPaper = makeMarkerIcon(R.drawable.circle_red,100, 80);

    }

/*    //set up FusedLocationProviderClient and the LocationCallback for handling location updates
    private void initLocationFramework(){
        //setup location tracking
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.getActivity());

        // setup location callback listening for new location results and location availability changes
        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    Log.d(TAG, "onLocationResult: NULL");   //we did not get a valid location
                    return;
                }
                for (Location location : locationResult.getLocations()) {  //usually there will only be one location here
                    Log.d(TAG, "onLocationResult: [" + location.getLatitude() + " ; " + location.getLongitude() + "]");
                    addMarker(new LatLng(location.getLatitude(), location.getLongitude())); //got location, add marker on map
                    lastLocation = location; //update as lastLocation
                }
            }
            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
                Log.d(TAG, "onLocationAvailability: " + locationAvailability.isLocationAvailable());
            }
        };
    }

    @SuppressLint("MissingPermission") //we check this at Activity start
    private void startTrackingLocation(){
        //create LocationRequest with our basic requirements for update interval and accuracy
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10*1000);
        locationRequest.setFastestInterval(5*1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        //create LocationSettingsRequest to check if the device settings are correctly set to
        //use location request, e.g.: location is enabled, wifi and/or mobile data is connected
        //Uses the SettingsClient API
        //see: https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        SettingsClient client = LocationServices.getSettingsClient(this.getActivity());
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        *//*
        task.addOnCompleteListener(this, new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

            }
        });
        *//*

        task.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                Log.d(TAG, "onComplete: yay, our location settings are good");
                LocationSettingsStates states = locationSettingsResponse.getLocationSettingsStates();
                ShowAvailableStates(states);
            }
        });

        task.addOnFailureListener(this.getActivity(), new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: ups, locations settings not good");
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(getActivity(),
                                Constants.REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });

        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper());
    }
    //stop tracking
    private void stopTrackingLocation(){
        if(fusedLocationClient!=null) {
            fusedLocationClient.removeLocationUpdates(locationCallback); //remove the location updates
        }
    }

    //method for handling location opdates in our activity
    private void handleLocationUpdate(Location location){
        lastLocation = location;
        if(mMap != null) {
            final LatLng locLatLong = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locLatLong, 13));
        }
    }

    //method for checking if a location already exist
    @SuppressLint("MissingPermission") //we check this at Activity start
    private void getLastLocationIfAny(){
        //call get last location and add success listener to react if an existing location is already known
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this.getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            String toast = "Got last known location,it is " + ((System.currentTimeMillis() - location.getTime()) / 1000) + "seconds old";
                            Toast.makeText(getActivity(), toast, Toast.LENGTH_LONG).show();
                            handleLocationUpdate(location);
                        }
                    }
                });
    }*/

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        mMap = googleMap;

        //move camera to position
        //map.moveCamera(CameraUpdateFactory.newLatLng());
        LatLng aarhus = new LatLng(56.16, 10.20);
        //mMap.addMarker(new MarkerOptions().position(aarhus).title("Marker in aarhus"));

        //Convert address to latitude/longitude to so we can add markers
        List<Address> addresses = new ArrayList<Address>();
        ArrayList<String> locations = mapViewModel.getBarList();
        Log.d(TAG, "onMapReady: " + locations.get(2) + "location size: " + locations.size());
        Log.d(TAG, "onMapReady: " + Geocoder.isPresent());

       for(int i = 0; i < locations.size(); i++){
            try {
                addresses = mGeocoder.getFromLocationName(locations.get(i), 1);
                Address address = addresses.get(0);
                Log.d(TAG, "onMapReady: " + address.toString());

                MarkerOptions markerOptions = new MarkerOptions()
                        .position(new LatLng(address.getLatitude(), address.getLongitude()))
                        .title(bars.getValue().get(i).getName());
                mMap.addMarker(markerOptions);
            } catch (IOException e) {
                Log.d(TAG, "There was an error trying to convert that address");
                e.printStackTrace();
            }
        }

        //move camera to aarhus as default, set zoom level to be appropriate
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(aarhus,14));

}

    //adding a new marker for the given position
    private void addMarker(LatLng latLng){
        if(mMap != null) {
            mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("")  //markers are numbered
                    .icon(BitmapDescriptorFactory.fromBitmap(true ? markerIconSick : markerIconHealthy))  //set marker icon based on infected state
            );
            //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }

    private void addMarkerClicked(LatLng latLng){
        if(mMap != null) {
            mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.fromBitmap(markerIconToiletPaper)) //set custom marker icon
            );
        }
    }

    /////////// HELPER METHODS

    //helper method for generating map icon bitmaps
    private Bitmap makeMarkerIcon(int drawableId, int height, int width){


        final BitmapDrawable bitmapDrawable = (BitmapDrawable)ContextCompat.getDrawable(this.getContext(), drawableId);
        final Bitmap bitmap = bitmapDrawable.getBitmap();
        final Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
        return scaledBitmap;
    }

/*    //helper method for printing out the status of LocationSettings, i.e. which information sources are available
    private void ShowAvailableStates(LocationSettingsStates states){
        String out = "Available location sources: \n" +
                "<TYPE> :         <PRESENT> - <USABLE> \n" +
                "Network:       " + states.isNetworkLocationPresent() + " " + states.isNetworkLocationUsable() + "\n" +
                "GPS:           " + states.isGpsPresent() + " " + states.isGpsUsable() + "\n" +
                "Location:      " + states.isLocationPresent() + " " + states.isNetworkLocationUsable() + "\n" +
                "Bluetooth LE : " + states.isBlePresent() + " " + states.isBleUsable();
        Log.d(TAG, "ShowAvailableStates: \n" + out);
        Toast.makeText(this.getActivity(), out, Toast.LENGTH_LONG).show();
    }*/

    /////////// HANDLING CALLBACKS FOR PERMISSIONS AND LOCATION SETTINGS DIALOGUES

    //Modified from: https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
/*    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case Constants.REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made
                        ShowAvailableStates(states);
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        Toast.makeText(this.getActivity(), "User did not allow location settings", Toast.LENGTH_SHORT).show();
                        break;
                }
                break;
        }
    }*/

    private void checkPermissions(){
        if (ContextCompat.checkSelfPermission(this.getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // No explanation needed, we can request the permission.

            ActivityCompat.requestPermissions(this.getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    Constants.PERMISSIONS_REQUEST_LOCATION);
        }
    }

/*
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Constants.PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // got permission
                } else {
                    // permission denied
                    //in this case we just close the app
                    Toast.makeText(this.getActivity(), "You need to enable permission for Location to find your location on the map", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
*/
}

