package com.aa.safelocksaving.data;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DAOUser {
    private DatabaseReference databaseReference;

    public DAOUser() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
    }

    public Task<Void> add(User user, String userID) {
        return databaseReference.child(userID).setValue(user);
    }

    public Task<Void> remove(String userID) {
        return databaseReference.child(userID).removeValue();
    }

}
