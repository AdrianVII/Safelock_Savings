package com.aa.safelocksaving;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Information_Activity extends AppCompatActivity {
    private TextView btnBack;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.information_activity);
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(view -> onBackPressed());
    }
}
