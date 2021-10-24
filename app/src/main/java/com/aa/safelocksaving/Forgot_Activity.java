package com.aa.safelocksaving;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class Forgot_Activity extends Activity {
    private TextView btnBACK;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_activity);
        btnBACK = findViewById(R.id.btnBACK);
        btnBACK.setOnClickListener(  view -> {
            onBackPressed();
        });
    }
}
