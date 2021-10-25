package com.aa.safelocksaving;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.navigation.NavigationBarView;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class Main_Activity extends AppCompatActivity {
    private ChipNavigationBar chipNavigationBarMain;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        chipNavigationBarMain = findViewById(R.id.chipNavigationBarMain);
        chipNavigationBarMain.setItemSelected(R.id.menuReminders, true);
        chipNavigationBarMain.setOnItemSelectedListener(item -> {
            switch (item) {
                case R.id.menuReminders: Toast.makeText(getBaseContext(), "Recordatorios", Toast.LENGTH_SHORT).show(); break;
                case R.id.menuBudgets: Toast.makeText(getBaseContext(), "Presupuestos", Toast.LENGTH_SHORT).show(); break;
                case R.id.menuReport: Toast.makeText(getBaseContext(), "Reportes", Toast.LENGTH_SHORT).show(); break;
                case R.id.menuSettings: Toast.makeText(getBaseContext(), "Ajustes", Toast.LENGTH_SHORT).show(); break;
            }
        });
    }
}