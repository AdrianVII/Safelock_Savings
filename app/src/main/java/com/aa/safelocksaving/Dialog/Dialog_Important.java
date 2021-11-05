package com.aa.safelocksaving.Dialog;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;

import androidx.annotation.NonNull;

import com.aa.safelocksaving.R;

public class Dialog_Important extends Dialog {
    private Button btnYes, btnNo;
    private RadioButton r1,r2,r3;
    private Activity activity;
    public interface OnPositiveClickListener {
        void positiveClick(int color);
    }
    public Dialog_Important(@NonNull Activity activity, OnPositiveClickListener listener) {
        super(activity);
        this.activity = activity;
        super.setContentView(R.layout.notification_color);
        super.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Window window = super.getWindow();
        window.setGravity(Gravity.CENTER);
        window.getAttributes().windowAnimations = R.anim.displacement_up;
        btnYes = super.findViewById(R.id.btnyes);
        btnNo = super.findViewById(R.id.btnno);
        r1 = super.findViewById(R.id.rdblessImpo);
        r2 = super.findViewById(R.id.rdbImportant);
        r3 = super.findViewById(R.id.rdbVeryImpo);
        super.setCancelable(false);
        window.setLayout(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        btnYes.setOnClickListener(view -> {
            int color = 0;
            if (r1.isChecked()) color = 1;
            else if (r2.isChecked()) color = 2;
            else if (r3.isChecked()) color = 3;
            listener.positiveClick(color);
            super.dismiss();
        });
        btnNo.setOnClickListener(view -> super.dismiss());
    }
}
