package com.aa.safelocksaving.DAO;

import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class DAOPicture {
    private StorageReference storageReference;
    private final String path = "/images/ProfilePicture.jpg";

    public DAOPicture() { storageReference = FirebaseStorage.getInstance().getReference(); }

    public UploadTask add(String userID, Uri img) { return storageReference.child(userID + path).putFile(img); }

    public Task<Void> remove(String userID) { return storageReference.child(userID + path).delete(); }

    public Task<byte[]> get(String userID) { return storageReference.child(userID + path).getBytes(1024*1024); }
}