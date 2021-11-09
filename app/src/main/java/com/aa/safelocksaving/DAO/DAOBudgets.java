package com.aa.safelocksaving.DAO;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

public class DAOBudgets {
    private Activity activity;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String Budget = "Budget";
    private String Amount = "Amount";

    public DAOBudgets(@NonNull Activity activity) {
        this.activity = activity;
        sharedPreferences = activity.getSharedPreferences(Budget, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setAmount(long amount) { editor.putLong(Amount, amount).apply(); }

    public long getAmount() { return sharedPreferences.getLong(Amount, 0); }

}
