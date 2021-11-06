package com.aa.safelocksaving;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class New_Reminders_Activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private TextView btnBack;
    private Spinner spinner;
    private Fragment fragmentAux;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_reminders_activity);
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(view -> onBackPressed());

        spinner = findViewById(R.id.Spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.typeReminders, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0: removeFragment(); break;
            case 1: loadFragments(new New_Reminders_Cards_Fragment()); break;
            case 2: loadFragments(new New_Reminders_Subscription_Fragments()); break;
            case 3: loadFragments(new New_Reminders_Store_Fragment()); break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void loadFragments(Fragment fragment) {
        fragmentAux = fragment;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, fragment);
        transaction.commit();
    }

    public void removeFragment() {
        if (fragmentAux != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.remove(fragmentAux);
            transaction.commit();
        }
    }
}
