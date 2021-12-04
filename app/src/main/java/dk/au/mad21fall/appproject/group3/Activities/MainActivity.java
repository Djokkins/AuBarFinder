package dk.au.mad21fall.appproject.group3.Activities;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;

import dk.au.mad21fall.appproject.group3.Models.Bar;
import dk.au.mad21fall.appproject.group3.ViewModels.MainViewModel;
import dk.au.mad21fall.appproject.group3.R;
import dk.au.mad21fall.appproject.group3.databinding.ActivityMainBinding;

//TODO: add color accent in custom button <solid android:color="@color/" https://www.youtube.com/watch?v=WUJ6Ve7_mA0&ab_channel=Stevdza-San 4:25
//TODO: make a repository class
//TODO: make a ViewModel for the details class
//TODO: make details get data from the viewmodel via the barID and post the info
//TODO: get pictures down via gradle
//TODO: Fix the mail
//TODO: make the compass
//TODO: Make the map
//TODO: Fix links to facebook and instagram on details
//TODO: Make Splash screen pretty
//TODO: Distance to the bars on a service
//TODO: Filter search
//TODO: Tablet version
//TODO: Custom logo
//TODO: Dark theme



public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    FirebaseAuth auth;
    MainViewModel mainVM;

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

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_map, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }



}