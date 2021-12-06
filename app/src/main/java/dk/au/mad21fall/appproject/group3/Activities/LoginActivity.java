package dk.au.mad21fall.appproject.group3.Activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

import dk.au.mad21fall.appproject.group3.R;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";


    private FirebaseAuth auth;
    private Button btnLoginEmail, btnSignUp, btnLoginFacebook, btnForgotPassword;
    private CallbackManager mCallbackManager;
    private TextInputLayout txtUsername, txtPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Initialize Facebook SDK - depricated but we didnt know what else to use
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        setupUI();
        setupAuth();


        //Initialize facebook stuff
        //https://firebase.google.com/docs/auth/android/facebook-login?hl=da
        mCallbackManager = CallbackManager.Factory.create();
        btnLoginFacebook = findViewById(R.id.btnLoginFacebook);
        btnLoginFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this,
                        Arrays.asList("email", "public_profile"));
                LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d(TAG, "facebook:onSuccess:" + loginResult);
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG, "facebook:onCancel");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d(TAG, "facebook:onError", error);
                    }
                });
            }
        });
    }

    //Setting up all the UI.
    private void setupUI() {
        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);

        btnLoginEmail = findViewById(R.id.btnLoginEmail);
        btnLoginEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MailLogin();
            }
        });
        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MailSignUp();
            }
        });

        btnForgotPassword = findViewById(R.id.btnForgotPassword);
        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoToForgotPassword();
            }
        });
    }


    //Facebook login method as standard.
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, go to main
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();
                            GoToMain();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, R.string.facebookAuthenticationFail,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    //Setup the authentication, and check if a user is already logged in in which case go to main activity
    private void setupAuth() {
        if(auth==null){
            auth = FirebaseAuth.getInstance();
        }
        if(auth.getCurrentUser() != null){
            GoToMain();
        }
    }




    //Login with mail as taken from week 10 live code #2
    @SuppressLint("WrongConstant")
    private void MailLogin() {
        String email = txtUsername.getEditText().getText().toString();
        String password = txtPassword.getEditText().getText().toString();

        if(email.equals("") || password.equals("")){
            Toast.makeText(this, R.string.mailMissingInfo, Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG, "MailLogin: MAIL = " + email);
        Log.d(TAG, "MailLogin: PASSWORD = " + password);

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    GoToMain();
                }
                else{
                    Toast.makeText(LoginActivity.this, R.string.loginError, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Sign up with mail as taken from week 10 live code #2
    private void MailSignUp() {
        String email = txtUsername.getEditText().getText().toString();
        String password = txtPassword.getEditText().getText().toString();

        if(email.equals("") || password.equals("")){
            Toast.makeText(this, R.string.mailMissingInfo, Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.length() <= 6){
            Toast.makeText(this, R.string.shortPassword, Toast.LENGTH_SHORT).show();
            return;
        }

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, R.string.userCreateSuccess, Toast.LENGTH_SHORT).show();
                            GoToMain();
                        }
                        else{
                            Toast.makeText(LoginActivity.this, R.string.userCreateFailure, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    //Go to main activity
    private void GoToMain() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    //Go to forgot password activity.
    private void GoToForgotPassword() {
        Intent i = new Intent(this, ForgotPasswordActivity.class);
        startActivity(i);
    }

}