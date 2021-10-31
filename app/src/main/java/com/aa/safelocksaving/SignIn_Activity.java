package com.aa.safelocksaving;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.aa.safelocksaving.DAO.DAOUserData;
import com.aa.safelocksaving.data.Authentication;
import com.aa.safelocksaving.data.User;
import com.aa.safelocksaving.Operation.OPBasics;
import com.aa.safelocksaving.Operation.md5;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;

import java.util.Objects;

public class SignIn_Activity extends Activity {
    private TextView btnFORGOT;
    private TextView btnBACK;
    private TextView btnREGISTER;
    private Button btnNEXT;
    private Authentication authentication;
    private EditText email;
    private EditText password;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private User userData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authentication = new Authentication();
        mAuth = FirebaseAuth.getInstance();
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
            finish();
        });
        btnNEXT = findViewById(R.id.btnNEXT);
        btnNEXT.setOnClickListener(view -> {
            loginUser();
        });
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
    }

    private void loginUser() {
        if (authentication.isCorrect(email, password, this)) {
            String emailText = email.getText().toString().trim();
            String passwordText = md5.getMD5(password.getText().toString().trim());
            mAuth.signInWithEmailAndPassword(emailText, passwordText)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            user = mAuth.getCurrentUser();
                            if (Objects.requireNonNull(user).isEmailVerified()) {
                                new OPBasics().getUserData(new OPBasics.getDataListener() {
                                    @Override
                                    public void getUser(User user) {
                                        new DAOUserData(getBaseContext()).add(user);
                                        startActivity(new Intent(getBaseContext(), Main_Activity.class));
                                        finish();
                                    }
                                    @Override
                                    public void getError(DatabaseError error) { }
                                });
                            } else {
                                Toast.makeText(this, getString(R.string.pleaseCheckYourEmailText), Toast.LENGTH_SHORT).show();
                                user.sendEmailVerification();
                                authentication.logoutUser(mAuth);
                            }
                        } else {
                            email.setText("");
                            password.setText("");
                            email.requestFocus();
                            Toast.makeText(this, getString(R.string.invalidEmailOrPasswordText), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    @Override
        public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, Start_Activity.class));
    }
}
