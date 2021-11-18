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
    private Activity activity;

    private interface onButtonClickListener {
        void onYesClick(View view);

        void onNoClick(View view);

        void onAcceptClick(View view);

        void onCancelClick(View view);
    }

    public Dialog_Shop_Store_Notification(@NonNull Activity activity, onButtonClickListener listener) {
        super(activity);
        this.activity = activity;
        super.setContentView(R.layout.subcription_notification_dialog);
        super.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Window window = super.getWindow();
        window.setGravity(Gravity.CENTER);
        yes = super.findViewById(R.id.yes);
        no = super.findViewById(R.id.no);
        accept = super.findViewById(R.id.accept);
        cancel = super.findViewById(R.id.cancel);
        title = super.findViewById(R.id.title);
        secondTitle = super.findViewById(R.id.secondTitle);
        titlePayment = super.findViewById(R.id.titlePayment);
        paymentText = super.findViewById(R.id.paymentText);
        spinner = super.findViewById(R.id.Spinner);
        editTextAmount = super.findViewById(R.id.EditAddAmount);
        yes.setOnClickListener(view -> {
            listener.onYesClick(view);
            dismiss();
        });
        no.setOnClickListener(view -> {
            listener.onNoClick(view);
            dismiss();
        });
        accept.setOnClickListener(view -> {
            listener.onAcceptClick(view);
            dismiss();
        });
        cancel.setOnClickListener(view -> {
            listener.onCancelClick(view);
            dismiss();
        });

    }
}
