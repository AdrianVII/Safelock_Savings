package com.aa.safelocksaving.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.aa.safelocksaving.Account_Activity;
import com.aa.safelocksaving.R;

import soup.neumorphism.NeumorphButton;

public class Dialog_Subscription_Notification extends Dialog {
    private Button yes;
    private Button no;
    private Button cancelled;

    public interface onButtonClickListener {
        void onYesClick(View view);
        void onNoClick(View view);
        void onCancelledClick(View view);
    }

    public interface onVisibilityListener {
        void OnVisibility(Button cancel);
    }

    public Dialog_Subscription_Notification(@NonNull Context context, onButtonClickListener listener, onVisibilityListener visibilityListener) {
        super(context);
        setContentView(R.layout.subscription_notification_dialog);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Window window = getWindow();
        window.setGravity(Gravity.CENTER);
        yes = findViewById(R.id.yes);
        no = findViewById(R.id.no);
        cancelled = findViewById(R.id.cancelled);
        visibilityListener.OnVisibility(cancelled);
        yes.setOnClickListener(view -> {
            listener.onYesClick(view);
            dismiss();
        });
        no.setOnClickListener(view -> {
            listener.onNoClick(view);
            dismiss();
        });
        cancelled.setOnClickListener(view -> {
            listener.onCancelledClick(view);
            dismiss();
        });
    }
}
