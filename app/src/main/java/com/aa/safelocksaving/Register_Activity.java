package com.aa.safelocksaving;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.aa.safelocksaving.dataOperation.Authentication;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register_Activity extends Activity {
    private TextView btnBACK;
    private TextView btnSIGNIN;
    private Authentication authentication;
    private EditText name;
    private EditText lastname;
    private EditText email;
    private EditText password;
    private EditText confirm_pass;
    private Button btnNEXT;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authentication = new Authentication();
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.register_activity);
        name = findViewById(R.id.name);
        lastname = findViewById(R.id.lastname);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirm_pass = findViewById(R.id.confirm_pass);
        btnBACK = findViewById(R.id.btnBACK);
        btnBACK.setOnClickListener(view -> {
            onBackPressed();
        });
        btnSIGNIN = findViewById(R.id.btnSIGNIN);
        btnSIGNIN.setOnClickListener(view -> {
            startActivity(new Intent(this, SignIn_Activity.class));
            finish();
        });
        btnNEXT = findViewById(R.id.btnNEXT);
        btnNEXT.setOnClickListener(view -> createUser());
    }

    private void createUser() {
        if (authentication.isCorrect(name, lastname, email, password, confirm_pass, this)) {
            String emailText = email.getText().toString();
            String passwordText = password.getText().toString();
            mAuth.createUserWithEmailAndPassword(emailText, passwordText)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            user = mAuth.getCurrentUser();
                            if (user.isEmailVerified()) {
                                startActivity(new Intent(this, Main_Activity.class));
                            } else {
                                user.sendEmailVerification();
                                Toast.makeText(this, getString(R.string.pleaseCheckYourEmailText), Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(this, SignIn_Activity.class));
                            }
                            finish();
                        } else Toast.makeText(this, getString(R.string.failedToRegisterText), Toast.LENGTH_SHORT).show();
                    });
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, Start_Activity.class));
    }

}
