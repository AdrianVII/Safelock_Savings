package com.aa.safelocksaving;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.aa.safelocksaving.DAO.DAOUserData;
import com.aa.safelocksaving.Dialog.Dialog_Bottom_Sheet_Fragment;
import com.aa.safelocksaving.Dialog.Dialog_Box;
import com.aa.safelocksaving.Dialog.Dialog_Change_Name;
import com.aa.safelocksaving.Dialog.Dialog_Change_Password;
import com.aa.safelocksaving.data.Authentication;
import com.aa.safelocksaving.data.UserData;
import com.aa.safelocksaving.Operation.OPBasics;
import com.github.dhaval2404.imagepicker.ImagePicker;

import de.hdodenhof.circleimageview.CircleImageView;

public class Account_Activity extends AppCompatActivity {
    private TextView email;
    private TextView name;
    private TextView btnBack;
    private Button btnDELETEACC;
    private LinearLayout changepass;
    private LinearLayout nameEdit;
    private CircleImageView imageView;
    private static final int REQUEST_CODE_IMAGE = 100;
    private static final int REQUEST_CODE_CAMERA = 101;
    private static final int REQUEST_CODE_READ_PERMISSION = 102;
    private Uri image;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_activity);
        btnBack = findViewById(R.id.btnBACK);
        btnBack.setOnClickListener( view -> onBackPressed());
        email = findViewById(R.id.email);
        name =findViewById(R.id.name);
        changepass = findViewById(R.id.changepass);
        nameEdit = findViewById(R.id.nameEdit);
        imageView = findViewById(R.id.imageView);
        btnDELETEACC = findViewById(R.id.btnDELETEACC);
        imageView.setOnClickListener(view -> choosePicture());
        changepass.setOnClickListener(view -> new Dialog_Change_Password(this, () -> {
            if (new Authentication().logoutUser()) {
                startActivity(new Intent(getBaseContext(), Start_Activity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        }).show());
        nameEdit.setOnClickListener(view -> new Dialog_Change_Name(this, () -> loadData()).show());
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
        new Dialog_Bottom_Sheet_Fragment(new Dialog_Bottom_Sheet_Fragment.OnClickListener() {
            @Override
            public void OnClickFolder(View view) {
                if (checkReadPermission())
                    startActivityForResult(new Intent().setType("image/*").setAction(Intent.ACTION_GET_CONTENT), REQUEST_CODE_IMAGE);
            }

            @Override
            public void OnClickCamera(View view) {
                ImagePicker.Companion.with(Account_Activity.this)
                        .cameraOnly()
                        .crop()
                        .compress(1024)
                        .start(REQUEST_CODE_CAMERA);
            }
        }).show(getSupportFragmentManager(), Dialog_Bottom_Sheet_Fragment.TAG);
    }

    private boolean checkReadPermission () {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[] { Manifest.permission.READ_EXTERNAL_STORAGE }, REQUEST_CODE_READ_PERMISSION);
        else return true;
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            image = data.getData();
            imageView.setImageURI(image);
            uploadPicture();
        } else if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK) {
            image = data.getData();
            imageView.setImageURI(image);
            uploadPicture();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_READ_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            startActivityForResult(new Intent().setType("image/*").setAction(Intent.ACTION_GET_CONTENT), REQUEST_CODE_IMAGE);
    }
}