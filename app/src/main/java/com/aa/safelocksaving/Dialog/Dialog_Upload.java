package com.aa.safelocksaving.Dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;

import com.aa.safelocksaving.R;

public class Dialog_Upload {
    private Activity activity;
    private Context context;
    private AlertDialog alertDialog;

    //public Dialog_Upload(Activity activity) { this.activity = activity; }

    public Dialog_Upload(Context context) { this.context = context; }


    @SuppressLint("InflateParams")
    public void start() {
        alertDialog = new AlertDialog.Builder(context)
                //.setView(activity.getLayoutInflater().inflate(R.layout.dialog_upload, null))
                .setView(((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.dialog_upload, null))
                .setCancelable(false).create();
        alertDialog.show();
    }
    public void dismiss() { alertDialog.dismiss(); }
}
