package com.aa.safelocksaving;

import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.aa.safelocksaving.DAO.DAOConfigurationData;

public class Language_Activity extends AppCompatActivity {
    private TextView btnBack;
    private Button btnAPPLY;
    private RadioButton rdbSPANISH;
    private RadioButton rdbENGLISH;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.language_activity);
        btnBack = findViewById(R.id.btnBack);
        btnAPPLY = findViewById(R.id.btnAPPLY);
        rdbSPANISH = findViewById(R.id.rdbSPANISH);
        rdbENGLISH = findViewById(R.id.rdbENGLISH);
        btnAPPLY = findViewById(R.id.btnAPPLY);
        setCheck();
        btnBack.setOnClickListener(view -> onBackPressed());
        btnAPPLY.setOnClickListener(view ->  radioButtonChecked());
    }

    private void setCheck() {
        if (new DAOConfigurationData(this).getLanguage().equals("es")) rdbSPANISH.setChecked(true);
        else if (new DAOConfigurationData(this).getLanguage().equals("en")) rdbENGLISH.setChecked(true);
    }

    private void radioButtonChecked() {
        if (rdbSPANISH.isChecked()) new DAOConfigurationData(this).updateLanguage("es");
        else if(rdbENGLISH.isChecked()) new DAOConfigurationData(this).updateLanguage("en");
        Toast.makeText(this, getString(R.string.restartTheAppText), Toast.LENGTH_SHORT).show();
    }

}
