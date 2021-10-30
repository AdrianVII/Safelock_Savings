package com.aa.safelocksaving.Dialog;

import android.app.Activity;
import android.app.AlertDialog;

import com.aa.safelocksaving.R;

public class Progress_Alert_Dialog {
    private Activity activity;
    private AlertDialog alertDialog;

    public Progress_Alert_Dialog(Activity activity) { this.activity = activity; }
    public void start() {
        alertDialog = new AlertDialog.Builder(activity)
                .setView(activity.getLayoutInflater().inflate(R.layout.progress_alert_dialog, null))
                .setCancelable(false).create();
        alertDialog.show();
    }
    public void dismiss() { alertDialog.dismiss(); }
}
