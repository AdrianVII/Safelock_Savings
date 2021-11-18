package com.aa.safelocksaving.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;

import com.aa.safelocksaving.Account_Activity;
import com.aa.safelocksaving.R;

import soup.neumorphism.NeumorphButton;

public class Dialog_Subcription_Notification extends Dialog {
    private NeumorphButton yes;
    private NeumorphButton no;
    private NeumorphButton cancelled;
    private Activity activity;

    public interface onButtonClickListener{
        void onYesClick(View view);
        void onNoClick(View view);
        void onCancelledClick(View view);
    }

    public Dialog_Subcription_Notification(@NonNull Activity activity, onButtonClickListener listener ) {
        super(activity);
        this.activity = activity;
        super.setContentView(R.layout.subcription_notification_dialog);
        super.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Window window = super.getWindow();
        window.setGravity(Gravity.CENTER);
        yes = super.findViewById(R.id.yes);
        no = super.findViewById(R.id.no);
        cancelled = super.findViewById(R.id.cancelled);
        yes.setOnClickListener(view ->{
            listener.onYesClick(view);
            dismiss();
        });
        no.setOnClickListener(view ->{
            listener.onNoClick(view);
            dismiss();
        });
        cancelled.setOnClickListener(view ->{
            listener.onCancelledClick(view);
            dismiss();
        });
    }
}
