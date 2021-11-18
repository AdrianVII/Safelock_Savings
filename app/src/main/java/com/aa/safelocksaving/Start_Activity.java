package com.aa.safelocksaving;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.aa.safelocksaving.data.Authentication;
import com.aa.safelocksaving.DAO.DAOConfigurationData;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.Executor;

public class Start_Activity extends AppCompatActivity {
    private Button btnSIGNIN;
    private Button btnREGISTER;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);
        btnSIGNIN = findViewById(R.id.btnSIGNIN);
        btnSIGNIN.setOnClickListener(view -> {
            startActivity(new Intent(this, SignIn_Activity.class));
            finish();
        });
        btnREGISTER = findViewById(R.id.btnREGISTER);
        btnREGISTER.setOnClickListener(view -> {
            startActivity(new Intent(this, Register_Activity.class));
            finish();
        });
    }

}
