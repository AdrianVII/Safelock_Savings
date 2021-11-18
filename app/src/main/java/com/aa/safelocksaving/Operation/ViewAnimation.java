package com.aa.safelocksaving.Operation;

import android.view.View;

final public class ViewAnimation {

    static public void showDownAnimation(View view) {
        view.animate()
                .translationY(100)
                .alpha(0.0f)
                .setDuration(100).withEndAction(() -> view.setVisibility(View.GONE))
                .start();
    }

    static public void showUpAnimation(View view, float Y) {
        view.setVisibility(View.VISIBLE);
        view.animate()
                .translationY(Y)
                .alpha(1.0f)
                .setDuration(100)
                .start();
    }


}
