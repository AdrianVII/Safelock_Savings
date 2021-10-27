package com.aa.safelocksaving;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Account_Activity extends Activity {
    private TextView email;
    private TextView name;
    private LinearLayout changepass;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.account_activity);
        email = findViewById(R.id.email);
        name =findViewById(R.id.name);
        changepass = findViewById(R.id.changepass);
    }

    @Override
    protected void onStart() {
        super.onStart();
        user = mAuth.getCurrentUser();
        String emailText = user.getEmail();
        String nameText = user.getDisplayName();
        email.setText(emailText);
        name.setText(nameText);
    }

    private void LoadData() {

    }

}
