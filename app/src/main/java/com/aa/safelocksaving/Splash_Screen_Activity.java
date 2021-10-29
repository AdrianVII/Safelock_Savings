package com.aa.safelocksaving;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.aa.safelocksaving.DAO.DAOConfigurationData;

public class Splash_Screen_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new DAOConfigurationData(this).setLanguage();
        setContentView(R.layout.splash_screen_activity);
        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.desplazamiento_arriba);
        Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.desplazamiento_abajo);

        TextView textView = findViewById(R.id.textView);
        ImageView imageView = findViewById(R.id.imageView);

        textView.setAnimation(animation2);
        imageView.setAnimation(animation1);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(getBaseContext(), Start_Activity.class);
            Pair[] pairs = new Pair[2];
            pairs[0] = new Pair<View, String>(imageView, "imageViewTransition");
            pairs[1] = new Pair<View, String>(textView, "textViewTransition");
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, pairs);
            startActivity(intent, options.toBundle());
            //finish();
        }, 4000);
    }
}
