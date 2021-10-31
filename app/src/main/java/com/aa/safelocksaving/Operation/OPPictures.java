package com.aa.safelocksaving.Operation;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.aa.safelocksaving.DAO.DAOPicture;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.UploadTask;

public class OPPictures extends DAOPicture {

    public interface PictureListener {
        void get(Bitmap bitmap);
        void upload(Task<UploadTask.TaskSnapshot> task);
    }


    public void get(String user, PictureListener listener) {
        get(user).addOnSuccessListener(bytes -> listener.get(BitmapFactory.decodeByteArray(bytes, 0, bytes.length)));
    }


    public void uploadPicture(String user, Uri img, PictureListener listener) {
        add(user, img).addOnCompleteListener(listener::upload);
    }

}
