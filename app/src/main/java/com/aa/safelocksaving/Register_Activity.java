package com.aa.safelocksaving;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.aa.safelocksaving.DAO.DAOUser;
import com.aa.safelocksaving.DAO.DAOUserData;
import com.aa.safelocksaving.data.User;
import com.aa.safelocksaving.data.Authentication;
import com.aa.safelocksaving.Operation.md5;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register_Activity extends AppCompatActivity {
    private TextView btnBACK;
    private TextView btnSIGNIN;
    private Authentication authentication;
    private EditText name;
    private EditText lastname;
    private EditText email;
    private EditText password;
    private EditText confirm_pass;
    private Button btnNEXT;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DAOUser daoUser;
    private DAOUserData daoUserData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        daoUserData = new DAOUserData(this);
        daoUser =new DAOUser();
        authentication = new Authentication();
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.register_activity);
        name = findViewById(R.id.name);
        lastname = findViewById(R.id.lastname);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirm_pass = findViewById(R.id.confirm_pass);
        btnBACK = findViewById(R.id.btnBACK);
        btnBACK.setOnClickListener(view -> {
            onBackPressed();
        });
        btnSIGNIN = findViewById(R.id.btnSIGNIN);
        btnSIGNIN.setOnClickListener(view -> {
            startActivity(new Intent(this, SignIn_Activity.class));
            finish();
        });
        btnNEXT = findViewById(R.id.btnNEXT);
        btnNEXT.setOnClickListener(view -> createUser());
        confirm_pass.setOnKeyListener((v, keyCode, event) -> {
            if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER){
                createUser();
                return true;

            }
            return false;
        });
    }

    private void createUser() {
        if (authentication.isCorrect(name, lastname, email, password, confirm_pass, this)) {
            String nameText = name.getText().toString();
            String lastnameText = lastname.getText().toString();
            String emailText = email.getText().toString();
            String passwordText = password.getText().toString();
            mAuth.createUserWithEmailAndPassword(emailText, passwordText)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            user = mAuth.getCurrentUser();
                            String pass = md5.getMD5(password.getText().toString());
                            User dataUser = new User(nameText, lastnameText, emailText, pass);
                            daoUser.add(dataUser, user.getUid()).addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    user.sendEmailVerification();
                                    daoUserData.add(dataUser);
                                    Toast.makeText(this, getString(R.string.pleaseCheckYourEmailText), Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(this, SignIn_Activity.class));
                                    finish();
                                }
                            });
                        } else Toast.makeText(this, getString(R.string.failedToRegisterText), Toast.LENGTH_SHORT).show();
                    });
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, Start_Activity.class));
    }
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("name", name.getText().toString());
        outState.putString("last", lastname.getText().toString());
        outState.putString("email", email.getText().toString());
        outState.putString("password", password.getText().toString());
        outState.putString("confirm", confirm_pass.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        name.setText(savedInstanceState.getString("name"));
        lastname.setText(savedInstanceState.getString("last"));
        email.setText(savedInstanceState.getString("email"));
        password.setText(savedInstanceState.getString("password"));
        confirm_pass.setText(savedInstanceState.getString("confirm"));

    }
}
