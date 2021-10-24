package com.aa.safelocksaving;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class Register_Activity extends Activity {
    private TextView btnBACK;
    private TextView btnSIGNIN;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        btnBACK = findViewById(R.id.btnBACK);
        btnBACK.setOnClickListener( view -> {
            onBackPressed();
        });
        btnSIGNIN = findViewById(R.id.btnSIGNIN);
        btnSIGNIN.setOnClickListener( view ->{
            startActivity(new Intent(this, SignIn_Activity.class));
        });
    }
}
