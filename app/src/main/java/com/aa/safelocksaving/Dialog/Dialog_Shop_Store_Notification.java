package com.aa.safelocksaving.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.aa.safelocksaving.R;

import soup.neumorphism.NeumorphButton;

public class Dialog_Shop_Store_Notification extends Dialog {
    private TextView title;
    private TextView secondTitle;
    private TextView titlePayment;
    private TextView paymentText;
    private NeumorphButton yes;
    private NeumorphButton no;
    private NeumorphButton accept;
    private NeumorphButton cancel;
    private Spinner spinner;
    private EditText editTextAmount;

    public interface onButtonClickListener {
        void OnYesClick(View view);
        void OnNoClick(View view);
        void OnAcceptClick(View view);
        void OnCancelClick(View view);
    }

    public Dialog_Shop_Store_Notification(@NonNull Context context, onButtonClickListener listener) {
        super(context);
        setContentView(R.layout.shop_store_notification_dialog);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Window window = getWindow();
        window.setGravity(Gravity.CENTER);
        yes = findViewById(R.id.yes);
        no = findViewById(R.id.no);
        accept = findViewById(R.id.accept);
        cancel = findViewById(R.id.cancel);
        title = findViewById(R.id.title);
        secondTitle = findViewById(R.id.secondTitle);
        titlePayment = findViewById(R.id.titlePayment);
        paymentText = findViewById(R.id.paymentText);
        spinner = findViewById(R.id.Spinner);
        editTextAmount = findViewById(R.id.EditAddAmount);
        yes.setOnClickListener(view -> {
            listener.OnYesClick(view);
            dismiss();
        });
        no.setOnClickListener(view -> {
            listener.OnNoClick(view);
            dismiss();
        });
        accept.setOnClickListener(view -> {
            listener.OnAcceptClick(view);
            dismiss();
        });
        cancel.setOnClickListener(view -> {
            listener.OnCancelClick(view);
            dismiss();
        });
    }
}
