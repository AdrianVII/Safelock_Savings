package com.aa.safelocksaving;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aa.safelocksaving.DAO.DAOConfigurationData;
import com.aa.safelocksaving.Dialog.Dialog_Important;
import com.aa.safelocksaving.Dialog.Dialog_Upload;
import com.aa.safelocksaving.Operation.ImportantColor;
import com.aa.safelocksaving.data.CardItem;
import com.aa.safelocksaving.data.DateBasic;
import com.aa.safelocksaving.data.Reminders_CardData;
import com.aa.safelocksaving.Operation.CheckData;
import com.aa.safelocksaving.Operation.Date_Picker;
import com.aa.safelocksaving.Operation.OPBasics;
import com.aa.safelocksaving.data.Status;
import com.aa.safelocksaving.data.Type;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;

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
    private boolean edit = false;
    private Dialog_Upload upload;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            name.setText(savedInstanceState.getString("name"));
            amount.setText(savedInstanceState.getString("amount"));
            minAmount.setText(savedInstanceState.getString("mina"));
            settlement.setText(savedInstanceState.getString("settlement"));
            month.setText(savedInstanceState.getString("month"));
        }
    }

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
        upload = new Dialog_Upload(getActivity());
        color = 0;
        if (getArguments() != null) setAllData();
        return view;
    }

    private void setAllData() {
        Calendar calendar = Calendar.getInstance();
        edit = true;
        Bundle bundle = getArguments();
        name.setText(bundle.getString("name"));
        amount.setText(String.valueOf(bundle.getDouble("amount")));
        minAmount.setText(String.valueOf(bundle.getDouble("minAmount")));
        settlement.setText(String.valueOf(bundle.getDouble("settlement")));
        int yearI = bundle.getInt("yearCutoffDate");
        int monthI = bundle.getInt("monthCutoffDate");
        int dayI = bundle.getInt("dayCutoffDate");
        calendar.set(yearI, monthI, dayI);
        cutoffDate = new DateBasic(dayI, monthI, yearI);
        cutoff.setText(DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime()));
        yearI = bundle.getInt("year");
        monthI = bundle.getInt("month");
        dayI = bundle.getInt("day");
        calendar.set(yearI, monthI, dayI);
        deadlineDate = new DateBasic(dayI, monthI, yearI);
        deadline.setText(DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime()));
        setColor(bundle.getInt("importance"));
        month.setText(String.valueOf(bundle.getInt("months")));
    }

    @Override
    public void onResume() {
        super.onResume();
        cutoff.setOnClickListener(view -> new Date_Picker(cutoff, getContext(), (day, month, year) -> cutoffDate = new DateBasic(day, month, year)));
        deadline.setOnClickListener(view -> new Date_Picker(deadline, getContext(), (day, month, year) -> deadlineDate = new DateBasic(day, month, year)));
        important.setOnClickListener(view -> new Dialog_Important(requireActivity(), this::setColor).show());
        btnNext.setOnClickListener(view -> {
            if (edit) upgrade(getArguments().getLong("id"));
            else upload();
        });
        settlement.setOnKeyListener((view, i, keyEvent) -> {
            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {
                new Date_Picker(cutoff, getContext(), (day, month, year) -> cutoffDate = new DateBasic(day, month, year));
                ((InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(settlement.getWindowToken(), 0);
                return true;
            }
            return false;
        });
        month.setOnKeyListener((view, i, keyEvent) -> {
            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {
                if (edit) upgrade(getArguments().getLong("id"));
                else upload();
                return true;
            }
            return false;
        });
    }

    private void setColor(int color) {
        this.color = color;
        new ImportantColor(getActivity(),importantColor,color);
    }

    private void upgrade(long ID) {
        if (new CheckData(getActivity()).isCardCorrect(name, amount, deadline, color, minAmount, settlement, cutoff, month)) {
            upload.start();
            HashMap<String, Object> card = new HashMap<>();
            card.put("name", name.getText().toString());
            card.put("amount", Double.parseDouble(amount.getText().toString()));
            card.put("minAmount", Double.parseDouble(minAmount.getText().toString()));
            card.put("settlement", Double.parseDouble(settlement.getText().toString()));
            card.put("importance", color);
            card.put("month", Integer.parseInt(month.getText().toString()));
            new OPBasics().updateCard(ID, card).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    new OPBasics().updateRemindersDate(ID, "cutoffDate", cutoffDate).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            new OPBasics().updateRemindersDate(ID, "deadline", deadlineDate).addOnCompleteListener(task2 -> {
                                upload.dismiss();
                                if (task2.isSuccessful()) {
                                    Toast.makeText(getContext(), getString(R.string.editedCardText), Toast.LENGTH_SHORT).show();
                                    requireActivity().finish();
                                }
                            }).addOnFailureListener(e -> {
                                upload.dismiss();
                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                        }
                    }).addOnFailureListener(e -> {
                        upload.dismiss();
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }
            }).addOnFailureListener(e -> {
                upload.dismiss();
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void upload() {
        if (new CheckData(getActivity()).isCardCorrect(name, amount, deadline, color, minAmount, settlement, cutoff, month)) {
            upload.start();
            long ID = System.currentTimeMillis();
            new OPBasics().addRemindersCards(
                    new CardItem(
                            Type.CARD,
                            new Reminders_CardData(
                                    ID,
                                    name.getText().toString().trim(),
                                    Double.parseDouble(amount.getText().toString().trim()),
                                    Double.parseDouble(minAmount.getText().toString().trim()),
                                    Double.parseDouble(settlement.getText().toString().trim()),
                                    cutoffDate,
                                    deadlineDate,
                                    color,
                                    Integer.parseInt(month.getText().toString().trim()),
                                    0,
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
        outState.putString("mina", minAmount.getText().toString());
        outState.putString("settlement", settlement.getText().toString());
        outState.putString("month", month.getText().toString());
    }
}