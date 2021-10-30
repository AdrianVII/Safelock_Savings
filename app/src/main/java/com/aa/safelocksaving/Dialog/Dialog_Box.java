package com.aa.safelocksaving.Dialog;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.aa.safelocksaving.R;

public class Dialog_Box extends Dialog {
    private Button btnYes, btnNo;
    private TextView textTitle, textMessage;
    private Activity activity;
    public interface OnPositiveClickListener {
        void positiveClick(View view, Activity activity);
        void negativeClick(View view);
    }
    public Dialog_Box(@NonNull Activity activity, String title, String message) {
        super(activity);
        this.activity = activity;
        super.setContentView(R.layout.dialog_box);
        super.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Window window = super.getWindow();
        window.setGravity(Gravity.CENTER);
        window.getAttributes().windowAnimations = R.anim.desplazamiento_arriba;
        btnYes = super.findViewById(R.id.btnyes);
        btnNo = super.findViewById(R.id.btnno);
        textTitle = super.findViewById(R.id.title);
        textMessage = super.findViewById(R.id.message);
        textTitle.setText(title);
        textMessage.setText(message);
        super.setCancelable(false);
        window.setLayout(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        show();
    }

    public void OnActionButton(OnPositiveClickListener listener) {
        btnYes.setOnClickListener(view -> listener.positiveClick(view, this.activity));
        btnNo.setOnClickListener(view -> {
            super.dismiss();
            listener.negativeClick(view);
        });
        //super.show();
    }


    /*private Button btnYes, btnNo;
    private TextView textTitle, textMessage;
    private ImageView imageView;
    private String title, message;
    private Dialog dialog;

    public Dialog_Box(Activity activity, String title, String message) {
        dialog = new Dialog(activity);
        this.title = title;
        this.message = message;
    }

    public void show() {
        dialog.setContentView(R.layout.dialog_box);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.getAttributes().windowAnimations = R.anim.desplazamiento_arriba;
        btnYes = dialog.findViewById(R.id.btnyes);
        btnNo = dialog.findViewById(R.id.btnno);
        textTitle = dialog.findViewById(R.id.title);
        textMessage = dialog.findViewById(R.id.message);
        textTitle.setText(title);
        textMessage.setText(message);
        btnYes.setOnClickListener(view -> {

        });
        btnNo.setOnClickListener(view -> dialog.dismiss());
        dialog.setCancelable(false);
        window.setLayout(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }*/



}
