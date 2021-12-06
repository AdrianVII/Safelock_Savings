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
import com.aa.safelocksaving.Dialog.Dialog_Update;
import com.aa.safelocksaving.Dialog.Dialog_Upload;
import com.aa.safelocksaving.Dialog.Progress_Alert_Dialog;
import com.aa.safelocksaving.R;
import com.aa.safelocksaving.data.Budgets_Data;
import com.aa.safelocksaving.data.CardItem;
import com.aa.safelocksaving.data.DateBasic;
import com.aa.safelocksaving.data.Reminders_CardData;
import com.aa.safelocksaving.data.User;
import com.aa.safelocksaving.data.UserData;
import com.google.android.gms.tasks.OnCompleteListener;
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

import java.util.HashMap;
import java.util.List;

public class OPBasics {
    private final FirebaseUser user;
    private StorageReference storageReference;
    private Dialog_Upload progressAlertDialog;
    private final MyCountDownTimer countDownTimer;
    private final long Timer = 1000 * 30;

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
        progressAlertDialog = new Dialog_Upload(activity);
        progressAlertDialog.start();
        countDownTimer.setMessage(activity.getString(R.string.connectionCouldNotBeEstablishedText), activity);
        countDownTimer.start();
        new DAOPicture().add(user.getUid(), img).addOnCompleteListener(task -> {
            countDownTimer.cancel();
            progressAlertDialog.dismiss();
            listener.uploadPicture(task);
        });
    }

    public Task<Void> updateCard(long ID, HashMap<String, Object> card) { return new DAOUser().updateCard(user.getUid(), String.valueOf(ID), card); }


    public Task<Void> reAuthenticate(Activity activity, String password) {
        return user.reauthenticate(EmailAuthProvider.getCredential(new DAOUserData(activity).get(UserData.Email, ""), password));
    }

    public Task<Void> addRemindersCards(CardItem cardItem, String ID) { return new DAOUser().addRemindersCards(user.getUid(), cardItem, ID); }

    public Task<Void> addBudgetsCards(Budgets_Data budgetsData, String ID) { return new DAOUser().addBudgetsCards(user.getUid(), budgetsData, ID); }

    public Task<Void> updateBudgetsCard(String ID, HashMap<String, Object> budget) { return new DAOUser().updateBudgetsCard(user.getUid(), ID, budget); }


    public Task<Void> removeBudgetsCard(long ID) { return new DAOUser().removeBudget(user.getUid(), String.valueOf(ID)); }

    public Task<Void> updateRemindersStatus(long ID, int Status) {
        HashMap<String, Object> status = new HashMap<>();
        status.put("status", Status);
        return new DAOUser().updateStatus(user.getUid(), String.valueOf(ID), status);
    }

    public Task<Void> updateRemindersDate(long ID, String dateID, DateBasic date) {
        HashMap<String, Object> dateObject = new HashMap<>();
        dateObject.put("day", date.getDay());
        dateObject.put("month", date.getMonth());
        dateObject.put("year", date.getYear());
        return new DAOUser().updateDate(user.getUid(), String.valueOf(ID), dateID, dateObject);
    }

    public DatabaseReference getCardsReminders() { return new DAOUser().get(user.getUid()).child("reminders"); }

    public DatabaseReference getCardsBudgets() { return new DAOUser().get(user.getUid()).child("budgets"); }

    public void deleteUser(Activity activity, deleteUserListener listener) {
        reAuthenticate(activity, new DAOUserData(activity).get(UserData.Password, "")).addOnCompleteListener(task -> {
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

    public Task<Void> removeAllBudgets() { return new DAOUser().removeAllBudgets(user.getUid()); }

    public Task<Void> updateUser(HashMap<String, Object> data) { return new DAOUser().update(data, user.getUid()); }

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



