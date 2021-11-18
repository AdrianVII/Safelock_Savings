package com.aa.safelocksaving.DAO;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import androidx.core.os.ConfigurationCompat;

import com.aa.safelocksaving.data.ConfigurationData;

import java.util.Locale;

public class DAOConfigurationData {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Activity activity;

    public DAOConfigurationData(Activity activity) {
        this.activity = activity;
        sharedPreferences = activity.getSharedPreferences(ConfigurationData.Configuration, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public DAOConfigurationData(Context context) {
        sharedPreferences = context.getSharedPreferences(ConfigurationData.Configuration, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setLanguage() {
        if (!sharedPreferences.contains(ConfigurationData.Language)) {
            String lang = ConfigurationCompat.getLocales(Resources.getSystem().getConfiguration()).toLanguageTags();
            String[] parts = lang.split("-");
            lang = parts[0];
            editor.putString(ConfigurationData.Language, lang).apply();
        }
        else {
            Locale locale = new Locale(sharedPreferences.getString(ConfigurationData.Language, "en"));
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            activity.getBaseContext().getResources().updateConfiguration(config, activity.getBaseContext().getResources().getDisplayMetrics());
        }
    }

    public boolean verifyBiometric() { return sharedPreferences.getBoolean(ConfigurationData.Biometric, false); }

    public boolean verifyNightMode() { return sharedPreferences.getBoolean(ConfigurationData.NightMode, false); }

    public String getLanguage() { return sharedPreferences.getString(ConfigurationData.Language, "en"); }

    public void updateLanguage(String data) { editor.putString(ConfigurationData.Language, data).apply(); }

    public void updateBiometric(boolean data) { editor.putBoolean(ConfigurationData.Biometric, data).apply(); }

    public void updateNightMode(boolean data) { editor.putBoolean(ConfigurationData.NightMode, data).apply(); }

    public boolean getBiometric() { return sharedPreferences.getBoolean(ConfigurationData.Biometric,false); }

    public boolean getNightMode() { return sharedPreferences.getBoolean(ConfigurationData.NightMode,false); }

}
