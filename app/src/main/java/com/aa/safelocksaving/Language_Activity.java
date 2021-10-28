package com.aa.safelocksaving;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.ConfigurationCompat;

import java.util.Locale;

public class Language_Activity extends AppCompatActivity {
    private TextView btnBack;
    private Button btnAPPLY;
    private RadioButton rdbSPANISH;
    private RadioButton rdbENGLISH;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.language_activity);
        btnBack = findViewById(R.id.btnBack);
        btnAPPLY = findViewById(R.id.btnAPPLY);
        rdbSPANISH = findViewById(R.id.rdbSPANISH);
        rdbENGLISH = findViewById(R.id.rdbENGLISH);
        btnAPPLY = findViewById(R.id.btnAPPLY);
        setCheck();
        btnBack.setOnClickListener(view -> onBackPressed());
        btnAPPLY.setOnClickListener(view -> {
            radioButtonChecked();
        });
    }

    private void setCheck() {
        sharedPreferences = getSharedPreferences("Configuration", Context.MODE_PRIVATE);
        String language = sharedPreferences.getString("Language", "en");
        if (language.equals("es")) rdbSPANISH.setChecked(true);
        else if (language.equals("en")) rdbENGLISH.setChecked(true);
    }

    private void radioButtonChecked() {
        String language = "en";
        if (rdbSPANISH.isChecked()) language = "es";
        else if(rdbENGLISH.isChecked()) language = "en";
        sharedPreferences = getSharedPreferences("Configuration", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Language", language);
        editor.apply();
        Toast.makeText(this, getString(R.string.restartTheAppText), Toast.LENGTH_SHORT).show();
    }

}
