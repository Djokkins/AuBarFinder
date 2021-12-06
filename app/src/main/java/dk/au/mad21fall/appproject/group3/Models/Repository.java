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

    //A standard repository pattern which handles the transfering of data between firebase and
    //the application.


    private static final String TAG = "Repository";

    FirebaseFirestore database;
    MutableLiveData<ArrayList<Bar>> bars;   //Livedata
    private static Repository instance;     //for Singleton pattern
    private FirebaseAuth mAuth;


    //Singletop pattern
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

    //Load in the data
    private void LoadData() {

        bars = new MediatorLiveData<ArrayList<Bar>>();
        if(database == null){
            database = FirebaseFirestore.getInstance();
        }

        //Standard way of loading from week 10
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
                                    //Set the distance and the average.
                                    setAverage(bar);
                                    bar.calcDistance(UserLocation.getInstance().getCurrentLocation());
                                    updatedBars.add(bar);
                                }
                            }
                            bars.setValue(updatedBars);
                        }
                    }
                });
    }

    //Take the bar as inputs and gets the sub-collection of ratings inside of it.
    public void setAverage(Bar bar){
        database.collection("bars").document(bar.getBarID()).collection("ratings")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        //ArrayList<Bar> updatedBars = new ArrayList<>();
                        if(value != null && !value.isEmpty()){
                            double average = 0;
                            int counter = 0;
                            //Add together all the raiting
                            for(DocumentSnapshot snap : value.getDocuments()){
                                if(snap.getId().equals(mAuth.getUid())){
                                    bar.setUserRating(snap.getDouble("rating"));
                                }
                                average = average + snap.getDouble("rating");
                                counter++;
                                Log.d(TAG, "onEvent: RATINGS DATA = " + snap.getDouble("rating"));

                            }
                            //Round and store the number to 1 decimal
                            average = round(average/counter, 1);

                            //Set the found average
                            bar.setAverage_Rating(average);


                        } else bar.setAverage_Rating(0.0); //If no ratings set the score to 0.0
                    }
                });
    }

    //Rounding function taken from:
    //https://stackoverflow.com/questions/22186778/using-math-round-to-round-to-one-decimal-place
    private static double round (double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

    //Returns a bar from a name
    public Bar getBar(String name){
        for(Bar bar : bars.getValue()){
            if(bar.getName().equals(name)){
                return bar;
            }
        }
        return new Bar();
    }


    //Returns the bar list as live data
    public LiveData<ArrayList<Bar>> getBars(){
        return bars;
    }

    //Takes in the bars ID and the rating of the bar
    public void rateBar(Number rating, String barID){
        String uID = mAuth.getCurrentUser().getUid();
        Log.d(TAG, "rateBar: Rating = " + rating);
        Log.d(TAG, "rateBar: UserID = " + uID);
        Log.d(TAG, "rateBar: Bar ID = " + barID);

        //Makes a new object for the specific user, only one per user allowed.
        Map<String, Object> newRating = new HashMap<>();
        newRating.put("rating", rating);

        //If such a document allready exists it overrides.
        database.collection("bars").document(barID).collection("ratings")
        .document(uID).set(newRating)
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "onSuccess: Rated successfully");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: Fail");
            }
        });

    }

}
