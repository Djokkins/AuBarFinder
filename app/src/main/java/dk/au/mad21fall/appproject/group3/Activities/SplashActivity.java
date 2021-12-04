package dk.au.mad21fall.appproject.group3.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import dk.au.mad21fall.appproject.group3.Models.Repository;
import dk.au.mad21fall.appproject.group3.R;

public class SplashActivity extends AppCompatActivity {

    //https://abhiandroid.com/programming/splashscreen
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //Get the data down by creating the repository
        Repository repository;  //repository
        repository = Repository.getInstance();  //get Repository singleton

        Thread welcomeThread = new Thread() {
            @Override
            public void run() {
                try {
                    super.run();
                    sleep(2000);  //Delay of 10 seconds
                } catch (Exception e) {

                } finally {
                    Intent i = new Intent(SplashActivity.this,
                            LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        };
        welcomeThread.start();




    }
}