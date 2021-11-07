package com.aa.safelocksaving;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.aa.safelocksaving.DAO.DAOUserData;
import com.aa.safelocksaving.Dialog.Dialog_Bottom_Sheet_Fragment;
import com.aa.safelocksaving.Dialog.Dialog_Box;
import com.aa.safelocksaving.Dialog.Dialog_Change_Password;
import com.aa.safelocksaving.data.Authentication;
import com.aa.safelocksaving.data.UserData;
import com.aa.safelocksaving.Operation.OPBasics;
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
    private static final int REQUEST_CODE_CAMERA = 101;
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
        changepass.setOnClickListener(view -> new Dialog_Change_Password(this, () -> {
            if (new Authentication().logoutUser()) {
                startActivity(new Intent(getBaseContext(), Start_Activity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        }).show());
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
                startActivityForResult(new Intent().setType("image/*").setAction(Intent.ACTION_GET_CONTENT), REQUEST_CODE_IMAGE);
            }

            @Override
            public void OnClickCamera(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,REQUEST_CODE_CAMERA);
            }
        }).show(getSupportFragmentManager(), Dialog_Bottom_Sheet_Fragment.TAG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            image = data.getData();
            imageView.setImageURI(image);
            uploadPicture();
        } else if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK) {
            /*try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), image);
                imageView.setImageBitmap(bitmap);
                String[] proj = {MediaStore.Images.Media.DATA};
                Cursor cursor = managedQuery(image, proj, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                image = Uri.parse(cursor.getString(column_index));
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            //Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            //ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            //bitmap.compress(Bitmap.CompressFormat.JPEG,100,bytes);
            //String path = MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver() ,bitmap ,"Profile_Picture.jpg",null);

            /*String filename = "Profile_Picture.jpg";
            String baseDirectory = String.format("%s/SafeLock_Savings", Environment.DIRECTORY_PICTURES);
            try {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.ImageColumns.DISPLAY_NAME, filename);
                values.put(MediaStore.Images.ImageColumns.MIME_TYPE, "image/jpeg");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    values.put(MediaStore.Images.ImageColumns.RELATIVE_PATH, baseDirectory);
                    values.put(MediaStore.Images.ImageColumns.IS_PENDING, true);
                } else {
                    String fullpath = String.format("%s/%s/%s",
                            Environment.getExternalStorageDirectory(),
                            baseDirectory,
                            filename);
                    values.put(MediaStore.Images.ImageColumns.DATA, fullpath);
                }
                Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                if (uri != null) {
                    OutputStream fos = getContentResolver().openOutputStream(uri);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();
                    image = uri;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        values.clear();
                        values.put(MediaStore.Images.ImageColumns.IS_PENDING, false);
                        getContentResolver().update(uri, values, null, null);
                    }
                }
            } catch (IOException ioe) { ioe.printStackTrace(); }*/

            //image = Uri.parse(path);
            imageView.setImageURI(image);
            uploadPicture();
        }
    }

}
