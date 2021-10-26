package com.aa.safelocksaving;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.aa.safelocksaving.dataOperation.Authentication;

public class Start_Activity extends Activity {
    private Button btnSIGNIN;
    private Button btnREGISTER;
    private Authentication authentication;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authentication = new Authentication();
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

    @Override
    protected void onStart() {
        super.onStart();
        if (authentication.isLogged()) {
            startActivity(new Intent(this, Main_Activity.class));
        }
    }
}
