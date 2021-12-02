package dk.au.mad21fall.appproject.group3.ui.map;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;

import dk.au.mad21fall.appproject.group3.Models.Bar;
import dk.au.mad21fall.appproject.group3.Models.Repository;
import dk.au.mad21fall.appproject.group3.R;

public class MapViewModel extends ViewModel {

    private static final String TAG = "HomefragmentViewModel";
    LiveData<ArrayList<Bar>> bars;

    public MapViewModel(){
        CreateRepository();
    }

    private Repository repository;  //repository

    private void CreateRepository(){
        repository = Repository.getInstance();  //get Repository singleton
        bars = repository.getBars();
    }

    public LiveData<ArrayList<Bar>> getBars() {
        return bars;
    }
}