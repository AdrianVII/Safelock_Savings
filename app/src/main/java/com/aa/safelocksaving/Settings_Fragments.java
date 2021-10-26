package com.aa.safelocksaving;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.aa.safelocksaving.dataOperation.Authentication;

public class Settings_Fragments extends Fragment {
    private Authentication authentication;
    private Button btnSIGNOUT;
    private SwitchCompat switchBiometric;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        authentication = new Authentication();
        View view = inflater.inflate(R.layout.settings_fragments, container, false);
        btnSIGNOUT = view.findViewById(R.id.btnSIGNOUT);
        switchBiometric = view.findViewById(R.id.switchBiometric);
        setSwitchBiometric();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btnSIGNOUT.setOnClickListener(view -> {
            if (authentication.logoutUser()) {
                startActivity(new Intent(getActivity(), Start_Activity.class));
                getActivity().finish();
            }
        });
        switchBiometric.setOnCheckedChangeListener((buttonView, isChecked) -> {
            controlBiometric(isChecked);
        });
    }

    private void setSwitchBiometric() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Biometric", Context.MODE_PRIVATE);
        switchBiometric.setChecked(sharedPreferences.getBoolean("biometric", false));
    }

    private void controlBiometric(boolean checked) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Biometric", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("biometric", checked);
        editor.apply();
    }

}
