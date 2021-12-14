package com.aa.safelocksaving.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.aa.safelocksaving.DAO.DAOConfigurationData;
import com.aa.safelocksaving.R;

import soup.neumorphism.NeumorphButton;

public class Card_Store_Notification extends Dialog implements AdapterView.OnItemSelectedListener {
    private TextView title;
    private TextView secondTitle;
    private TextView titlePayment;
    private TextView signo;
    private TextView paymentText;
    private NeumorphButton yes;
    private NeumorphButton no;
    private NeumorphButton accept;
    private NeumorphButton cancel;
    private Spinner spinner;
    private EditText editTextAmount;
    private EditText interestText;
    private LinearLayout group_yes_no;
    private LinearLayout interest;
    private LinearLayout group_accept_cancel;
    private double settlement;
    private double amount;
    private double minAmount;
    private double editAmount = 0;
    private double accumulatedAmount;

    private void showPayment(int visible, double quantity) {
        titlePayment.setVisibility(visible);
        signo.setVisibility(visible);
        paymentText.setVisibility(visible);
        editTextAmount.setVisibility(View.GONE);
        accept.setEnabled(visible != View.GONE);
        paymentText.setText(String.valueOf(quantity));
        interest.setVisibility(quantity == minAmount + accumulatedAmount ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0: showPayment(View.GONE, 0); break;
            case 1: showPayment(View.VISIBLE, settlement + accumulatedAmount); break;
            case 2: showPayment(View.VISIBLE, amount + accumulatedAmount); break;
            case 3: showPayment(View.VISIBLE, minAmount + accumulatedAmount); break;
            case 4:
                titlePayment.setVisibility(View.VISIBLE);
                signo.setVisibility(View.VISIBLE);
                paymentText.setVisibility(View.GONE);
                editTextAmount.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        editAmount = editTextAmount.getText().toString().isEmpty() ? 0 : Double.parseDouble(editTextAmount.getText().toString());
                        accept.setEnabled( editAmount >= minAmount );
                        interest.setVisibility( editAmount < (amount + accumulatedAmount) ? View.VISIBLE : View.GONE );
                    }
                }); //Pending...
                editTextAmount.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public interface onButtonClickListener {
        void OnYesClick(View view);
        void OnNoClick(View view);
        void OnAcceptClick(View view, double amount, double accumulated);
        void OnCancelClick(View view);
    }

    public Card_Store_Notification(@NonNull Context context, onButtonClickListener listener, double settlement, double amount, double minAmount, double accumulatedAmount) {
        super(context);
        this.settlement = settlement;
        this.amount = amount;
        this.minAmount = minAmount;
        this.accumulatedAmount = accumulatedAmount;
        setContentView(R.layout.card_notification_dialog);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Window window = getWindow();
        window.setGravity(Gravity.CENTER);
        yes = findViewById(R.id.yes);
        no = findViewById(R.id.no);
        signo = findViewById(R.id.signo);
        interest = findViewById(R.id.interest);
        group_yes_no = findViewById(R.id.group_yes_no);
        group_accept_cancel = findViewById(R.id.group_accept_cancel);
        accept = findViewById(R.id.accept);
        cancel = findViewById(R.id.cancel);
        title = findViewById(R.id.title);
        secondTitle = findViewById(R.id.secondTitle);
        titlePayment = findViewById(R.id.titlePayment);
        paymentText = findViewById(R.id.paymentText);
        editTextAmount = findViewById(R.id.EditAddAmount);
        spinner = findViewById(R.id.Spinner);
        interestText = findViewById(R.id.interestText);
        //interestText.setText("0");
        interestText.requestFocus();
        ArrayAdapter<CharSequence> adapter;
        if (!new DAOConfigurationData(context).verifyNightMode())
            adapter= ArrayAdapter.createFromResource(context, R.array.PaymentType, android.R.layout.simple_spinner_item);
        else  adapter = ArrayAdapter.createFromResource(context, R.array.PaymentType, R.layout.spinner_text_color);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        yes.setOnClickListener(view -> {
            title.setVisibility(View.GONE);
            group_yes_no.setVisibility(View.GONE);
            secondTitle.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.VISIBLE);
            group_accept_cancel.setVisibility(View.VISIBLE);
            listener.OnYesClick(view);
        });
        no.setOnClickListener(view -> {
            listener.OnNoClick(view);
            dismiss();
        });
        accept.setOnClickListener(view -> {
            listener.OnAcceptClick(
                    view,
                    spinner.getSelectedItemPosition() == 4 ? Double.parseDouble(editTextAmount.getText().toString()) : Double.parseDouble(paymentText.getText().toString()),
                    interestText.getText().toString().isEmpty() ? 0 : ((amount + accumulatedAmount) * Double.parseDouble(interestText.getText().toString()) / 100)
            );
            dismiss();
        });
        cancel.setOnClickListener(view -> {
            listener.OnCancelClick(view);
            dismiss();
        });
    }
}
