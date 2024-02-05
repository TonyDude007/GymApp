package com.example.gymapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gymapp.entity.User;
import com.example.gymapp.manager.UserManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ProfileActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICKER = 126;
    private static final int REQUEST_CODE = 1;
    Context context;
    EditText name;
    EditText age;
    EditText weight;
    EditText imgName;
    ImageView imageUser;
    Button btnDel;
    UserManager userManager = new UserManager();
    Button btnSave;
    User currentUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        context = this;

        View customActionBar = getLayoutInflater().inflate(R.layout.delete, null);

        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(customActionBar, layoutParams);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Intent intent = getIntent();
        int userId = intent.getIntExtra("userId", -1);
        currentUser = userManager.getById(context, userId);

        name = findViewById(R.id.profileName);
        name.setText(currentUser.getName());

        age = findViewById(R.id.profileAge);
        age.setText(String.valueOf(currentUser.getAge()));

        weight = findViewById(R.id.profileWeight);
        weight.setText(String.valueOf(currentUser.getWeight()));

        imgName = findViewById(R.id.profileImgName);
        imgName.setText(currentUser.getUserImg());

        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(v -> {

            currentUser.setName(name.getText().toString());
            currentUser.setAge(Integer.parseInt(age.getText().toString()));
            currentUser.setWeight(Integer.parseInt(weight.getText().toString()));
            currentUser.setUserImg(currentUser.getName() + ".png");

            alertBoxSaveInfo(currentUser);
        });

        btnDel = customActionBar.findViewById(R.id.btnDelete);
        btnDel.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Are you sure?");
            builder.setPositiveButton("Yes", (dialog, which) -> {

                userManager.delete(context, currentUser.getId());

                dialog.dismiss();

                Intent intentToMain = new Intent(ProfileActivity.this, MainActivity.class);
                finish();
                startActivity(intentToMain);
            });

            builder.setNegativeButton("No", (dialog, which) -> {
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });

        imageUser = findViewById(R.id.imgUser);
        if (currentUser.getUserImg().equals("noUser.png")) {
            userPictureSearch(imageUser, currentUser.getUserImg());
        } else {
            setPic(currentUser.getUserImg(),imageUser);
        }

        imageUser.setOnClickListener(v -> {
            Intent intentForPic = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intentForPic, REQUEST_IMAGE_PICKER);

            User user = currentUser;
            user.setUserImg(currentUser.getName() + ".png");
            userManager.updateUser(context,user);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICKER && resultCode == RESULT_OK && data != null) {
            // Retrieve the selected image URI
            Uri selectedImageUri = data.getData();

            // Set the selected image URI to the ImageView
            imageUser.setImageURI(selectedImageUri);

            // Convert the selected image URI to a bitmap
            try {
                Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);

                // Save the bitmap to internal storage
                String imageName = currentUser.getUserImg();
                FileOutputStream outputStream = openFileOutput(imageName, Context.MODE_PRIVATE);
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void alertBoxSaveInfo(User userToUpdate) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("SaveInfo");

        builder.setPositiveButton("Yes", (dialog, which) -> {

                userManager.updateUser(context,userToUpdate);

                dialog.dismiss();
                Intent intentToMain = new Intent(ProfileActivity.this, MainActivity.class);
                finish();
                startActivity(intentToMain);
        });

        builder.setNegativeButton("No", (dialog, which) -> {
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    void userPictureSearch(ImageView targetPosition,String picName) {
        try {
            InputStream inputStream = getAssets().open("Users/" + picName);

            Drawable drawable = Drawable.createFromStream(inputStream, null);

            targetPosition.setImageDrawable(drawable);

            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void setPic (String imgName, ImageView location) {
        String imageName = imgName; // Replace with the actual image name
        File imageFile = new File(context.getFilesDir(), imageName);

        if (imageFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            location.setImageBitmap(bitmap);
        }
    }
}
