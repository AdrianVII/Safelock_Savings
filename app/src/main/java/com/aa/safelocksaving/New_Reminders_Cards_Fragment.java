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

import com.aa.safelocksaving.Dialog.Dialog_Important;
import com.aa.safelocksaving.data.CardItem;
import com.aa.safelocksaving.data.DateBasic;
import com.aa.safelocksaving.data.Reminders_CardData;
import com.aa.safelocksaving.Operation.CheckData;
import com.aa.safelocksaving.Operation.DatePicker;
import com.aa.safelocksaving.Operation.OPBasics;

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
    private View importantColor;
    private int color;
    private DateBasic cutoffDate, deadlineDate;

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
        importantColor = view.findViewById(R.id.importantColor);
        color = 0;
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        cutoff.setOnClickListener(view -> {
            cutoffDate = new DatePicker(cutoff, getContext()).getDate();
        });
        deadline.setOnClickListener(view -> {
            deadlineDate = new DatePicker(deadline, getContext()).getDate();

        });
        important.setOnClickListener(view -> new Dialog_Important(getActivity(), color -> {
            this.color = color;
            switch (color) {
                case 0:
                    importantColor.setBackgroundResource(R.drawable.box_reminders);
                    break;
                case 1:
                    importantColor.setBackgroundColor(getContext().getColor(R.color.blue_grey_white));
                    break;
                case 2:
                    importantColor.setBackgroundColor(getContext().getColor(R.color.orange_white));
                    break;
                case 3:
                    importantColor.setBackgroundColor(getContext().getColor(R.color.orange_black));
                    break;
            }
        }).show());
        btnNext.setOnClickListener(view -> upload());
    }

    private void upload() {
        if (new CheckData(getActivity()).isCardCorrect(name, amount, deadline, color, minAmount, settlement, cutoff, month)) {
            String nameText = name.getText().toString().trim();
            double amountText = Double.parseDouble(amount.getText().toString().trim());
            double minAmountText = Double.parseDouble(minAmount.getText().toString().trim());
            double SettlementText = Double.parseDouble(settlement.getText().toString().trim());
            int monthText = Integer.parseInt(month.getText().toString().trim());
            Reminders_CardData reminders_cardData = new Reminders_CardData(nameText, amountText, minAmountText, SettlementText, cutoffDate, deadlineDate, color, monthText);
            new OPBasics().addRemindersCards(new CardItem(0, reminders_cardData), String.valueOf(System.currentTimeMillis())).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), getString(R.string.newCardHasBeenAddedText), Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
            });
        }
    }

}
