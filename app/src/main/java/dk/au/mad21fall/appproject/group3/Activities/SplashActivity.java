package dk.au.mad21fall.appproject.group3.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

import dk.au.mad21fall.appproject.group3.Models.Repository;
import dk.au.mad21fall.appproject.group3.R;

public class SplashActivity extends AppCompatActivity {

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
                //overridePendingTransition(R.anim.activity_change_zoom, 300);
                finish();
            }
        }, 3000); //Time for it to run


    }
}