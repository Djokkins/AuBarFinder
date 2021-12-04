package dk.au.mad21fall.appproject.group3.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import dk.au.mad21fall.appproject.group3.Models.Bar;
import dk.au.mad21fall.appproject.group3.Models.Constants;
import dk.au.mad21fall.appproject.group3.R;
import dk.au.mad21fall.appproject.group3.ViewModels.DetailsViewModel;

public class DetailsActivity extends AppCompatActivity {
    private static final String TAG = "DetailsViewModel";

    TextView txtName, txtOpening, txtAddress, txtDescription, txtMyRating;
    Button btnFacebook, btnInstagram;
    ImageView imgIcon;
    SeekBar skbRating;
    private DetailsViewModel detailsViewModel;
    Bar bar;
    Number Score;
    int scoreInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent data = getIntent();
        String barName = data.getStringExtra(Constants.BAR_NAME);
        Log.d(TAG, "onCreate: The name is " + barName);
        detailsViewModel = new ViewModelProvider(this).get(DetailsViewModel.class);
        bar = detailsViewModel.getBar(barName);

        Score = bar.getUserRating();
        if(Score == null) Score = 0.0;
        scoreInt = Score.intValue() * 10;

        setupView();
        updateUI();
    }

    private void setupView() {
        txtName = findViewById(R.id.txtNameDetails);
        txtOpening = findViewById(R.id.txtOpening);
        txtDescription = findViewById(R.id.txtDescription);
        txtAddress = findViewById(R.id.txtAddress);
        txtMyRating = findViewById(R.id.txtMyRating);

        Log.d(TAG, "setupView: Score = " + Score + " and scoreInt = " + scoreInt);
        txtMyRating.setText(getString(R.string.txtMyRating) + Score);
        skbRating = findViewById(R.id.skbRating);
        btnFacebook = findViewById(R.id.btnFacebook);
        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoUrl(bar.getFacebook());
            }
        });
        btnInstagram = findViewById(R.id.btnInstagram);
        btnInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoUrl(bar.getInstagram());
            }
        });
        imgIcon = findViewById(R.id.imgLogoDetails);

        skbRating.setProgress(scoreInt);
        skbRating.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Score = (i / 10.0);
                updateScoreUI(Score);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                detailsViewModel.rateBar(Score, bar.getBarID());
            }
        });

    }

    private void gotoUrl(String url) {
        //https://stackoverflow.com/questions/2762861/android-goto-http-url-on-button-click
        if(url.equals("N/A") || url.equals("")){
            Toast.makeText(getApplicationContext(), "That sadly doesn't exists.", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent browse = new Intent( Intent.ACTION_VIEW , Uri.parse(url) );
        startActivity( browse );
    }

    private void updateScoreUI(Number score){
        txtMyRating.setText(getString(R.string.txtMyRating) + " " + score);
    }

    private void updateUI() {
        txtName.setText(bar.getName());
        txtOpening.setText(bar.getOpen() + " - " + bar.getClose());
        txtDescription.setText('"' + bar.getDescription() + '"');
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
}