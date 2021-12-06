package dk.au.mad21fall.appproject.group3.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import dk.au.mad21fall.appproject.group3.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    //A simple activity for resetting the password.

    private EditText txtEmail;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        txtEmail = findViewById(R.id.txtEmailForgot);
        btnSubmit = findViewById(R.id.btnSubmitEmail);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendMail();
            }
        });

    }

    //When an input is given and submit is clicked, use Firebase in build function to reset password.
    private void SendMail() {
        String mail = txtEmail.getText().toString();
        FirebaseAuth.getInstance().sendPasswordResetEmail(mail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //If success go back to login
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), R.string.MailSent, Toast.LENGTH_SHORT).show();
                            finish();
                        } else{
                            //Else try again
                            Toast.makeText(getApplicationContext(), R.string.Error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}