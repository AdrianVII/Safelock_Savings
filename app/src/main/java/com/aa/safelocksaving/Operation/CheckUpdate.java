package com.aa.safelocksaving.Operation;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.view.View;

import androidx.annotation.NonNull;

import com.aa.safelocksaving.DAO.DAOUser;
import com.aa.safelocksaving.Dialog.Dialog_Update;
import com.aa.safelocksaving.data.AppData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class CheckUpdate {
    private AppData appData;
    private StorageReference storageReference;
    private final Activity activity;

    public interface noUpdateListener {
        void noUpdate();

    }

    public CheckUpdate(Activity activity, noUpdateListener listener) {
        this.activity = activity;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("App");
        storageReference = FirebaseStorage.getInstance().getReference("update");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    appData = snapshot.getValue(AppData.class);
                    try {
                        String currentVersion = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionName;
                        int currentVersionCode = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionCode;
                        if (!currentVersion.equals(appData.getLastVersion())) {
                            if (currentVersionCode < appData.getLastVersionCode()) {
                                new Dialog_Update(activity, appData.getLastVersion(), currentVersion, new Dialog_Update.onClickListener() {
                                    @Override
                                    public void onUpdateClick(View view) {
                                        storageReference.child("SafeLock-Savings.apk").getDownloadUrl().addOnSuccessListener(uri -> {
                                            downloadUpdate(uri, appData.getLastVersion());
                                        });
                                    }

                                    @Override
                                    public void onCancelClick(View view) {
                                        activity.finish();
                                    }
                                });

                            }
                        } else {
                            listener.noUpdate();
                        }
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void downloadUpdate(Uri uri, String version) {
        DownloadManager manager = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        //request.setDestinationInExternalFilesDir(activity, Environment.DIRECTORY_DOWNLOADS, String.format("SafeLock-Savings_%s.apk", version));
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, String.format("SafeLock-Savings_%s.apk", version));
        manager.enqueue(request);
    }

}
