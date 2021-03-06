package com.aa.safelocksaving;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.aa.safelocksaving.Dialog.Dialog_Box;
import com.aa.safelocksaving.data.Authentication;
import com.aa.safelocksaving.DAO.DAOConfigurationData;

public class Settings_Fragments extends Fragment implements View.OnClickListener {
    private Authentication authentication;
    private Button btnSIGNOUT;
    private SwitchCompat switchBiometric;
    private LinearLayout btnACCOUNT;
    private LinearLayout btnSWITCH;
    private LinearLayout info;
    private LinearLayout lang;
    private LinearLayout notification;
    private LinearLayout btnSWITCHNightMode;
    private SwitchCompat nightMode;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        authentication = new Authentication();
        View view = inflater.inflate(R.layout.settings_fragments, container, false);
        btnSIGNOUT = view.findViewById(R.id.btnSIGNOUT);
        switchBiometric = view.findViewById(R.id.switchBiometric);
        btnACCOUNT = view.findViewById(R.id.btnACCOUNT);
        info = view.findViewById(R.id.info);
        lang = view.findViewById(R.id.lang);
        btnSWITCH = view.findViewById(R.id.btnSWITCH);
        notification = view.findViewById(R.id.Notification);
        btnSWITCHNightMode = view.findViewById(R.id.btnSWITCHNightMode);
        nightMode = view.findViewById(R.id.switchNightMode);
        setSwitchBiometric();
        setSwitchNightMode();
        checkNightMode();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        btnACCOUNT.setOnClickListener(this);
        btnSWITCH.setOnClickListener(this);
        info.setOnClickListener(this);
        lang.setOnClickListener(this);
        btnSIGNOUT.setOnClickListener(this);
        notification.setOnClickListener(this);
        switchBiometric.setOnCheckedChangeListener((buttonView, isChecked) -> controlBiometric(isChecked));
        btnSWITCHNightMode.setOnClickListener(this);
        nightMode.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            controlNightMode(isChecked);
            setNightMode(isChecked);
        }));
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSWITCHNightMode: nightMode.setChecked(!nightMode.isChecked()); break;
            case R.id.btnACCOUNT: startActivity(new Intent(getContext(), Account_Activity.class)); break;
            case R.id.btnSWITCH: switchBiometric.setChecked(!switchBiometric.isChecked()); break;
            case R.id.info: startActivity(new Intent(getContext(), Information_Activity.class)); break;
            case R.id.lang: startActivity(new Intent(getContext(), Language_Activity.class)); break;
            case R.id.btnSIGNOUT: new Dialog_Box(requireActivity(),getString(R.string.logoutText), getString(R.string.areYouSureToLogoutText)).OnActionButton(new Dialog_Box.OnPositiveClickListener(){

                @Override
                public void positiveClick(View view, Activity activity) {
                    if (authentication.logoutUser()) {
                        startActivity(new Intent(getActivity(), Start_Activity.class));
                        requireActivity().finish();
                    }
                }

                @Override
                public void negativeClick(View view) {

                }
            });break;
            case R.id.Notification:
                Intent intent = new Intent();
                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                    intent.putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().getPackageName());
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                    intent.putExtra("app_package", requireContext().getPackageName());
                    intent.putExtra("app_uid", requireContext().getApplicationInfo().uid);
                } else {
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.setData(Uri.parse("package:" + requireContext().getPackageName()));
                }
                startActivity(intent);
        }
    }

    private void setSwitchBiometric() { switchBiometric.setChecked(new DAOConfigurationData(requireActivity()).getBiometric()); }

    private void setSwitchNightMode() { nightMode.setChecked(new DAOConfigurationData(requireActivity()).getNightMode()); }

    private void controlBiometric(boolean checked) { new DAOConfigurationData(requireActivity()).updateBiometric(checked); }

    private void controlNightMode(boolean checked) { new DAOConfigurationData(requireActivity()).updateNightMode(checked); }

    private void checkNightMode() {
        if (new DAOConfigurationData(requireActivity()).verifyNightMode())
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    private void setNightMode(boolean isChecked) {
        if (isChecked)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

}
