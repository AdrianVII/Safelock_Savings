package com.aa.safelocksaving.DAO;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import java.util.Map;
import java.util.Random;

public class DAOAlarm {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String AlarmList = "Alarms";

    public DAOAlarm (@NonNull Context context) {
        sharedPreferences = context.getSharedPreferences(AlarmList, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public boolean setAlarm(long id, int requestCode) {
        if (!existAlarm(id)) {
            editor.putInt(String.valueOf(id),requestCode).apply();
            return true;
        } else return false;
    }

    public int getRequestCode(long id) {
        int requestCode = 0;
        if (!existAlarm(id)) {
            while (checkRequestCode(requestCode) || requestCode == 0)
                requestCode = new Random().nextInt(1000 - 1) + 1000;
        }
        return requestCode;
    }

    private boolean checkRequestCode(int requestCode) {
        Map<String,  ?> alarms = getAll();
        boolean exist = false;
        for (Map.Entry<String, ?> alarm : alarms.entrySet())
            exist = Integer.parseInt(alarm.getValue().toString()) == requestCode;
        return exist;
    }

    public int getAlarm(long id) { return sharedPreferences.getInt(String.valueOf(id), 0); }

    public boolean existAlarm(long id) { return sharedPreferences.contains(String.valueOf(id)); }

    public Map<String, ?> getAll() { return sharedPreferences.getAll(); }

    public void removeAlarm(long id) { editor.remove(String.valueOf(id)).apply(); }
}
