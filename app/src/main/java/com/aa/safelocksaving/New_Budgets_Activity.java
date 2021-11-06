package com.aa.safelocksaving;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.aa.safelocksaving.Operation.CheckData;
import com.aa.safelocksaving.Operation.OPBasics;
import com.aa.safelocksaving.data.Budgets_Data;

public class New_Budgets_Activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private TextView btnBack;
    private EditText editName;
    private EditText editAmount;
    private EditText editAdd;
    private Spinner spinner;
    private Button btnNext;
    private int spinnerSelected;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_budgets_activity);
        btnBack = findViewById(R.id.btnBack);
        editName = findViewById(R.id.name);
        editAmount = findViewById(R.id.amount);
        spinner = findViewById(R.id.Spinner);
        editAdd = findViewById(R.id.add);
        btnNext = findViewById(R.id.btnNEXT);
        spinnerSelected = 0;
    }

    @Override
    protected void onStart() {
        super.onStart();
        btnBack.setOnClickListener(view -> onBackPressed());
        spinner.setOnItemSelectedListener(this);
        btnNext.setOnClickListener(view -> upload());
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        spinnerSelected = i;
        if (i == 3) editAdd.setVisibility(View.VISIBLE);
        else editAdd.setVisibility(View.GONE);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void upload() {
        if (new CheckData(this).isBudgetCorrect(editName, editAmount, spinnerSelected, editAdd)) {
            long ID = System.currentTimeMillis();
            new OPBasics().addBudgetsCards(
                    new Budgets_Data(
                            ID,
                            editName.getText().toString().trim(),
                            Double.parseDouble(editAmount.getText().toString().trim()),
                            spinnerSelected == 3 ? editAdd.getText().toString().trim() : spinner.getSelectedItem().toString().trim()
                    ),
                    String.valueOf(ID)
            ).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, getString(R.string.newCardHasBeenAddedText), Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
    }
}