package com.aa.safelocksaving;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.aa.safelocksaving.dataOperation.Authentication;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.Executor;

public class Start_Activity extends Activity {
    private Button btnSIGNIN;
    private Button btnREGISTER;
    private Authentication authentication;
    private FirebaseAuth mAuth;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private boolean isCorrect = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authentication = new Authentication();
        mAuth = FirebaseAuth.getInstance();
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
        /*Executor executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });

                promptInfo = new BiometricPrompt.PromptInfo.Builder()
                        .setTitle(getString(R.string.enterFingerprintText))
                        .setNegativeButtonText(getString(R.string.cancelText))
                        .build();
                        */
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (authentication.isLogged(mAuth)) {
            if (verifyBiometric()) {
                //Datos biometricos...


                biometricPrompt.authenticate(promptInfo);

                if (isCorrect) startActivity(new Intent(this, Main_Activity.class));

            } else startActivity(new Intent(this, Main_Activity.class));
            finish();
        }
    }

    private boolean verifyBiometric() {
        SharedPreferences sharedPreferences = getSharedPreferences("Biometric", Context.MODE_PRIVATE);
        Toast.makeText(this, "Datos biometricos: " + sharedPreferences.getBoolean("biometric", false), Toast.LENGTH_SHORT).show();
        return sharedPreferences.getBoolean("biometric", false);
    }

}
