package com.aa.safelocksaving;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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
        loadFragments(new Reminders_Fragments());
        chipNavigationBarMain.setItemSelected(R.id.menuReminders, true);
        chipNavigationBarMain.setOnItemSelectedListener(this);
    }

    public void loadFragments(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, fragment);
        transaction.commit();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onItemSelected(int i) {
        switch (i) {
            case R.id.menuReminders:
                loadFragments(new Reminders_Fragments());
                break;
            case R.id.menuBudgets:
                loadFragments(new Budgets_Fragments());
                break;
            case R.id.menuReport:
                loadFragments(new Reports_Fragments());
                break;
            case R.id.menuSettings:
                loadFragments(new Settings_Fragments());
                break;
        }
    }
}