package com.aa.safelocksaving;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.aa.safelocksaving.DAO.DAOUserData;
import com.aa.safelocksaving.Dialog.Dialog_Box;
import com.aa.safelocksaving.Operation.OPBasics;
import com.aa.safelocksaving.data.Authentication;
import com.aa.safelocksaving.data.UserData;

import java.util.Calendar;
import java.util.concurrent.Executor;

import de.hdodenhof.circleimageview.CircleImageView;
import soup.neumorphism.NeumorphImageView;

public class SignIn_Biometric_Activity extends AppCompatActivity {
    private CircleImageView imageView;
    private TextView name;
    private TextView day;
    private NeumorphImageView biometric;
    private TextView signOut;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_biometric_activity);
        fingerPrint();
        imageView = findViewById(R.id.imageView);
        name = findViewById(R.id.name);
        day = findViewById(R.id.days);
        biometric = findViewById(R.id.biometric);
        sayHi();
        signOut = findViewById(R.id.btnSIGNOUT);
        loadPicture();
        name.setText(new DAOUserData (this).get(UserData.Name, "Unknown"));
    }

    private void sayHi() {
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if(hour < 12 && hour >= 0){
            day.setText(getString(R.string.goodMorningText));
        }else if (hour >= 12 && hour <=19){
            day.setText(getString(R.string.goodAfternoonText));
        }else if(hour >=20 && hour <=23){
            day.setText(getString(R.string.goodNightText));
        }
    }

    private void loadPicture() { new OPBasics().getPicture(bitmap -> imageView.setImageBitmap(bitmap) ); }

    @Override
    protected void onResume() {
        super.onResume();
        biometric.setOnClickListener(view -> fingerPrint());
        signOut.setOnClickListener(view -> {
            new Dialog_Box(this, getString(R.string.logoutText), getString(R.string.areYouSureToLogoutText)).OnActionButton(new Dialog_Box.OnPositiveClickListener(){

                @Override
                public void positiveClick(View view, Activity activity) {
                    if (new Authentication().logoutUser()) {
                        startActivity(new Intent(getBaseContext(), Start_Activity.class));
                        finish();
                    }
                }

                @Override
                public void negativeClick(View view) {

                }
            });
        });
    }

    private void fingerPrint() {
        Executor executor = ContextCompat.getMainExecutor(this);
        BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                startActivity(new Intent(getBaseContext(), Main_Activity.class));
                finish();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle(getString(R.string.enterFingerprintText))
                .setNegativeButtonText(getString(R.string.cancelText))
                .build();
        biometricPrompt.authenticate(promptInfo);
    }
}
