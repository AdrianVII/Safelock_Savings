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
    private Reminders_Fragments reminders_fragments = new Reminders_Fragments();
    private Budgets_Fragments budgets_fragments = new Budgets_Fragments();
    private Reports_Fragments reports_fragments = new Reports_Fragments();
    private Settings_Fragments  settings_fragments =  new Settings_Fragments();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        chipNavigationBarMain = findViewById(R.id.chipNavigationBarMain);
        loadFragments(reminders_fragments);
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
                loadFragments(reminders_fragments);
                break;
            case R.id.menuBudgets:
                loadFragments(budgets_fragments);
                break;
            case R.id.menuReport:
                loadFragments(reports_fragments);
                break;
            case R.id.menuSettings:
                loadFragments(settings_fragments);
                break;
        }
    }
}