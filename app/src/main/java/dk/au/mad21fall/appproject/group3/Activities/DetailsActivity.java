package dk.au.mad21fall.appproject.group3.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

    TextView txtName, txtOpen, txtClose, txtAddress;
    Button btnFacebook, btnInstagram;
    ImageView imgIcon;
    private DetailsViewModel detailsViewModel;
    Bar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent data = getIntent();
        String barName = data.getStringExtra(Constants.BAR_NAME);
        Log.d(TAG, "onCreate: The name is " + barName);
        detailsViewModel = new ViewModelProvider(this).get(DetailsViewModel.class);
        bar = detailsViewModel.getBar(barName);
        setupView();
        setupUI();
    }

    private void setupView() {
        txtName = findViewById(R.id.txtNameDetails);
        txtOpen = findViewById(R.id.txtOpen);
        txtClose = findViewById(R.id.txtClose);
        txtAddress = findViewById(R.id.txtAddress);
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
    }

    private void gotoUrl(String url) {
        //https://stackoverflow.com/questions/2762861/android-goto-http-url-on-button-click
        Intent browse = new Intent( Intent.ACTION_VIEW , Uri.parse(url) );
        startActivity( browse );
    }

    private void setupUI() {
        txtName.setText(bar.getName());
        txtOpen.setText(getString(R.string.txtOpen) + " " + bar.getOpen());
        txtClose.setText(getString(R.string.txtClose) + " " + bar.getClose());
        txtAddress.setText(bar.getAddress());

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference dateRef = storageRef.child("/" + bar.getName() + ".png");
        dateRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
        {
            @Override
            public void onSuccess(Uri downloadUrl)
            {
                Log.d(TAG, "onSuccess: We have success!" + downloadUrl);
                Glide.with(imgIcon.getContext()).load(downloadUrl).into(imgIcon);
            }
        });
    }
}