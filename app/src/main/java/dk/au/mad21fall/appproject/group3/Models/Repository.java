package dk.au.mad21fall.appproject.group3.Models;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.RequestQueue;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

public class Repository {

    private static final String TAG = "Repository";

    FirebaseFirestore database;
    MutableLiveData<ArrayList<Bar>> bars;   //Livedata
    private static Repository instance;     //for Singleton pattern


    public static Repository getInstance() {
        if (instance == null) {
            instance = new Repository();
        }
        return instance;
    }

    private Repository() {
        LoadData();
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

    public Bar getBar(String name){
        Log.d(TAG, "getBar: We get here at least?!" + name);
        Log.d(TAG, "getBar: " + bars.getValue());
        for(Bar bar : bars.getValue()){
            Log.d(TAG, "getBar: Bar = " + bar.getName());
            Log.d(TAG, "getBar: Bar = " + name);
            if(bar.getName().equals(name)){
                Log.d(TAG, "getBar: We got here!");
                return bar;
            }
        }
        return new Bar();
    }


    public LiveData<ArrayList<Bar>> getBars(){
        return bars;
    }



}
