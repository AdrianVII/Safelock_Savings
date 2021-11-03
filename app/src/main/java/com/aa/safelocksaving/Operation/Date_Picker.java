package com.aa.safelocksaving.Operation;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.TextView;

import com.aa.safelocksaving.data.DateBasic;

import java.text.DateFormat;
import java.util.Calendar;

public class Date_Picker {
    private int day;
    private int month;
    private int year;

    public Date_Picker(TextView textView, Context context) {
        final Calendar calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, (datePicker, i, i1, i2) -> {
            calendar.set(i, i1, i2);
            textView.setText(DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime()));
        }, day, month, year);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    public DateBasic getDate() { return new DateBasic(day, month, year); }

}
