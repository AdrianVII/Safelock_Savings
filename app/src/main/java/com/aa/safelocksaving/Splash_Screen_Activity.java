package com.aa.safelocksaving;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.aa.safelocksaving.DAO.DAOConfigurationData;
import com.aa.safelocksaving.Operation.CheckUpdate;
import com.aa.safelocksaving.data.Authentication;
import com.google.firebase.auth.FirebaseAuth;

public class Splash_Screen_Activity extends AppCompatActivity {
    private MyCountDownTimer countDownTimer;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new DAOConfigurationData(this).setLanguage();
        checkNightMode();
        setContentView(R.layout.splash_screen_activity);
        countDownTimer = new MyCountDownTimer(2000, 1000);
        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.displacement_up);
        Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.displacement_down);

        TextView textView = findViewById(R.id.textView);
        ImageView imageView = findViewById(R.id.imageView);

        textView.setAnimation(animation2);
        imageView.setAnimation(animation1);
        //checkBatteryOptimization(); //Test...
        new CheckUpdate(this, () -> new Handler().postDelayed(() -> {
            if (new Authentication().isLogged(FirebaseAuth.getInstance())) {
                if (new DAOConfigurationData(this).verifyBiometric())
                    startActivity(new Intent(getBaseContext(), SignIn_Biometric_Activity.class));
                else
                    startActivity(new Intent(this, Main_Activity.class));
                countDownTimer.start();
            } else {
                startActivity(new Intent(getBaseContext(), Start_Activity.class));
                countDownTimer.start();
            }
        }, 2000));
    }

    private boolean isIgnoringBatteryOptimizations() { return ((PowerManager)getSystemService(Context.POWER_SERVICE)).isIgnoringBatteryOptimizations(getPackageName()); }

    private void checkBatteryOptimization() {
        if (!isIgnoringBatteryOptimizations()) startActivity(new Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS));
    }

    class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long startTime, long interval) { super(startTime, interval); }

        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            finish();
        }
    }

    private void checkNightMode() {
        if (new DAOConfigurationData(this).verifyNightMode())
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

}
