package com.aa.safelocksaving;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.aa.safelocksaving.data.Reminders_SubscriptionData;

public class New_Reminders_Subscription_Fragments extends Fragment {
    private EditText name;
    private EditText amount;
    private TextView Date;
    private LinearLayout importantcolor1;
    private View importantcolor;
    private CheckBox repeat;
    private LinearLayout btnRepeat;
    private Spinner spinner;
    private Button btnNEXT;
    private DateBasic DATE;
    private int color;
    private int itemSelected;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_reminders_subscription_fragment, container, false);
        name = view.findViewById(R.id.name);
        amount = view.findViewById(R.id.amount);
        Date = view.findViewById(R.id.Date);
        importantcolor = view.findViewById(R.id.importantColor);
        importantcolor1 = view.findViewById(R.id.importantColor1);
        repeat = view.findViewById(R.id.repeat);
        btnRepeat = view.findViewById(R.id.btnRepeat);
        spinner = view.findViewById(R.id.Spinner);
        btnNEXT = view.findViewById(R.id.btnNEXT);
        color = 0;
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { itemSelected = position; }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { itemSelected = 0; }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Date.setOnClickListener(view -> DATE = new Date_Picker(Date, getContext()).getDate());
        importantcolor1.setOnClickListener(view -> {
            new Dialog_Important(getActivity(),color ->{
                this.color = color;
                switch (color){
                    case 0: importantcolor.setBackgroundResource(R.drawable.box_important_unselected); break;
                    case 1: importantcolor.setBackgroundResource(R.drawable.box_important_less); break;
                    case 2: importantcolor.setBackgroundResource(R.drawable.box_important_important); break;
                    case 3: importantcolor.setBackgroundResource(R.drawable.box_important_very); break;
                }
            }).show();

        });
        btnNEXT.setOnClickListener(view -> {
            upload();
        });
        btnRepeat.setOnClickListener(view ->{
            repeat.setChecked(!repeat.isChecked());
            spinner.setVisibility((repeat.isChecked())? View.VISIBLE : View.GONE);
        });
    }
    private void upload(){
        if (new CheckData(getActivity()).isSubscriptionCorrect(name,amount,Date,color)){
            new OPBasics().addRemindersCards(
                    new CardItem(
                            1,
                            new Reminders_SubscriptionData(
                                    name.getText().toString().trim(),
                                    Double.parseDouble(amount.getText().toString().trim()),
                                    DATE,
                                    color,
                                    itemSelected
                            )
                    ),
                    String.valueOf(System.currentTimeMillis())
            ).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    Toast.makeText(getContext(), getString(R.string.newCardHasBeenAddedText), Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
            });
        }
    }
}
