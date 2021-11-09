package com.aa.safelocksaving;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.aa.safelocksaving.Operation.OPBasics;

import java.util.HashMap;

public class Budgets_Edit_Card extends AppCompatActivity {

    private TextView title;
    private EditText name;
    private EditText amount;
    private EditText type;
    private TextView back;
    private Button next;
    private long Id;
    private double amountDouble;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.budgets_edit_activity);
        Bundle bundle = getIntent().getExtras();
        title = findViewById(R.id.title);
        name = findViewById(R.id.name);
        amount = findViewById(R.id.amount);
        type = findViewById(R.id.Type);
        next = findViewById(R.id.btnNEXT);
        back = findViewById(R.id.btnBack);
        back.setOnClickListener(view -> onBackPressed());
        Id = bundle.getLong("id");
        title.setText(bundle.getString("name"));
        name.setText(bundle.getString("name"));
        amount.setText(bundle.getString("amount"));
        type.setText(bundle.getString("type"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        next.setOnClickListener(view -> {
            amountDouble = Double.parseDouble(amount.getText().toString());
            HashMap<String, Object> budget = new HashMap<>();
            budget.put("name", name.getText().toString());
            budget.put("type", type.getText().toString());
            budget.put("amount", amountDouble);
            new OPBasics().updateBudgetsCard(String.valueOf(Id), budget).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    finish();
                }
            });
        });
    }
}
