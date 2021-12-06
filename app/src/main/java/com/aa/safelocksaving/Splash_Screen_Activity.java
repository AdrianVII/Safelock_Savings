package com.aa.safelocksaving;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.aa.safelocksaving.DAO.DAOConfigurationData;
import com.aa.safelocksaving.Dialog.Dialog_Box;
import com.aa.safelocksaving.Operation.CheckUpdate;
import com.aa.safelocksaving.data.Authentication;
import com.google.firebase.auth.FirebaseAuth;

public class Splash_Screen_Activity extends AppCompatActivity {
    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new DAOConfigurationData(this).setLanguage();
        checkNightMode();
        setContentView(R.layout.splash_screen_activity);
        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.displacement_up);
        Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.displacement_down);
        TextView textView = findViewById(R.id.textView);
        ImageView imageView = findViewById(R.id.imageView);
        textView.setAnimation(animation2);
        imageView.setAnimation(animation1);
        handler = new Handler();
        runnable = () -> {
            if (isIgnoringBatteryOptimizations()) {
                if (new Authentication().isLogged(FirebaseAuth.getInstance())) {
                    if (new DAOConfigurationData(this).verifyBiometric()) startActivity(new Intent(getBaseContext(), SignIn_Biometric_Activity.class));
                    else startActivity(new Intent(this, Main_Activity.class));
                } else startActivity(new Intent(this, Start_Activity.class));
                finish();
            } else new Dialog_Box(this, getString(R.string.batteryOptimizationText), getString(R.string.batteryOptimizationDescriptionText)).OnActionButton(new Dialog_Box.OnPositiveClickListener() {
                @Override
                public void positiveClick(View view, Activity activity) { startActivity(new Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)); }

                @Override
                public void negativeClick(View view) { finish(); }
            });
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        new CheckUpdate(this, () -> handler.postDelayed(runnable, 2000));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        handler.removeCallbacks(runnable);
        finish();
    }

    private boolean isIgnoringBatteryOptimizations() { return ((PowerManager)getSystemService(Context.POWER_SERVICE)).isIgnoringBatteryOptimizations(getPackageName()); }

    private void checkNightMode() {
        if (new DAOConfigurationData(this).verifyNightMode()) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }
}