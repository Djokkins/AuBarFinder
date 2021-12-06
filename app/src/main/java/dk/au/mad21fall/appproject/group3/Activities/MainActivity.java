package dk.au.mad21fall.appproject.group3.Activities;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.Calendar;

import dk.au.mad21fall.appproject.group3.Models.Constants;
import dk.au.mad21fall.appproject.group3.Other.Notification_receiver;
import dk.au.mad21fall.appproject.group3.R;
import dk.au.mad21fall.appproject.group3.databinding.ActivityMainBinding;
import dk.au.mad21fall.appproject.group3.ui.home.HomeFragment;
import dk.au.mad21fall.appproject.group3.ui.home.HomeViewModel;

public class MainActivity extends AppCompatActivity {

    //Template stuff for fragments
    private ActivityMainBinding binding;
    FirebaseAuth auth;
    HomeViewModel homeVM;
    HomeFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //This doesn't have a viewmodel as it have no use for it, as everything is done in the fragments.


        //For navigation between fragments
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        invalidateOptionsMenu();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_map, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);

        //For notifications
        createNotificationChannel();
        startFridayNotification();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        //Remove action bar
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }

        checkPermissions();
    }


    // Implemented by following the code examples in: https://developer.android.com/training/notify-user/build-notification
    private void createNotificationChannel() {


        CharSequence name = getString(R.string.notificationChannelName);
        String description = getString(R.string.notificationChannelDescription);
        String channelID = getString(R.string.notificationChannelID);

        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(channelID, name, importance);
        channel.setDescription(description);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

        Log.d(Constants.NOTIFICATION_TAG, "createNotificationChannel: The notification channel was initialized");
    }


    public void startFridayNotification()
    {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 16);
        calendar.set(Calendar.MINUTE, 00); // Not necessary, but used to force notification in testing


        Intent intent = new Intent(getApplicationContext(), Notification_receiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 201, intent, PendingIntent.FLAG_MUTABLE);
        // FLAG_UPDATE_CURRENT
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setInexactRepeating(alarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmManager.INTERVAL_DAY, pendingIntent);

        // For debugging
        // alarmManager.setInexactRepeating(alarmManager.RTC_WAKEUP, System.currentTimeMillis(), 10 * 1000, pendingIntent);

        Log.d(Constants.NOTIFICATION_TAG, "startFridayNotification: The notification AlarmManager was initialized");
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }
    }
}
