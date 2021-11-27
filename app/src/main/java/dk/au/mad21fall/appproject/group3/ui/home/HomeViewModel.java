package dk.au.mad21fall.appproject.group3.ui.home;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import dk.au.mad21fall.appproject.group3.Bar;

public class HomeViewModel extends ViewModel {


    MutableLiveData<ArrayList<Bar>> bars;
    FirebaseFirestore database;

    /*
    private MutableLiveData<String> mText;
    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }
    public LiveData<String> getText() {
        return mText;
    }
    */

    public LiveData<ArrayList<Bar>> getBars() {
        if(bars == null){
            LoadData();
        }

        return bars;
    }

    private void LoadData() {
        bars = new MediatorLiveData<ArrayList<Bar>>();

        if(database == null){
            database = FirebaseFirestore.getInstance();
        }
        database.collection("bars")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        ArrayList<Bar> updatedBars = new ArrayList<>();
                        if(value != null && !value.isEmpty()){
                            for(DocumentSnapshot snap : value.getDocuments()){
                                Bar bar = snap.toObject(Bar.class);
                                if(bar != null){
                                    updatedBars.add(bar);
                                }
                            }
                            bars.setValue(updatedBars);
                        }
                    }
                });
    }



}