package com.aa.safelocksaving.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;

import com.aa.safelocksaving.R;

import java.nio.channels.AcceptPendingException;

import soup.neumorphism.NeumorphButton;

public class Dialog_Delete_Cards_Budgets extends Dialog {
    private NeumorphButton yes;
    private NeumorphButton no;
    private Activity activity;

    public interface onButtonClickListener{
        void onYesClick(View view);
        void onNoClick(View view);
    }

    public Dialog_Delete_Cards_Budgets(@NonNull Activity activity, onButtonClickListener listener) {
        super(activity);
        this.activity = activity;
        super.setContentView(R.layout.dialog_delete);
        super.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Window window = super.getWindow();
        window.setGravity(Gravity.CENTER);
        window.getAttributes().windowAnimations = R.anim.displacement_up;
        yes = super.findViewById(R.id.yes);
        no = super.findViewById(R.id.no);
        super.setCancelable(false);
        yes.setOnClickListener(view ->{
            listener.onYesClick(view);
            dismiss();
        });
        no.setOnClickListener(view ->{
            listener.onNoClick(view);
            dismiss();
        });



    }
}
