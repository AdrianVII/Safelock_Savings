package com.aa.safelocksaving.Dialog;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.aa.safelocksaving.R;

import soup.neumorphism.NeumorphButton;

public class Dialog_Update extends Dialog {
    private Context context;
    private NeumorphButton Cancel;
    private NeumorphButton Update;
    private TextView lastVersion;
    private TextView currentVersion;


    public interface onClickListener{
        void onUpdateClick(View view);
        void onCancelClick(View view);
    }

    public Dialog_Update(@NonNull Context context, String lastVersionT, String currentVersionT, onClickListener listener) {
        super(context);
        this.context= context;
        super.setContentView(R.layout.dialog_checkupdate);
        super.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Window window = super.getWindow();
        window.setGravity(Gravity.CENTER);
        window.getAttributes().windowAnimations = R.anim.displacement_up;
        Cancel = super.findViewById(R.id.Cancel);
        Update = super.findViewById(R.id.Update);
        lastVersion = super.findViewById(R.id.lastVersion);
        currentVersion = super.findViewById(R.id.currentVersion);
        super.setCancelable(false);
        window.setLayout(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        lastVersion.setText(lastVersionT);
        currentVersion.setText(currentVersionT);
        Cancel.setOnClickListener(view ->{
            listener.onCancelClick(view);
            dismiss();
        });
        Update.setOnClickListener(view ->{
            listener.onUpdateClick(view);
            //dismiss();
        });
        show();
    }
}
