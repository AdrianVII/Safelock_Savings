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

import com.aa.safelocksaving.Dialog.Dialog_Important;
import com.aa.safelocksaving.Dialog.Dialog_Upload;
import com.aa.safelocksaving.Operation.CheckData;
import com.aa.safelocksaving.Operation.Date_Picker;
import com.aa.safelocksaving.Operation.ImportantColor;
import com.aa.safelocksaving.Operation.OPBasics;
import com.aa.safelocksaving.data.CardItem;
import com.aa.safelocksaving.data.DateBasic;
import com.aa.safelocksaving.data.Reminders_ShopData;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;

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
    private boolean edit = false;
    private Dialog_Upload upload;

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
        upload = new Dialog_Upload(requireActivity());
        color = 0;
        if (getArguments() != null) setAllData();
        return view;
    }

    private void setAllData() {
        Calendar calendar = Calendar.getInstance();
        edit = true;
        Bundle bundle = getArguments();
        nameEdit.setText(bundle.getString("name"));
        amountEdit.setText(String.valueOf(bundle.getDouble("amount")));
        int dayI = bundle.getInt("dayCutoffDate");
        int monthI = bundle.getInt("monthCutoffDate");
        int yearI = bundle.getInt("yearCutoffDate");
        calendar.set(yearI, monthI, dayI);
        cutoffDate = new DateBasic(dayI, monthI, yearI);
        cutoffDateText.setText(DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime()));
        dayI = bundle.getInt("day");
        monthI = bundle.getInt("month");
        yearI = bundle.getInt("year");
        calendar.set(yearI, monthI, dayI);
        deadlineDate = new DateBasic(dayI, monthI, yearI);
        deadlineText.setText(DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime()));
        descriptionEdit.setText(bundle.getString("description"));
        setColor(bundle.getInt("importance"));
        monthEdit.setText(String.valueOf(bundle.getInt("months")));
    }

    @Override
    public void onStart() {
        super.onStart();
        cutoffDateText.setOnClickListener(view -> new Date_Picker(cutoffDateText, getContext(), (day, month, year) -> cutoffDate = new DateBasic(day, month, year)));
        deadlineText.setOnClickListener(view -> new Date_Picker(deadlineText, getContext(), (day, month, year) -> deadlineDate = new DateBasic(day, month, year)));
        importanceLayout.setOnClickListener(view -> new Dialog_Important(requireActivity(), this::setColor).show());
        amountEdit.setOnKeyListener((view, i, keyEvent) -> {
            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {
                new Date_Picker(cutoffDateText, getContext(), (day, month, year) -> cutoffDate = new DateBasic(day, month, year));
                ((InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(amountEdit.getWindowToken(), 0);
                return true;
            }
            return false;
        });
        /*descriptionEdit.setOnKeyListener((view, i, keyEvent) -> {
            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {
                new Dialog_Important(requireActivity(), this::setColor).show();
                ((InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(descriptionEdit.getWindowToken(), 0);
                return true;
            }
            return false;
        });*/
        monthEdit.setOnKeyListener((view, i, keyEvent) -> {
            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {
                if (edit) upgrade(getArguments().getLong("id"));
                else upload();
                return true;
            }
            return false;
        });
        btnNext.setOnClickListener(view -> {
            if (edit) upgrade(getArguments().getLong("id"));
            else upload();
        });
    }

    private void setColor(int color) {
        this.color = color;
        new ImportantColor(getActivity(), importantColor, color);
    }

    private void upgrade(long ID) {
        if (new CheckData(getActivity()).isStoreCorrect(nameEdit, amountEdit, deadlineText, color, cutoffDateText, descriptionEdit, monthEdit)) {
            upload.start();
            HashMap<String, Object> card = new HashMap<>();
            card.put("name", nameEdit.getText().toString());
            card.put("amount", Double.parseDouble(amountEdit.getText().toString()));
            card.put("description", descriptionEdit.getText().toString());
            card.put("importance", color);
            card.put("month", Integer.parseInt(monthEdit.getText().toString()));
            new OPBasics().updateCard(ID, card).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    HashMap<String, Object> date = new HashMap<>();
                    date.put("day", cutoffDate.getDay());
                    date.put("month", cutoffDate.getMonth());
                    date.put("year", cutoffDate.getYear());
                    new OPBasics().updateDate(ID, "cutoffDate", date).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            HashMap<String, Object> dateD = new HashMap<>();
                            dateD.put("day", deadlineDate.getDay());
                            dateD.put("month", deadlineDate.getMonth());
                            dateD.put("year", deadlineDate.getYear());
                            new OPBasics().updateDate(ID, "deadline", dateD).addOnCompleteListener(task2 -> {
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
        if (new CheckData(getActivity()).isStoreCorrect(nameEdit, amountEdit, deadlineText, color, cutoffDateText, descriptionEdit, monthEdit)) {
            upload.start();
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
                                    Integer.parseInt(monthEdit.getText().toString().trim())

                            ),
                            1
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
        outState.putString("name", nameEdit.getText().toString());
        outState.putString("amount", amountEdit.getText().toString());
        outState.putString("des", descriptionEdit.getText().toString());
        outState.putString("month", monthEdit.getText().toString());

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            nameEdit.setText(savedInstanceState.getString("name"));
            amountEdit.setText(savedInstanceState.getString("amount"));
            descriptionEdit.setText(savedInstanceState.getString("des"));
            monthEdit.setText(savedInstanceState.getString("month"));
        }
    }
}