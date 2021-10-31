package com.aa.safelocksaving;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aa.safelocksaving.DAO.DAOUser;
import com.aa.safelocksaving.DAO.DAOUserData;
import com.aa.safelocksaving.Dialog.Progress_Alert_Dialog;
import com.aa.safelocksaving.data.DataUser;
import com.aa.safelocksaving.data.Reminders_CardData;
import com.aa.safelocksaving.data.User;
import com.aa.safelocksaving.operation.CheckData;
import com.aa.safelocksaving.operation.DatePicker;
import com.aa.safelocksaving.operation.OPBasics;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class New_Reminders_Cards_Fragment extends Fragment {
    private EditText name;
    private EditText amount;
    private EditText minAmount;
    private EditText settlement;
    private TextView cutoff;
    private TextView deadline;
    private LinearLayout important;
    private EditText month;
    private Button btnNext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_reminders_cards_fragment, container, false);
        name = view.findViewById(R.id.name);
        amount = view.findViewById(R.id.amount);
        minAmount = view.findViewById(R.id.miniAmount);
        settlement = view.findViewById(R.id.settlement);
        cutoff = view.findViewById(R.id.cutoff);
        deadline = view.findViewById(R.id.deadline);
        important = view.findViewById(R.id.important);
        month = view.findViewById(R.id.month);
        btnNext = view.findViewById(R.id.btnNEXT);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        cutoff.setOnClickListener(view -> {
            new DatePicker(cutoff, getContext());
        });
        deadline.setOnClickListener(view -> {
            new DatePicker(deadline, getContext());

        });
        important.setOnClickListener(view -> {

        });
        btnNext.setOnClickListener(view -> upload());
    }

    private void upload() {
        if (new CheckData(getActivity()).isCardCorrect(name, amount, deadline, 1, minAmount, settlement, cutoff, month)) {
            String nameText = name.getText().toString().trim();
            double amountText = Double.parseDouble(amount.getText().toString().trim());
            String deadlineText = deadline.getText().toString().trim();
            double minAmountText = Double.parseDouble(minAmount.getText().toString().trim());
            double SettlementText = Double.parseDouble(settlement.getText().toString().trim());
            String cutoffText = cutoff.getText().toString().trim();
            int monthText = Integer.parseInt(month.getText().toString().trim());
            Reminders_CardData reminders_cardData = new Reminders_CardData(nameText, amountText, minAmountText, SettlementText, cutoffText, deadlineText, 0, monthText);
            new OPBasics().addCard(reminders_cardData, String.valueOf(System.currentTimeMillis())).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), getString(R.string.newCardHasBeenAddedText), Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
            });
        }
    }

}
