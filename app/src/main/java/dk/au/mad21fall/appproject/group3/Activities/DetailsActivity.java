package dk.au.mad21fall.appproject.group3.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import dk.au.mad21fall.appproject.group3.Models.Bar;
import dk.au.mad21fall.appproject.group3.Models.Constants;
import dk.au.mad21fall.appproject.group3.Models.UserLocation;
import dk.au.mad21fall.appproject.group3.R;
import dk.au.mad21fall.appproject.group3.ViewModels.DetailsViewModel;

public class DetailsActivity extends AppCompatActivity {
    private static final String TAG = "DetailsViewModel";

    //Variables
    Number Score;
    int scoreInt;

    //Widgets
    TextView txtName, txtOpening, txtAddress, txtDescription, txtMyRating, txtDistance;
    Button btnFacebook, btnInstagram;
    ImageView imgIcon;
    SeekBar skbRating;

    //Other
    Bar bar;
    Location userLocation;
    private DetailsViewModel detailsViewModel;
    private LocationListener locationListener;
    private LocationManager locationManager;
    private Criteria criteria = new Criteria();
    private String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        //Get data for the specific bar
        Intent data = getIntent();
        String barName = data.getStringExtra(Constants.BAR_NAME);
        Log.d(TAG, "onCreate: The name is " + barName);
        detailsViewModel = new ViewModelProvider(this).get(DetailsViewModel.class);
        bar = detailsViewModel.getBar(barName);

        //Set an observer on the current location which updates every ~10 seconds
        UserLocation.getInstance().getLocationLive().observe(this, new Observer<Location>() {
            @Override
            public void onChanged(Location location) {
                //Update location and distance to the bar -> change the UI
                userLocation = location;
                bar.calcDistance(userLocation);
                updateUI();
            }
        });



        setupView();
        updateUI();
    }

    private void setupView() {
        //Get the current rating and the int version
        Score = bar.getUserRating();
        if(Score == null) Score = 0.0;
        scoreInt = Score.intValue() * 10;

        //Setup widgets
        txtName = findViewById(R.id.txtNameDetails);
        txtOpening = findViewById(R.id.txtOpening);
        txtDescription = findViewById(R.id.txtDescription);
        txtAddress = findViewById(R.id.txtAddress);
        txtMyRating = findViewById(R.id.txtMyRating);
        txtDistance = findViewById(R.id.txtDistance);

        Log.d(TAG, "setupView: Score = " + Score + " and scoreInt = " + scoreInt);
        txtMyRating.setText(getString(R.string.txtMyRating, Score.toString()));
        skbRating = findViewById(R.id.skbRating);

        //If clicked go to the Facebook site
        btnFacebook = findViewById(R.id.btnFacebook);
        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoUrl(bar.getFacebook());
            }
        });

        //If clicked go to the Instagram site
        btnInstagram = findViewById(R.id.btnInstagram);
        btnInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoUrl(bar.getInstagram());
            }
        });
        imgIcon = findViewById(R.id.imgLogoDetails);

        //Set the progress and setup the seekbar
        skbRating.setProgress(scoreInt);
        skbRating.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                //When the score changes change the UI.
                Score = (i / 10.0);
                updateScoreUI(Score);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //When released publish the rating to Firebase
                detailsViewModel.rateBar(Score, bar.getBarID());
            }
        });

    }

    //For going to the Facebook and Instagram websites.
    private void gotoUrl(String url) {
        //https://stackoverflow.com/questions/2762861/android-goto-http-url-on-button-click
        //If they dont exist.
        if(url.equals("N/A") || url.equals("")){
            Toast.makeText(getApplicationContext(), R.string.urlErrorToast, Toast.LENGTH_SHORT).show();
            return;
        }
        //Go to the website
        Intent browse = new Intent( Intent.ACTION_VIEW , Uri.parse(url) );
        startActivity( browse );
    }

    //Update the score only
    private void updateScoreUI(Number score){
        txtMyRating.setText(getString(R.string.txtMyRating, Score.toString()));
    }

    //Update every UI element.
    private void updateUI() {
        txtName.setText(bar.getName());
        txtOpening.setText(getString(R.string.txtOpenHours, bar.getOpen(), bar.getClose()));
        txtDistance.setText("" + bar.getDistance() + " m");
        txtDescription.setText(getString(R.string.txtDescriptionMainText, bar.getDescription()));
        txtDescription.setMovementMethod(new ScrollingMovementMethod());
        txtAddress.setText(bar.getAddress());


        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference dateRef = storageRef.child("/" + bar.getName() + ".png");
        dateRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
        {
            @Override
            public void onSuccess(Uri downloadUrl)
            {
                Log.d(TAG, "onSuccess: We have success!" + downloadUrl);
                Glide.with(imgIcon.getContext()).load(downloadUrl).into(imgIcon);}
        });
    }

    //Go to google maps which navigates to the address of the bar
    public void Navigate(View view) {
        Uri gmmIntentUri = Uri.parse("geo:0,0?q="+bar.getAddress());
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

}