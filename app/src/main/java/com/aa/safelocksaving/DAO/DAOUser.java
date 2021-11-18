package com.aa.safelocksaving.DAO;

import com.aa.safelocksaving.data.Budgets_Data;
import com.aa.safelocksaving.data.CardItem;
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

    public Task<Void> add(User user, String userID) { return databaseReference.child(userID).setValue(user); }

    public Task<Void> update(HashMap<String, Object> user, String userID) { return databaseReference.child(userID).updateChildren(user); }

    public Task<Void> updateStatus(String userID, String ID, HashMap<String, Object> Status) { return databaseReference.child(userID).child("reminders").child(ID).updateChildren(Status); }

    public Task<Void> addRemindersCards(String userID, CardItem cardItem, String ID) { return databaseReference.child(userID).child("reminders").child(ID).setValue(cardItem); }

    public Task<Void> addBudgetsCards(String userID, Budgets_Data budgetsData, String ID) { return databaseReference.child(userID).child("budgets").child(ID).setValue(budgetsData); }

    public Task<Void> updateBudgetsCard(String userID, String ID, HashMap<String, Object> budget) { return databaseReference.child(userID).child("budgets").child(ID).updateChildren(budget); }

    public Task<Void> removeBudget(String userID, String ID) { return databaseReference.child(userID).child("budgets").child(ID).removeValue(); }

    public Task<Void> addAllBudgetsCards(String userID, List<Budgets_Data> budgetsDataList) { return databaseReference.child(userID).child("budgets").setValue(budgetsDataList); }

    public DatabaseReference get(String userID) {
        return databaseReference.child(userID);
    }

    public Task<Void> remove(String userID) { return databaseReference.child(userID).removeValue(); }

    public Task<Void> removeAllBudgets(String userID) { return databaseReference.child(userID).child("budgets").removeValue(); }

    public Task<Void> updateCard(String userID, String ID, HashMap<String, Object> card) { return databaseReference.child(userID).child("reminders").child(ID).child("item").updateChildren(card); }

    public Task<Void> updateDate(String userID, String ID, String dateID, HashMap<String, Object> card) { return databaseReference.child(userID).child("reminders").child(ID).child("item").child(dateID).updateChildren(card); }

}

