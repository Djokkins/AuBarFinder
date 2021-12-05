package dk.au.mad21fall.appproject.group3.ui.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dk.au.mad21fall.appproject.group3.Models.Bar;
import dk.au.mad21fall.appproject.group3.R;
import dk.au.mad21fall.appproject.group3.databinding.FragmentMapBinding;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "2";

    private LiveData<ArrayList<Bar>> bars;

    //UI
    private MapViewModel mapViewModel;
    private FragmentMapBinding binding;

    //Variables for implementation of maps
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;


    //Variables for location based services
    private LocationManager locationManager;
    private Location userLocation;
    private Marker marker;
    private Geocoder mGeocoder;
    private Criteria criteria = new Criteria();
    private String provider;
    private LocationListener locationListener;


    //Convert address to latitude/longitude to so we can add markers
    private ArrayList<String> locations;
    private MarkerOptions markerOptions = new MarkerOptions();
    //only load map pins onto map once, so we toggle it after use
    private boolean initMapPins = true;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mapViewModel = new ViewModelProvider(this).get(MapViewModel.class);
        binding = FragmentMapBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mGeocoder = new Geocoder(this.getContext());
        bars = mapViewModel.getBars();
        locations = mapViewModel.getBarList();

        setupLocationListener();
        //initialize map
        getLocation();
        initMap();

        return root;
    }

    private void setupLocationListener() {
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                userLocation = location;
                if(userLocation!=null){ setCurrentLocationOnMap(userLocation);}
            }
        };
    }

    private void setCurrentLocationOnMap(Location location) {
        if ((location != null) && this.getActivity() != null) {
            if (marker != null) {
                marker.remove();
            }
            LatLng user = new LatLng(location.getLatitude(), location.getLongitude());
            Log.d(TAG, "onMapReady: We are still running");
            marker = mMap.addMarker(new MarkerOptions()
                    .position(user)
                    .title("My location")
                    .icon(BitmapDescriptorFactory.fromBitmap(getBitmap(R.drawable.my_location))));

        }

    }


    //Initialize map asynchronously
    private void initMap() {
        if (mapFragment == null) {
            mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);
            mapFragment.getMapAsync(this);
        }
    }

    //function that gets the users location,
    @SuppressLint("MissingPermission")
    private void getLocation() {
        try {
            locationManager = (LocationManager) getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);

            //get initial userLocation
            provider = locationManager.getBestProvider(criteria, false);
            userLocation = locationManager.getLastKnownLocation(provider);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        if (mMap == null) {
            mMap = googleMap;
        }
        getLocation();

        setupBarMarkers();
    }

    private void setupBarMarkers() {
        //Todo: Make the moving of user marker a smooth animation.
        /// inspiration from https://stackoverflow.com/questions/13728041/move-markers-in-google-map-v2-android
        if (initMapPins) {
            //move camera to aarhus as default, set zoom level to be appropriate
            if (userLocation == null) {
                LatLng aarhus = new LatLng(56.16, 10.20);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(aarhus, 14));
                //TODO: try adding userLocation here :)
            }
            //creation of user marker and goes to where the user is.
            else {
                LatLng user = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
                markerOptions
                        .position(new LatLng(user.latitude, user.longitude))
                        .title("@string/mapLocationDisplay")
                        .icon(BitmapDescriptorFactory.fromBitmap(getBitmap(R.drawable.my_location)));
                marker = mMap.addMarker(markerOptions);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(user, 14));
            }

            Log.d(TAG, "onMapReady: " + locations.get(2) + "location size: " + locations.size());
            Log.d(TAG, "onMapReady: " + Geocoder.isPresent());

            //translating addresses from human readable to coordinates and marks each bar on the map
            for (int i = 0; i < locations.size(); i++) {
                try {
                    List<Address> addresses = Collections.unmodifiableList(mGeocoder.getFromLocationName(locations.get(i), 1));
                    Address address = addresses.get(0);
                    Log.d(TAG, "onMapReady: " + address.toString());

                    markerOptions
                            .position(new LatLng(address.getLatitude(), address.getLongitude()))
                            .title(bars.getValue().get(i).getName())
                            .icon(BitmapDescriptorFactory.defaultMarker());

                    mMap.addMarker(markerOptions);
                } catch (IOException e) {
                    Log.d(TAG, "There was an error trying to convert that address");
                    e.printStackTrace();
                }
            }
            initMapPins = false;
        }
    }


    //Helper function for bitmap from:
    //https://stackoverflow.com/questions/10111073/how-to-get-a-bitmap-from-a-drawable-defined-in-a-xml
    private Bitmap getBitmap(int drawableRes) {
        Drawable drawable = ActivityCompat.getDrawable(this.getContext(), drawableRes);
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}


