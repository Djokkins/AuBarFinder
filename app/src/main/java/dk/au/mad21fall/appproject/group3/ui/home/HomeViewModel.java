package dk.au.mad21fall.appproject.group3.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import dk.au.mad21fall.appproject.group3.Models.Bar;
import dk.au.mad21fall.appproject.group3.Models.Repository;

public class HomeViewModel extends ViewModel {


    private static final String TAG = "HomefragmentViewModel";
    LiveData<ArrayList<Bar>> bars;

    public HomeViewModel(){
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