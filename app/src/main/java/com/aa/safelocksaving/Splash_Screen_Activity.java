package com.aa.safelocksaving;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.aa.safelocksaving.DAO.DAOConfigurationData;

public class Splash_Screen_Activity extends AppCompatActivity {
    private MyCountDownTimer countDownTimer;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new DAOConfigurationData(this).setLanguage();
        setContentView(R.layout.splash_screen_activity);
        countDownTimer = new MyCountDownTimer(2000, 1000);
        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.displacement_up);
        Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.displacement_down);

        TextView textView = findViewById(R.id.textView);
        ImageView imageView = findViewById(R.id.imageView);

        textView.setAnimation(animation2);
        imageView.setAnimation(animation1);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(getBaseContext(), Start_Activity.class);
            startActivity(intent);
            countDownTimer.start();
        }, 2000);
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

}
