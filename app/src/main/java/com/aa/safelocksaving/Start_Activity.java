package com.aa.safelocksaving;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.core.os.ConfigurationCompat;

import com.aa.safelocksaving.dataOperation.Authentication;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;
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
        setLanguage();
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
            if (verifyBiometric()) {
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
            } else{
                startActivity(new Intent(this, Main_Activity.class));
                finish();
            }
        }
    }

    private boolean verifyBiometric() {
        SharedPreferences sharedPreferences = getSharedPreferences("Configuration", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("biometric", false);
    }

    private void setLanguage() {
        String language = "";
        SharedPreferences sharedPreferences = getSharedPreferences("Configuration", Context.MODE_PRIVATE);
        if (!sharedPreferences.contains("Language")) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            language = ConfigurationCompat.getLocales(Resources.getSystem().getConfiguration()).toLanguageTags();
            editor.putString("Language", language);
            editor.apply();
        } else {
            language = sharedPreferences.getString("Language", "en");
            Locale locale = new Locale(language);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            Toast.makeText(this, language, Toast.LENGTH_SHORT).show();
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }
    }

}
