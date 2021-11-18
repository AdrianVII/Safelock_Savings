package com.aa.safelocksaving.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.aa.safelocksaving.DAO.DAOUser;
import com.aa.safelocksaving.DAO.DAOUserData;
import com.aa.safelocksaving.Operation.OPBasics;
import com.aa.safelocksaving.R;
import com.aa.safelocksaving.data.UserData;

import java.util.HashMap;

import soup.neumorphism.NeumorphButton;

public class Dialog_Change_Name extends Dialog {
    private EditText name;
    private EditText lastname;
    private NeumorphButton yes;
    private NeumorphButton no;
    private Activity activity;

    public interface OnOkListener {
        void OnOk();
    }

    public Dialog_Change_Name(@NonNull Activity activity, OnOkListener listener) {
        super(activity);
        this.activity = activity;
        setContentView(R.layout.dialog_edit_name);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Window window = getWindow();
        window.setGravity(Gravity.CENTER);
        window.getAttributes().windowAnimations = R.anim.displacement_up;
        name = findViewById(R.id.name);
        lastname = findViewById(R.id.lastname);
        yes = findViewById(R.id.yes);
        no = findViewById(R.id.no);
        name.setText(new DAOUserData(activity).get(UserData.Name, "Unknown"));
        lastname.setText(new DAOUserData(activity).get(UserData.LastName, "Unknown"));
        name.requestFocus();
        setCancelable(false);
        HashMap<String, Object> names = new HashMap<>();
        yes.setOnClickListener(view -> {
            names.put("name", name.getText().toString().trim());
            names.put("lastname", lastname.getText().toString().trim());
            new OPBasics().updateUser(names);
            new DAOUserData(activity).add(UserData.Name, name.getText().toString().trim());
            new DAOUserData(activity).add(UserData.LastName, lastname.getText().toString().trim());
            listener.OnOk();
            dismiss();
        });
        no.setOnClickListener(view -> dismiss());
    }
}
