package com.aa.safelocksaving.operation;

import android.app.Activity;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.aa.safelocksaving.Dialog.Dialog_Information;
import com.aa.safelocksaving.R;

public class CheckData {
    private Activity activity;

    public CheckData(Activity activity) {
        this.activity = activity;
    }

    public boolean isBaseCorrect(EditText name, EditText amount, TextView date, int important) {
        String nameText = name.getText().toString().trim();
        String amountText = amount.getText().toString().trim();
        String dateText = date.getText().toString().trim();
        if (nameText.isEmpty()) {
            name.setError(activity.getString(R.string.nameIsRequiredText));
            name.requestFocus();
            return false;
        }
        if (amountText.isEmpty()) {
            amount.setError(activity.getString(R.string.amountIsRequiredText));
            amount.requestFocus();
            return false;
        }
        if (dateText.isEmpty()) {
            date.setError(activity.getString(R.string.dateIsRequieredText));
            date.requestFocus();
            return false;
        }
        if (important == 0) {
            new Dialog_Information(activity, activity.getString(R.string.informationText), activity.getString(R.string.importanceIsNotSelectedText));
            return false;
        }
        return true;
    }

    public boolean isCardCorrect(EditText name, EditText amount, TextView date, int important, EditText minAmount, EditText settlement, TextView cutoffDate, EditText month) {
        if (!isBaseCorrect(name, amount, date, important)) return false;
        String minAmountText = minAmount.getText().toString().trim();
        String settlementText = settlement.getText().toString().trim();
        String cutoffDateText = cutoffDate.getText().toString().trim();
        String monthText = month.getText().toString().trim();
        if (minAmountText.isEmpty()) {
            minAmount.setError(activity.getString(R.string.minimumAmountIsRequiredText));
            minAmount.requestFocus();
            return false;
        }
        if (settlementText.isEmpty()) {
            settlement.setError(activity.getString(R.string.settlementIsRequiredText));
            settlement.requestFocus();
            return false;
        }
        if (cutoffDateText.isEmpty()) {
            cutoffDate.setError(activity.getString(R.string.cutoffIsRequiredText));
            cutoffDate.requestFocus();
            return false;
        }
        if (monthText.isEmpty()) {
            month.setError(activity.getString(R.string.theMonthIsRequiredText));
            month.requestFocus();
            return false;
        }
        return true;
    }

    public boolean isSubscriptionCorrect(EditText name, EditText amount, TextView date, int important) {
        return isBaseCorrect(name, amount, date, important);
    }
    public  boolean isStoreCorrect(EditText name, EditText amount, TextView date, int important,TextView cutoffDate, EditText description ,EditText month){
        if (!isBaseCorrect(name, amount, date, important)) return false;
        String cutoffDateText = cutoffDate.getText().toString().trim();
        String descriptionText = description.getText().toString().trim();
        String monthText = month.getText().toString().trim();
        if (cutoffDateText.isEmpty()) {
            cutoffDate.setError(activity.getString(R.string.cutoffIsRequiredText));
            cutoffDate.requestFocus();
            return false;
        }
        if (descriptionText.isEmpty()) {
            description.setError(activity.getString(R.string.descriptionIsRequiredText));
            description.requestFocus();
            return false;
        }
        if (monthText.isEmpty()) {
            month.setError(activity.getString(R.string.theMonthIsRequiredText));
            month.requestFocus();
            return false;
        }
        return true;
    }

}
