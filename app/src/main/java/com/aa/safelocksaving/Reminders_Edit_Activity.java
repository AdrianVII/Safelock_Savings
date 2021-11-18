package com.aa.safelocksaving;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class Reminders_Edit_Activity extends AppCompatActivity {
    private int typeBundle;
    private TextView title;
    private ImageView menuCard;
    private TextView type;
    private Bundle allBundle;
    private TextView btnBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reminders_edit_activity);
        allBundle = getIntent().getExtras();
        title = findViewById(R.id.title);
        menuCard = findViewById(R.id.menuCard);
        type = findViewById(R.id.Type);
        btnBack = findViewById(R.id.btnBack);
        typeBundle = allBundle.getInt("type");
    }

    @Override
    protected void onResume() {
        super.onResume();
        btnBack.setOnClickListener(view -> onBackPressed());
        title.setText(allBundle.getString("name"));
        type.setText(getString(typeBundle == 0 ? R.string.cardText : typeBundle == 1 ? R.string.subscriptionText : R.string.shopText));
        switch (typeBundle) {
            case 0: loadFragments(new New_Reminders_Cards_Fragment()); break;
            case 1: loadFragments(new New_Reminders_Subscription_Fragments()); break;
            case 2: loadFragments(new New_Reminders_Store_Fragment()) ;break;
        }
    }

    private void loadFragments(Fragment fragment) {
        fragment.setArguments(allBundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, fragment);
        transaction.commit();
    }

}
