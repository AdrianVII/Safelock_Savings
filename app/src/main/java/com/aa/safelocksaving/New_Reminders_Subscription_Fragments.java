package com.aa.safelocksaving;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
import com.aa.safelocksaving.Dialog.Dialog_Upload;
import com.aa.safelocksaving.Operation.CheckData;
import com.aa.safelocksaving.Operation.Date_Picker;
import com.aa.safelocksaving.Operation.ImportantColor;
import com.aa.safelocksaving.Operation.OPBasics;
import com.aa.safelocksaving.data.CardItem;
import com.aa.safelocksaving.data.DateBasic;
import com.aa.safelocksaving.data.Reminders_SubscriptionData;
import com.aa.safelocksaving.data.Status;
import com.aa.safelocksaving.data.Type;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;

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
    private boolean edit = false;
    private Dialog_Upload upload;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            name.setText(savedInstanceState.getString("name"));
            amount.setText(savedInstanceState.getString("amount"));
        }
    }

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
        upload = new Dialog_Upload(requireActivity());
        color = 0;
        if (getArguments() != null) setAllData();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemSelected = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                itemSelected = 0;
            }
        });
        return view;
    }

    private void setAllData() {
        Calendar calendar = Calendar.getInstance();
        edit = true;
        Bundle bundle = getArguments();
        name.setText(bundle.getString("name"));
        amount.setText(String.valueOf(bundle.getDouble("amount")));
        calendar.set(bundle.getInt("yearCutoffDate"), bundle.getInt("monthCutoffDate"), bundle.getInt("dayCutoffDate"));
        Date.setText(DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime()));
        setColor(bundle.getInt("importance"));
        if (bundle.getInt("repeat") > 0) {
            repeat.setChecked(true);
            spinner.setVisibility(View.VISIBLE);
        }
        spinner.setSelection(bundle.getInt("repeat"));
    }

    @Override
    public void onStart() {
        super.onStart();
        Date.setOnClickListener(view -> new Date_Picker(Date, getContext(), (day, month, year) -> DATE = new DateBasic(day, month, year)));
        importantcolor1.setOnClickListener(view -> new Dialog_Important(requireActivity(), this::setColor).show());
        amount.setOnKeyListener((view, i, keyEvent) -> {
            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {
                new Date_Picker(Date, getContext(), (day, month, year) -> DATE = new DateBasic(day, month, year));
                ((InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(amount.getWindowToken(), 0);
                return true;
            }
            return false;
        });
        btnNEXT.setOnClickListener(view -> {
            if (edit) upgrade(getArguments().getLong("id"));
            else upload();
        });
        btnRepeat.setOnClickListener(view -> {
            repeat.setChecked(!repeat.isChecked());
            spinner.setVisibility((repeat.isChecked()) ? View.VISIBLE : View.GONE);
        });
    }

    private void setColor(int color) {
        this.color = color;
        new ImportantColor(getActivity(), importantcolor, color);
    }

    private void upgrade(long ID) {
        if (new CheckData(getActivity()).isSubscriptionCorrect(name, amount, Date, color)) {
            upload.start();
            HashMap<String, Object> card = new HashMap<>();
            card.put("name", name.getText().toString());
            card.put("amount", Double.parseDouble(amount.getText().toString()));
            card.put("importance", color);
            card.put("repeat", itemSelected);
            new OPBasics().updateCard(ID, card).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    new OPBasics().updateRemindersDate(ID, "date", DATE).addOnCompleteListener(task1 -> {
                        upload.dismiss();
                        if (task1.isSuccessful()) {
                            Toast.makeText(getContext(), getString(R.string.editedCardText), Toast.LENGTH_SHORT).show();
                            requireActivity().finish();
                        }
                    }).addOnFailureListener(e -> {
                        upload.dismiss();
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                    /*HashMap<String, Object> date = new HashMap<>();
                    date.put("day", DATE.getDay());
                    date.put("month", DATE.getMonth());
                    date.put("year", DATE.getYear());
                    new OPBasics().updateDate(ID, "date", date).addOnCompleteListener(task1 -> {
                        upload.dismiss();
                        if (task1.isSuccessful()) {
                            Toast.makeText(getContext(), getString(R.string.editedCardText), Toast.LENGTH_SHORT).show();
                            requireActivity().finish();
                        }
                    }).addOnFailureListener(e -> {
                        upload.dismiss();
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    });*/
                }
            }).addOnFailureListener(e -> {
                upload.dismiss();
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void upload() {
        if (new CheckData(getActivity()).isSubscriptionCorrect(name, amount, Date, color)) {
            upload.start();
            long ID = System.currentTimeMillis();
            new OPBasics().addRemindersCards(
                    new CardItem(
                            Type.SUBSCRIPTION,
                            new Reminders_SubscriptionData(
                                    ID,
                                    name.getText().toString().trim(),
                                    Double.parseDouble(amount.getText().toString().trim()),
                                    DATE,
                                    color,
                                    itemSelected,
                                    0,
                                    0
                            ),
                            Status.ACTIVE
                    ),
                    String.valueOf(ID)
            ).addOnCompleteListener(task -> {
                upload.dismiss();
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), getString(R.string.newCardHasBeenAddedText), Toast.LENGTH_SHORT).show();
                    requireActivity().finish();
                }
            }).addOnFailureListener(e -> {
                upload.dismiss();
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("name", name.getText().toString());
        outState.putString("amount", amount.getText().toString());
    }
}