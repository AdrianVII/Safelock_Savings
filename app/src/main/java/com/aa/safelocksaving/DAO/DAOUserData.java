package com.aa.safelocksaving.DAO;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.aa.safelocksaving.data.User;
import com.aa.safelocksaving.data.UserData;

public class DAOUserData {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public DAOUserData(Context activity) {
        sharedPreferences = activity.getSharedPreferences(UserData.UserData, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void add(String key, String data) {
        editor.putString(key, data);
        editor.apply();
    }

    public void add(User data) {
        editor.putString(UserData.Name, data.getName());
        editor.putString(UserData.LastName, data.getLastname());
        editor.putString(UserData.Email, data.getEmail());
        editor.putString(UserData.Password, data.getPassword());
        editor.apply();
    }

    public void add(String name, String lastname, String picture, String email, String password) {
        editor.putString(UserData.Name, name);
        editor.putString(UserData.LastName, lastname);
        editor.putString(UserData.Picture, picture);
        editor.putString(UserData.Email, email);
        editor.putString(UserData.Password, password);
        editor.apply();
    }

    public String get(String key, String defValue) {
        return sharedPreferences.getString(key, defValue);
    }

    public String getFullName() { return String.format("%s %s", sharedPreferences.getString(UserData.Name, ""), sharedPreferences.getString(UserData.LastName, "")); }

    public void remove(String key) {
        editor.putString(key, "");
        editor.apply();
    }

    public void removeAll() {
        editor.putString(UserData.Name, "");
        editor.putString(UserData.LastName, "");
        editor.putString(UserData.Picture, "");
        editor.putString(UserData.Email, "");
        editor.putString(UserData.Password, "");
        editor.apply();
    }

}
