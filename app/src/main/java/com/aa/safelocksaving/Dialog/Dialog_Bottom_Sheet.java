package com.aa.safelocksaving.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.aa.safelocksaving.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class Dialog_Bottom_Sheet extends Dialog {
    private Activity activity;
    private LinearLayout Folder;
    private LinearLayout Camera;
    public interface OnClickListener{
        void OnClickFolder(View view);
        void OnClickCamera(View view);
    }

    public Dialog_Bottom_Sheet(@NonNull Activity activity, OnClickListener listener) {
        super(activity);
        this.activity = activity;
        setContentView(R.layout.bottom_sheet);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Window window = super.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.BOTTOM);
        window.getAttributes().windowAnimations = R.anim.displacement_up;
        Folder = findViewById(R.id.Folder);
        Camera = findViewById(R.id.Camera);
        setCancelable(true);
        Folder.setOnClickListener(view -> {
            listener.OnClickFolder(view);
            dismiss();
        });
        Camera.setOnClickListener(view -> {
            listener.OnClickCamera(view);
            dismiss();
        });
    }

}
