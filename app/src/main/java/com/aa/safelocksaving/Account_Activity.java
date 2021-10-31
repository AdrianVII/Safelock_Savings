package com.aa.safelocksaving;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.aa.safelocksaving.DAO.DAOUserData;
import com.aa.safelocksaving.Dialog.Dialog_Box;
import com.aa.safelocksaving.data.UserData;
import com.aa.safelocksaving.operation.OPBasics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class Account_Activity extends AppCompatActivity {
    private TextView email;
    private TextView name;
    private TextView btnBack;
    private Button btnDELETEACC;
    private LinearLayout changepass;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private CircleImageView imageView;
    private static final int REQUEST_CODE_IMAGE = 100;
    private Uri image;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        setContentView(R.layout.account_activity);
        btnBack = findViewById(R.id.btnBACK);
        btnBack.setOnClickListener( view -> onBackPressed());
        email = findViewById(R.id.email);
        name =findViewById(R.id.name);
        changepass = findViewById(R.id.changepass);
        imageView = findViewById(R.id.imageView);
        btnDELETEACC = findViewById(R.id.btnDELETEACC);
        imageView.setOnClickListener(view -> choosePicture());
        changepass.setOnClickListener(view -> { });
        btnDELETEACC.setOnClickListener(view -> new Dialog_Box(this, getString(R.string.deleteAccountText), getString(R.string.areYouSureToDeleteYourAccountText)).OnActionButton(new Dialog_Box.OnPositiveClickListener() {
            @Override
            public void positiveClick(View view, Activity activity) {
                new OPBasics().deleteUser(activity, task -> {
                    if (task.isSuccessful()) {
                        new DAOUserData(getBaseContext()).removeAll();
                        Toast.makeText(getBaseContext(), getString(R.string.yourAccountHasBeenDeletedText), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getBaseContext(), Start_Activity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    }
                });
            }

            @Override
            public void negativeClick(View view) {

            }
        }));
        loadPicture();
        loadData();
    }

    private void loadData() {
        name.setText(new DAOUserData(this).getFullName());
        email.setText(new DAOUserData(this).get(UserData.Email, ""));
    }

    private void loadPicture() { new OPBasics().getPicture(bitmap -> imageView.setImageBitmap(bitmap) ); }

    private void uploadPicture() {new OPBasics().uploadPicture(image, task -> {
            if (task.isSuccessful()) Toast.makeText(this, getString(R.string.theImageHasBeenUploadedText), Toast.LENGTH_SHORT).show();
        }, this);
    }

    private void choosePicture() {
        startActivityForResult(new Intent().setType("image/*").setAction(Intent.ACTION_GET_CONTENT), REQUEST_CODE_IMAGE);
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
    private void changePassword (){
        mAuth.sendPasswordResetEmail(user.getEmail())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, getString(R.string.pleaseCheckYourEmailText), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, getString(R.string.errorSendingEmailText), Toast.LENGTH_SHORT).show();
                    }
                    startActivity(new Intent(this, SignIn_Activity.class));
                    finish();
                });
    }

}
