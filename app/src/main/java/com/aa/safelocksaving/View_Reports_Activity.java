package com.aa.safelocksaving;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormatSymbols;

public class View_Reports_Activity extends AppCompatActivity {
    private TextView btnBack, title, month, year, name, amount, Date;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_reports_activity);
        btnBack = findViewById(R.id.btnBack);
        title = findViewById(R.id.title);
        month = findViewById(R.id.month);
        year = findViewById(R.id.year);
        name = findViewById(R.id.name);
        amount = findViewById(R.id.amount);
        Date = findViewById(R.id.Date);
        btnBack.setOnClickListener(view -> onBackPressed());
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            title.setText(bundle.getString("name"));
            String monthText = new DateFormatSymbols().getMonths()[bundle.getInt("month")];
            monthText = monthText.substring(0, 1).toUpperCase() + monthText.substring(1);
            month.setText(monthText);
            year.setText(String.valueOf(bundle.getInt("year")));
            name.setText(bundle.getString("name"));
            String amountText = String.format("$%.2f", bundle.getDouble("amount"));
            amount.setText(amountText);
            Date.setText(bundle.getString("date"));
        } else onBackPressed();
    }
}