package com.aa.safelocksaving.Dialog;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
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
        window.getAttributes().windowAnimations = R.anim.displacement_up;
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
    }
}
