package dk.au.mad21fall.appproject.group3.ui.map;

import android.content.Context;

import androidx.lifecycle.LiveData;

import androidx.lifecycle.ViewModel;


import java.util.ArrayList;

import dk.au.mad21fall.appproject.group3.Models.Bar;
import dk.au.mad21fall.appproject.group3.Models.Repository;

public class MapViewModel extends ViewModel {



    private static final String TAG = "MapFragmentViewModel";
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

    //Function for loading in adresses
    public ArrayList<String> getBarList(){
        ArrayList<String> locations = new ArrayList<String>();
        for (int i = 0; i < bars.getValue().size(); i++){
            locations.add(i, bars.getValue().get(i).getAddress());
        }
        return locations;
    }




}