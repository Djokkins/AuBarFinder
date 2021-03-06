package dk.au.mad21fall.appproject.group3.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

import dk.au.mad21fall.appproject.group3.Models.Repository;
import dk.au.mad21fall.appproject.group3.Other.TrackApplication;
import dk.au.mad21fall.appproject.group3.R;

public class SplashActivity extends AppCompatActivity {

    //Made to load the data before showing the app. This is a simple animation that run for 3 secdons.


    Animation leftAnimation, rightAnimation, leftDropAnim, rightDropAnim;
    ImageView imgBeerLeft, imgBeerRight, imgDropsLeft, imgDropsRight;


    //https://abhiandroid.com/programming/splashscreen
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //Get the data down by creating the repository
        Repository repository;  //repository
        repository = Repository.getInstance();  //get Repository singleton
        //Instantiate the singleton track application
        TrackApplication test = new TrackApplication();

        //Check permissions so they load before the list view
        checkPermissions();

        //This could be moved into a function, but it's all there is to the activity.
        imgBeerLeft = findViewById(R.id.imgBeerLeft);
        imgBeerRight = findViewById(R.id.imgBeerRight);
        imgDropsLeft = findViewById(R.id.imgDropsLeft);
        imgDropsRight = findViewById(R.id.imgDropsRight);
        imgDropsLeft.setVisibility(View.INVISIBLE);
        imgDropsRight.setVisibility(View.INVISIBLE);
        leftAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_left);
        rightAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_right);
        leftDropAnim = AnimationUtils.loadAnimation(this, R.anim.appear_left);
        rightDropAnim = AnimationUtils.loadAnimation(this, R.anim.appear_right);


        imgBeerLeft.setAnimation(leftAnimation);
        imgBeerRight.setAnimation(rightAnimation);

        //Listen to the animation so we know when the beers are centered, and then move the drops.
        leftAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imgDropsLeft.setAnimation(leftDropAnim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        rightAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imgDropsRight.setAnimation(rightDropAnim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });



        //https://www.youtube.com/watch?v=JLIFqqnSNmg&ab_channel=CodingWithTea
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i;
                if(FirebaseAuth.getInstance().getCurrentUser() != null) {
                    i = new Intent(SplashActivity.this,MainActivity.class);
                } else i = new Intent(SplashActivity.this,LoginActivity.class);

                startActivity(i);
                finish();
            }
        }, 3000); //Time for it to run


    }

    //Check for location permission
    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }
    }

}