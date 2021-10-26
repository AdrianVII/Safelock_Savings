package com.aa.safelocksaving.dataOperation;

import android.app.Activity;
import android.util.Patterns;
import android.widget.EditText;

import com.aa.safelocksaving.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class Authentication {
    public boolean isLogged(FirebaseAuth auth) {
        FirebaseUser user = auth.getCurrentUser();
        return user != null;
    }

    public boolean logoutUser(FirebaseAuth auth) {
        if (isLogged(auth)) {
            auth.signOut();
            return true;
        } return false;
    }

    public boolean logoutUser() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (isLogged(auth)) {
            auth.signOut();
            return true;
        } return false;
    }

    public boolean isCorrect(EditText email, EditText password, Activity activity) {
        String emailText = email.getText().toString().trim();
        String passwordText = password.getText().toString().trim();
        if (emailText.isEmpty()) {
            email.setError(activity.getString(R.string.emailIsRequiredText));
            email.requestFocus();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            email.setError(activity.getString(R.string.provideValidEmailText));
            email.requestFocus();
            return false;
        }
        if (passwordText.isEmpty()) {
            password.setError(activity.getString(R.string.passwordIsRequiredtext));
            password.requestFocus();
            return false;
        }
        if (passwordText.length() < 6) {
            password.setError(activity.getString(R.string.leastSixCharactersText));
            password.requestFocus();
            return false;
        }
        return true;
    }

    public boolean isCorrect(
            EditText name,
            EditText lastname,
            EditText email,
            EditText password,
            EditText passwordConfirm,
            Activity activity) {

        String nameText = name.getText().toString().trim();
        String lastnameText = lastname.getText().toString().trim();
        String emailText = email.getText().toString().trim();
        String passwordText = password.getText().toString().trim();
        String passwordConfirmText = passwordConfirm.getText().toString().trim();

        if (nameText.isEmpty()) {
            name.setError(activity.getString(R.string.nameIsRequiredText));
            name.requestFocus();
            return false;
        }
        if (lastnameText.isEmpty()) {
            lastname.setError(activity.getString(R.string.lastnameIsRequiredText));
            lastname.requestFocus();
            return false;
        }
        if (emailText.isEmpty()) {
            email.setError(activity.getString(R.string.emailIsRequiredText));
            email.requestFocus();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            email.setError(activity.getString(R.string.provideValidEmailText));
            email.requestFocus();
            return false;
        }
        if (passwordText.isEmpty()) {
            password.setError(activity.getString(R.string.passwordIsRequiredtext));
            password.requestFocus();
            return false;
        }
        if (passwordText.length() < 6) {
            password.setError(activity.getString(R.string.leastSixCharactersText));
            password.requestFocus();
            return false;
        }
        if (!passwordConfirmText.equals(passwordText)) {
            passwordConfirm.setError(activity.getString(R.string.passwordDoesnotMatchText));
            passwordConfirm.requestFocus();
            return false;
        }

        return true;

    }

}
