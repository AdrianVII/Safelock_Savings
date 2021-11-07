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

    static public void showScaleAlphaHide(View view) {
        view.animate()
                .scaleY(0.0f)
                .scaleX(0.0f)
                .alpha(0.0f)
                .setDuration(100).withEndAction(() -> view.setVisibility(View.GONE))
                .start();
    }

    static  public void showScaleAlphaShow(View view, float X, float Y) {
        view.animate()
                .scaleX(X)
                .scaleY(Y)
                .alpha(1.0f)
                .setDuration(100)
                .start();
    }

}
