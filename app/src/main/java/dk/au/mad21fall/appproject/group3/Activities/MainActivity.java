package dk.au.mad21fall.appproject.group3.Activities;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import static android.content.ContentValues.TAG;
import android.os.Build;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import static android.content.ContentValues.TAG;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;
import java.util.Calendar;

import dk.au.mad21fall.appproject.group3.Models.Bar;
import dk.au.mad21fall.appproject.group3.Models.Constants;
import dk.au.mad21fall.appproject.group3.Other.HomeFragmentCallback;
import dk.au.mad21fall.appproject.group3.Other.Notification_receiver;
import dk.au.mad21fall.appproject.group3.ViewModels.MainViewModel;
import dk.au.mad21fall.appproject.group3.R;
import dk.au.mad21fall.appproject.group3.databinding.ActivityMainBinding;
import dk.au.mad21fall.appproject.group3.ui.home.HomeFragment;
import dk.au.mad21fall.appproject.group3.ui.home.HomeViewModel;

//TODO: add color accent in custom button <solid android:color="@color/" https://www.youtube.com/watch?v=WUJ6Ve7_mA0&ab_channel=Stevdza-San 4:25
//TODO: make the compass
//TODO: Make the map
//TODO: Filter search
//TODO: Tablet version
//TODO: Custom logo
//TODO: Dark theme
//TODO: Make facebook login work?!
//TODO: make a second language
//TODO: Change the name
//TODO: Comments!
//TODO: Fix fragment icon 'Notification' to fit the compas fragment better
//TODO: DEN CRASHER PÅ LANDSCAPE MODE!!!!!! DEN SKAL VERTICAL-LOCKES
//TODO: Use notifications
//TODO: Resource externalization
//TODO: 2 languages (MANGLER STADIG FILTERLAYOUTS)
//TODO: Fix 'forgot password'




public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    FirebaseAuth auth;
    MainViewModel mainVM;
    HomeViewModel homeVM;
    HomeFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainVM = new ViewModelProvider(this).get(MainViewModel.class);

        mainVM.getBars().observe(this, new Observer<ArrayList<Bar>>() {
            @Override
            public void onChanged(ArrayList<Bar> bars) {
                //TODO: make this update recycler view!
                //TODO: Perhaps move this to the fragments?
            }
        });

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

        createNotificationChannel();
        startFridayNotification();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
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
