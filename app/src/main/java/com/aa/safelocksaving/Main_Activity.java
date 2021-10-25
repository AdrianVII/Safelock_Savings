package com.aa.safelocksaving;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationBarView;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class Main_Activity extends AppCompatActivity {
    private ChipNavigationBar chipNavigationBarMain;
    private Reminders_Fragments reminders_fragments = new Reminders_Fragments();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        chipNavigationBarMain = findViewById(R.id.chipNavigationBarMain);
        loadFragments(reminders_fragments);
        //chipNavigationBarMain.setItemSelected(R.id.menuReminders, true);
        chipNavigationBarMain.setOnItemSelectedListener(item -> {
            switch (item) {
                case R.id.menuReminders: loadFragments(reminders_fragments); break;
                case R.id.menuBudgets: Toast.makeText(getBaseContext(), "Presupuestos", Toast.LENGTH_SHORT).show(); break;
                case R.id.menuReport: Toast.makeText(getBaseContext(), "Reportes", Toast.LENGTH_SHORT).show(); break;
                case R.id.menuSettings: Toast.makeText(getBaseContext(), "Ajustes", Toast.LENGTH_SHORT).show(); break;
            }
        });
    }

    public void loadFragments(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, fragment);
        transaction.commit();
    }

}