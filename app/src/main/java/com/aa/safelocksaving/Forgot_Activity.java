package com.aa.safelocksaving;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;

public class Forgot_Activity extends Activity {
    private TextView btnBACK;
    private EditText email;
    private FirebaseAuth mAuth;
    private Button btnNEXT;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.forgot_activity);
        btnBACK = findViewById(R.id.btnBACK);
        btnBACK.setOnClickListener(view -> {
            onBackPressed();
        });

        email = findViewById(R.id.email);
        btnNEXT = findViewById(R.id.btnNEXT);
        btnNEXT.setOnClickListener( view ->{
            recoveryPassword();
        });
    }

    private void recoveryPassword() {
        String emailText = email.getText().toString().trim();
        if (emailText.isEmpty()) {
            email.setError(getString(R.string.emailIsRequiredText));
            email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            email.setError(getString(R.string.provideValidEmailText));
            email.requestFocus();
            return ;
        }
        mAuth.sendPasswordResetEmail(emailText)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, getString(R.string.pleaseCheckYourEmailText), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, getString(R.string.errorSendingEmailText), Toast.LENGTH_SHORT).show();
                    }
                    startActivity(new Intent(this, SignIn_Activity.class));
                    finish();
                });
    }
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("email", email.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        email.setText(savedInstanceState.getString("email"));

    }

}
