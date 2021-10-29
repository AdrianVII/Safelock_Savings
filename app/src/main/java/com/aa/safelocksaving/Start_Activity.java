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
import com.aa.safelocksaving.data.DAOConfigurationData;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.Executor;

public class Start_Activity extends AppCompatActivity {
    private Button btnSIGNIN;
    private Button btnREGISTER;
    private Authentication authentication;
    private FirebaseAuth mAuth;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new DAOConfigurationData(this).setLanguage();
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
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (authentication.isLogged(mAuth)) {
            if (new DAOConfigurationData(this).verifyBiometric()) {
                Executor executor = ContextCompat.getMainExecutor(this);
                biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);
                    }

                    @Override
                    public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        startActivity(new Intent(getBaseContext(), Main_Activity.class));
                        finish();
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
                biometricPrompt.authenticate(promptInfo);
            } else {
                startActivity(new Intent(this, Main_Activity.class));
                finish();
            }
        }
    }
}
