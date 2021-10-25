package com.aa.safelocksaving;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class SignIn_Activity extends Activity {
    private TextView btnFORGOT;
    private TextView btnBACK;
    private TextView btnREGISTER;
    private Button btnNEXT;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_activity);
        btnFORGOT = findViewById(R.id.btnFORGOT);
        btnFORGOT.setOnClickListener(view -> {
            startActivity(new Intent(this, Forgot_Activity.class));
        });
        btnBACK = findViewById(R.id.btnBACK);
        btnBACK.setOnClickListener(view -> {
            onBackPressed();
        });
        btnREGISTER = findViewById(R.id.btnREGISTER);
        btnREGISTER.setOnClickListener(view -> {
            startActivity(new Intent(this, Register_Activity.class));
        });
        btnNEXT = findViewById(R.id.btnNEXT);
        btnNEXT.setOnClickListener(view -> {
            startActivity(new Intent(this, Main_Activity.class));
        });
    }
}
