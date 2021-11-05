package com.aa.safelocksaving.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.aa.safelocksaving.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class Dialog_Change_Password extends Dialog {
    private TextView Title;
    private Button sendEmail;
    private Button Cancel;
    private Activity activity;
    public interface OnOkListener { void OnOk(); }

    public Dialog_Change_Password(@NonNull Activity activity, OnOkListener listener) {
        super(activity);
        this.activity = activity;
        super.setContentView(R.layout.dialog_change_password);
        super.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Window window = super.getWindow();
        window.setGravity(Gravity.CENTER);
        window.getAttributes().windowAnimations = R.anim.displacement_up;
        Title = super.findViewById(R.id.Title);
        sendEmail = super.findViewById(R.id.sendEmail);
        Cancel = super.findViewById(R.id.Cancel);
        super.setCancelable(false);
        sendEmail.setOnClickListener(view -> {
            FirebaseAuth.getInstance().sendPasswordResetEmail(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser().getEmail())).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    dismiss();
                    listener.OnOk();
                }
            });
        });
        Cancel.setOnClickListener(view -> {
            dismiss();
        });
    }
}
