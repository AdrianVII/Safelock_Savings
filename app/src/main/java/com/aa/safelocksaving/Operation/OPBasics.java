package com.aa.safelocksaving.Operation;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.CountDownTimer;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.aa.safelocksaving.DAO.DAOPicture;
import com.aa.safelocksaving.DAO.DAOUser;
import com.aa.safelocksaving.DAO.DAOUserData;
import com.aa.safelocksaving.Dialog.Progress_Alert_Dialog;
import com.aa.safelocksaving.R;
import com.aa.safelocksaving.data.Reminders_CardData;
import com.aa.safelocksaving.data.User;
import com.aa.safelocksaving.data.UserData;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class OPBasics {
    private FirebaseUser user;
    private StorageReference storageReference;
    private Progress_Alert_Dialog progressAlertDialog;
    private MyCountDownTimer countDownTimer;
    private final long Timer = 1000 * 30;

    //Interfaces
    public interface getDataListener {
        void getUser(User user);
        void getError(DatabaseError error);
    }
    public interface getPictureListener {
        void getPicture(Bitmap bitmap);
    }
    public interface deleteUserListener {
        void deleteUser(Task<Void> task);
    }
    public interface uploadPictureListener {
        void uploadPicture(Task<UploadTask.TaskSnapshot> task);
        //void timeOutPicture();
    }

    public OPBasics () {
        user = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();
        countDownTimer = new MyCountDownTimer(Timer, 1000);
    }

    public void getUserData(getDataListener listener) {
        new DAOUser().get(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listener.getUser(snapshot.getValue(User.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.getError(error);
            }
        });
    }

    public void getPicture(getPictureListener listener) {
        new DAOPicture().get(user.getUid()).addOnSuccessListener(bytes -> listener.getPicture(BitmapFactory.decodeByteArray(bytes, 0, bytes.length)));
    }

    public void uploadPicture(Uri img, uploadPictureListener listener, Activity activity) {
        progressAlertDialog = new Progress_Alert_Dialog(activity);
        progressAlertDialog.start();
        countDownTimer.setMessage(activity.getString(R.string.connectionCouldNotBeEstablishedText), activity);
        countDownTimer.start();
        new DAOPicture().add(user.getUid(), img).addOnCompleteListener(task -> {
            countDownTimer.cancel();
            progressAlertDialog.dismiss();
            listener.uploadPicture(task);
        });
    }

    public Task<Void> reAuthenticate(Activity activity) {
        return user.reauthenticate(EmailAuthProvider.getCredential(new DAOUserData(activity).get(UserData.Email, ""), new DAOUserData(activity).get(UserData.Password, "")));
    }

    public Task<Void> addCard(Reminders_CardData reminders_cardData, String ID) { return new DAOUser().addCards(user.getUid(), reminders_cardData, ID); }

    public DatabaseReference getCardsReminders() { return new DAOUser().get(user.getUid()).child("reminders"); }

    public void deleteUser(Activity activity, deleteUserListener listener) {
        reAuthenticate(activity).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                new DAOUser().remove(user.getUid()).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        new DAOPicture().remove(user.getUid()).addOnCompleteListener(task2 -> {
                            if (task2.isSuccessful()) {
                                user.delete().addOnCompleteListener(listener::deleteUser);
                            }
                        });
                    }
                });
            }
        });
    }

    class MyCountDownTimer extends CountDownTimer {
        String message;
        Activity activity;
        public MyCountDownTimer(long startTime, long interval) { super(startTime, interval); }
        public void setMessage(String message,Activity activity) {
            this.message = message;
            this.activity = activity;
        }
        @Override
        public void onTick(long millisUntilFinished) { }
        @Override
        public void onFinish() {
            progressAlertDialog.dismiss();
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
        }
    }

}
