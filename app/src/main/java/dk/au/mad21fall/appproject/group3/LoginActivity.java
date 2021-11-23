package dk.au.mad21fall.appproject.group3;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

import java.net.Authenticator;
import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth auth;
    Button btnLogin;

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),   //default contract
            new ActivityResultCallback<ActivityResult>() {          //our callback
                @Override
                public void onActivityResult(ActivityResult result) {   //result contains result code and data
                    if (result.getResultCode() == RESULT_OK) {
                        GoToMain();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login();
            }
        });

    }

    private void Login() {
        if(auth==null){
            auth = FirebaseAuth.getInstance();
        }
        if(auth.getCurrentUser() != null){
            GoToMain();
        }
        else{

            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build()
                    //new AuthUI.IdpConfig.FacebookBuilder().build()
            );

            launcher.launch(
                    AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build());
        }
    }

    private void GoToMain() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}