package dk.au.mad21fall.appproject.group3.Other;

import android.app.Application;
import android.content.Context;
import android.util.Log;

public class TrackApplication extends Application {

    private static TrackApplication instance; //The one application project for the application lifetime

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("BLA", "getAppContext: Is this what breaks it?! 1");
        instance = this;
    }

    public static Context getAppContext(){
        Log.d("BLA", "getAppContext: Is this what breaks it?! 2");
        return instance.getApplicationContext();}

}
