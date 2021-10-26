package com.aa.safelocksaving;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.aa.safelocksaving.dataOperation.Authentication;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class SignIn_Activity extends Activity {
    private TextView btnFORGOT;
    private TextView btnBACK;
    private TextView btnREGISTER;
    private Button btnNEXT;
    private Authentication authentication;
    private EditText email;
    private EditText password;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authentication = new Authentication();
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.signin_activity);
        btnFORGOT = findViewById(R.id.btnFORGOT);
        btnFORGOT.setOnClickListener(view -> {
            startActivity(new Intent(this, Forgot_Activity.class));
        });
        btnBACK = findViewById(R.id.btnBACK);
        btnBACK.setOnClickListener(view -> {
            onBackPressed();
        });
        btnREGISTER = findViewById(R.id.btnREGISTER);
        btnREGISTER.setOnClickListener(view -> {
            startActivity(new Intent(this, Register_Activity.class));
            finish();
        });
        btnNEXT = findViewById(R.id.btnNEXT);
        btnNEXT.setOnClickListener(view -> {
            loginUser();
        });
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
    }

    private void loginUser() {
        if (authentication.isCorrect(email, password, this)) {
            String emailText = email.getText().toString().trim();
            String passwordText = password.getText().toString().trim();
            mAuth.signInWithEmailAndPassword(emailText, passwordText)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            user = mAuth.getCurrentUser();
                            if (Objects.requireNonNull(user).isEmailVerified()) {
                                startActivity(new Intent(this, Main_Activity.class));
                                finish();
                            } else {
                                Toast.makeText(this, getString(R.string.pleaseCheckYourEmailText), Toast.LENGTH_SHORT).show();
                                user.sendEmailVerification();
                                authentication.logoutUser(mAuth);
                            }
                        }
                    });
        }
    }

    @Override
        public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, Start_Activity.class));
    }
}
