package dk.au.mad21fall.appproject.group3;

import android.app.Application;
import android.content.Context;

public class TrackApplication extends Application {

    private static TrackApplication instance; //The one application project for the application lifetime


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static Context getAppContext(){return instance.getApplicationContext();}
}
