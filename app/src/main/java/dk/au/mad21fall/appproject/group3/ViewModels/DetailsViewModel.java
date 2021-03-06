package dk.au.mad21fall.appproject.group3.ViewModels;
        import android.util.Log;

        import androidx.lifecycle.LiveData;
        import androidx.lifecycle.ViewModel;

        import java.util.ArrayList;

        import dk.au.mad21fall.appproject.group3.Models.Bar;
        import dk.au.mad21fall.appproject.group3.Models.Repository;

public class DetailsViewModel extends ViewModel {

    //A standard viewmodel pulling data from the viewmodel

    private static final String TAG = "DetailsViewModel";
    LiveData<ArrayList<Bar>> bars;

    public DetailsViewModel(){
        CreateRepository();
    }

    private Repository repository;  //repository

    public void CreateRepository(){
        repository = Repository.getInstance();  //get Repository singleton
        bars = repository.getBars();
    }

    public LiveData<ArrayList<Bar>> getBars() {
        return bars;
    }

    public Bar getBar(String name){

        return repository.getBar(name);
    }

    public void rateBar(Number Rating, String barID){
        repository.rateBar(Rating, barID);
    }





}