package dk.au.mad21fall.appproject.group3.Models;

import android.content.Context;
import android.location.Location;
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

import java.text.DecimalFormat;
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
                                if(bar != null){
                                    bar.setBarID(snap.getId());
                                    setAverage(bar);
                                    bar.calcDistance(UserLocation.getInstance().getCurrentLocation());

                                    Log.d(TAG, "onEvent: BAR AVERAGE = " + bar.getAverage_Rating());
                                    updatedBars.add(bar);
                                }
                            }
                            bars.setValue(updatedBars);
                        }
                    }
                });
    }

    public void setAverage(Bar bar){

        database.collection("bars").document(bar.getBarID()).collection("ratings")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        //ArrayList<Bar> updatedBars = new ArrayList<>();
                        if(value != null && !value.isEmpty()){
                            double average = 0;
                            int counter = 0;
                            for(DocumentSnapshot snap : value.getDocuments()){
                                if(snap.getId().equals(mAuth.getUid())){
                                    bar.setUserRating(snap.getDouble("rating"));
                                }
                                average = average + snap.getDouble("rating");
                                counter++;
                                Log.d(TAG, "onEvent: RATINGS DATA = " + snap.getDouble("rating"));

                            }
                            //Round and store the number
                            average = round(average/counter, 1);

                            bar.setAverage_Rating(average);


                        } else bar.setAverage_Rating(0.0);
                    }
                });
    }

    //https://stackoverflow.com/questions/22186778/using-math-round-to-round-to-one-decimal-place
    private static double round (double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

    public Bar getBar(String name){
        for(Bar bar : bars.getValue()){
            if(bar.getName().equals(name)){
                return bar;
            }
        }
        return new Bar();
    }


    public LiveData<ArrayList<Bar>> getBars(){
        return bars;
    }

    public void rateBar(Number rating, String barID){
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
