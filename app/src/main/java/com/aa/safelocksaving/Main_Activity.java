package com.aa.safelocksaving;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class Main_Activity extends AppCompatActivity implements ChipNavigationBar.OnItemSelectedListener {
    private ChipNavigationBar chipNavigationBarMain;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        chipNavigationBarMain = findViewById(R.id.chipNavigationBarMain);
        loadFragments(new Reminders_Fragments(), true);
        chipNavigationBarMain.setItemSelected(R.id.menuReminders, true);
        chipNavigationBarMain.setOnItemSelectedListener(this);
    }

    private void loadFragments(Fragment fragment, boolean netChecker) {
        if (!isConnection() && netChecker) fragment = new NotConnection_Fragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, fragment);
        transaction.commit();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("idNavigation", chipNavigationBarMain.getSelectedItemId());

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        onItemSelected(savedInstanceState.getInt("idNavigation"));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onItemSelected(int i) {
        switch (i) {
            case R.id.menuReminders:
                loadFragments(new Reminders_Fragments(), true);
                break;
            case R.id.menuBudgets:
                loadFragments(new Budgets_Fragments(), true);
                break;
            case R.id.menuReport:
                loadFragments(new Reports_Fragments(), true);
                break;
            case R.id.menuSettings:
                loadFragments(new Settings_Fragments(), false);
                break;
        }
    }

    private boolean isConnection() {
        return ( ((ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE))
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .getState() == NetworkInfo.State.CONNECTED || (((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE))
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED));
    }
}