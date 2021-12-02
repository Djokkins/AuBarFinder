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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

public class Repository {

    private static final String TAG = "Repository";

    FirebaseFirestore database;
    MutableLiveData<ArrayList<Bar>> bars;   //Livedata
    private static Repository instance;     //for Singleton pattern
    private FirebaseAuth mAuth;


    public static Repository getInstance() {
        if (instance == null) {
            instance = new Repository();
        }
        return instance;
    }

    private Repository() {
        mAuth = FirebaseAuth.getInstance();
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
                                bar.setBarID(snap.getId());
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

    public void rateBar(double rating, String barID){
        String uID = mAuth.getCurrentUser().getUid();
        Log.d(TAG, "rateBar: Rating = " + rating);
        Log.d(TAG, "rateBar: UserID = " + uID);
        Log.d(TAG, "rateBar: Bar ID = " + barID);

        Map<String, Object> newRating = new HashMap<>();
        newRating.put("rating", rating);

        database.collection("bars").document(barID).collection("ratings")
        .document(uID).set(newRating)
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "onSuccess: SUCCESS!!!!!!!!!!!!!!!!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: FAILLLLLLLLLLLLLLL!!!!!!!!!!!!!");
            }
        });



    }

}
