package com.aa.safelocksaving.DAO;

import com.aa.safelocksaving.data.Reminders_CardData;
import com.aa.safelocksaving.data.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

public class DAOUser {
    private DatabaseReference databaseReference;

    public DAOUser() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
    }

    public Task<Void> add(User user, String userID) {
        return databaseReference.child(userID).setValue(user);
    }

    public Task<Void> update(HashMap<String, Object> user, String userID) {
        return databaseReference.child(userID).updateChildren(user);
    }

    public Task<Void> addCards(String userID, Reminders_CardData reminders_cardData, String ID) {
        return databaseReference.child(userID).child("reminders").child("cards").child(ID).setValue(reminders_cardData);
    }

    public DatabaseReference get(String userID) {
        return databaseReference.child(userID);
    }

    public Task<Void> remove(String userID) {
        return databaseReference.child(userID).removeValue();
    }

}
