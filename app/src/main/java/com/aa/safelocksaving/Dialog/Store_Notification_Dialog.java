package com.aa.safelocksaving.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.aa.safelocksaving.R;

import soup.neumorphism.NeumorphButton;

public class Store_Notification_Dialog extends Dialog {
    private NeumorphButton yes;
    private NeumorphButton no;
    private TextView name;
    private TextView amount;

    public interface onButtonClickListener {
        void OnYesClick(View view);
        void OnNoClick(View view);
    }

    public Store_Notification_Dialog(@NonNull Context context,onButtonClickListener listener, String nameText, double amountText) {
        super(context);
        setContentView(R.layout.store_notification_dialog);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Window window = getWindow();
        window.setGravity(Gravity.CENTER);
        yes = findViewById(R.id.yes);
        no = findViewById(R.id.no);
        yes.setOnClickListener(view -> {
            listener.OnYesClick(view);
            dismiss();
        });
        no.setOnClickListener(view -> {
            listener.OnNoClick(view);
            dismiss();
        });
        name = findViewById(R.id.name);
        amount = findViewById(R.id.amount);
        name.setText(nameText);
        amount.setText(String.valueOf(amountText));
    }
}
