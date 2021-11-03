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
import com.aa.safelocksaving.Operation.CheckData;
import com.aa.safelocksaving.Operation.Date_Picker;
import com.aa.safelocksaving.Operation.OPBasics;
import com.aa.safelocksaving.data.CardItem;
import com.aa.safelocksaving.data.DateBasic;
import com.aa.safelocksaving.data.Reminders_ShopData;

public class New_Reminders_Store_Fragment extends Fragment {
    private EditText nameEdit;
    private EditText amountEdit;
    private TextView cutoffDateText;
    private TextView deadlineText;
    private EditText descriptionEdit;
    private LinearLayout importanceLayout;
    private EditText monthEdit;
    private Button btnNext;
    private View importantColor;
    private int color;
    private DateBasic cutoffDate;
    private DateBasic deadlineDate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_reminders_store_fragments, container, false);
        nameEdit = view.findViewById(R.id.nameEdit);
        amountEdit = view.findViewById(R.id.amountEdit);
        cutoffDateText = view.findViewById(R.id.cutoffDateText);
        deadlineText = view.findViewById(R.id.deadlineText);
        descriptionEdit = view.findViewById(R.id.descriptionEdit);
        importanceLayout = view.findViewById(R.id.importanceLayout);
        monthEdit = view.findViewById(R.id.monthEdit);
        btnNext = view.findViewById(R.id.btnNEXT);
        importantColor = view.findViewById(R.id.importantColor);
        color = 0;
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        cutoffDateText.setOnClickListener(view -> cutoffDate = new Date_Picker(cutoffDateText, getContext()).getDate());
        deadlineText.setOnClickListener(view -> deadlineDate = new Date_Picker(deadlineText, getContext()).getDate());
        importanceLayout.setOnClickListener(view -> new Dialog_Important(getActivity(), color -> {
            this.color = color;
            switch (color) {
                case 0: importantColor.setBackgroundResource(R.drawable.box_important_unselected); break;
                case 1: importantColor.setBackgroundResource(R.drawable.box_important_less); break;
                case 2: importantColor.setBackgroundResource(R.drawable.box_important_important); break;
                case 3: importantColor.setBackgroundResource(R.drawable.box_important_very); break;
            }
        }).show());
        btnNext.setOnClickListener(view -> upload());
    }

    private void upload() {
        if (new CheckData(getActivity()).isStoreCorrect(nameEdit,amountEdit, deadlineText, color, cutoffDateText, descriptionEdit, monthEdit)) {
            long ID = System.currentTimeMillis();
            new OPBasics().addRemindersCards(
                    new CardItem(
                            2,
                            new Reminders_ShopData(
                                    ID,
                                    nameEdit.getText().toString().trim(),
                                    Double.parseDouble(amountEdit.getText().toString().trim()),
                                    cutoffDate,
                                    deadlineDate,
                                    descriptionEdit.getText().toString().trim(),
                                    color,
                                    Integer.parseInt(monthEdit.getText().toString().trim()),
                                    1
                            )
                    ),
                    String.valueOf(ID)
            ).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), getString(R.string.newCardHasBeenAddedText), Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
            });
        }
    }

}
