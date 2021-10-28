package com.aa.safelocksaving;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.aa.safelocksaving.data.User;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class Account_Activity extends Activity {
    private TextView email;
    private TextView name;
    private TextView btnBack;
    private LinearLayout changepass;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private CircleImageView imageView;
    private static final int REQUEST_CODE_IMAGE = 100;
    private Uri image;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        setContentView(R.layout.account_activity);
        btnBack = findViewById(R.id.btnBACK);
        btnBack.setOnClickListener( view -> onBackPressed());
        email = findViewById(R.id.email);
        name =findViewById(R.id.name);
        changepass = findViewById(R.id.changepass);
        imageView = findViewById(R.id.imageView);
        imageView.setOnClickListener(view -> choosePicture());
        loadPicture();
        loadData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        String emailText = user.getEmail();
        String nameText = user.getDisplayName();
        email.setText(emailText);
        name.setText(nameText);
    }

    private void loadData() {
        final String userID = user.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User dataUser = snapshot.getValue(User.class);
                if (dataUser != null) {
                    String fullName = String.format("%s %s", dataUser.getName(), dataUser.getLastname());
                    name.setText(fullName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadPicture() {
        final String userID = user.getUid();
        StorageReference reference = storageReference.child(userID + "/images/ProfilePicture.jpg");
        final long MAXBYTES = 1024*1024;
        reference.getBytes(MAXBYTES).addOnSuccessListener(bytes -> {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            imageView.setImageBitmap(bitmap);
        }).addOnFailureListener(e -> {

        });
    }

    private void uploadPicture() {
        final String userID = user.getUid();
        StorageReference reference = storageReference.child(userID + "/images/ProfilePicture.jpg");
        reference.putFile(image)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, getString(R.string.theImageHasBeenUploadedText), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_CODE_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            image = data.getData();
            imageView.setImageURI(image);
            uploadPicture();
        }
    }
}
