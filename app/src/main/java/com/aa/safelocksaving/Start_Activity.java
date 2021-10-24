package com.aa.safelocksaving;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

public class Start_Activity extends Activity {
    private Button btnSIGNIN;
    private Button btnREGISTER;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);
        btnSIGNIN = findViewById(R.id.btnSIGNIN);
        btnSIGNIN.setOnClickListener(view -> {
            startActivity(new Intent(this, SignIn_Activity.class));
        });
        btnREGISTER = findViewById(R.id.btnREGISTER);
        btnREGISTER.setOnClickListener(view -> {
           startActivity(new Intent(this, Register_Activity.class));
        });
    }
}
