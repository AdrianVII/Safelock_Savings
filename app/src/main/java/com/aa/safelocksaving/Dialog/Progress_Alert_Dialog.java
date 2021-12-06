package com.aa.safelocksaving.Dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;

import com.aa.safelocksaving.R;

public class Progress_Alert_Dialog {
    private Activity activity;
    private Context context;
    private AlertDialog alertDialog;

    //public Progress_Alert_Dialog(Activity activity) { this.activity = activity; }

    public Progress_Alert_Dialog(Context context) { this.context = context; }

    @SuppressLint("InflateParams")
    public void start() {
        //alertDialog = new AlertDialog.Builder(activity)
        alertDialog = new AlertDialog.Builder(context)
                //.setView(activity.getLayoutInflater().inflate(R.layout.progress_alert_dialog, null))
                .setView(((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                        R.layout.progress_alert_dialog,
                        null
                ))
                .setCancelable(false).create();
        alertDialog.show();
    }
    public void dismiss() { alertDialog.dismiss(); }
}
