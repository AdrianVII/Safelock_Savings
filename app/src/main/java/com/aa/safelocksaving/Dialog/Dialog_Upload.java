package com.aa.safelocksaving.Dialog;

import android.app.Activity;
import android.app.AlertDialog;

import com.aa.safelocksaving.R;

public class Dialog_Upload {
    private Activity activity;
    private AlertDialog alertDialog;

    public Dialog_Upload(Activity activity) { this.activity = activity; }
    public void start() {
        alertDialog = new AlertDialog.Builder(activity)
                .setView(activity.getLayoutInflater().inflate(R.layout.dialog_upload, null))
                .setCancelable(false).create();
        alertDialog.show();
    }
    public void dismiss() { alertDialog.dismiss(); }
}
