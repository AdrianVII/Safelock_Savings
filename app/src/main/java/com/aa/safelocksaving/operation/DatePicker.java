package com.aa.safelocksaving.operation;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;

public class DatePicker {

    public DatePicker(TextView textView, Context context) {
        final Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, (datePicker, i, i1, i2) -> {
            calendar.set(i, i1, i2);
            textView.setText(DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime()));
        }, day, month, year);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

}
