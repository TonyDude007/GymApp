package com.example.gymapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gymapp.entity.User;
import com.example.gymapp.manager.UserManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {

    Context context;
    Button btnUser;
    Button schedule;
    LinearLayout userLayout;
    UserManager userManager = new UserManager();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        userLayout = findViewById(R.id.userLayout);

        ArrayList<User> userArrayList = userManager.getAll(context);
        if (!userArrayList.isEmpty()){
            for (int i = 0; i < userArrayList.size(); i++) {
                LayoutInflater inflater = LayoutInflater.from(this);
                View dialogView = inflater.inflate(R.layout.user_login_layout, null);

                int height = 500;
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, height
                );

                layoutParams.setMargins(0,25,0,25);

                dialogView.setLayoutParams(layoutParams);

                TextView nameUser = dialogView.findViewById(R.id.userName);
                TextView ageUser = dialogView.findViewById(R.id.userAge);
                TextView WeightUser = dialogView.findViewById(R.id.userWeight);
                ImageView userImg = dialogView.findViewById(R.id.userImg);

                if (userArrayList.get(i).getUserImg().equals("noUser.png")) {
                    userPictureSearch(userImg,"noUser.png");
                }else {
                    setPic(userArrayList.get(i).getUserImg(),userImg);
                }

                nameUser.setText(userArrayList.get(i).getName());
                ageUser.setText(String.valueOf(userArrayList.get(i).getAge()));
                WeightUser.setText(String.valueOf(userArrayList.get(i).getWeight()));

                int finalI = i;
                dialogView.setOnClickListener(v -> {
                    Intent intent = new Intent(MainActivity.this, UserActivity.class);
                    intent.putExtra("userId", userArrayList.get(finalI).getId());
                    startActivity(intent);
                });

                userLayout.addView(dialogView);
            }
        }

        btnUser = findViewById(R.id.buttonUser);

        btnUser.setOnClickListener( v -> {
            View dialogView = getLayoutInflater().inflate(R.layout.new_user_input_alert, null);

            EditText editTextName = dialogView.findViewById(R.id.inputNameUser);
            EditText editTextAge = dialogView.findViewById(R.id.inputAgeUser);
            EditText editTextWeight = dialogView.findViewById(R.id.inputWeightUser);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("New User");
            builder.setMessage("Fill out info :");
            builder.setView(dialogView);
            builder.setPositiveButton("OK", (dialog, which) -> {
                String name = editTextName.getText().toString();
                String ageText = editTextAge.getText().toString();
                String weightText = editTextWeight.getText().toString();

                if (!name.isEmpty() && !ageText.isEmpty() && !weightText.isEmpty()) {
                    int age = Integer.parseInt(ageText);
                    int weight = Integer.parseInt(weightText);

                    createUserAndRefresh(name, age, weight);
                }
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> {});
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });

        schedule = findViewById(R.id.schedule);
        schedule.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ScheduleActivity.class);
            startActivity(intent);
        });

    }

    private void createUserAndRefresh(String name, int age, int weight) {
        User newUser = new User();
        newUser.setName(name);
        newUser.setAge(age);
        newUser.setWeight(weight);

        userManager.AddUser(context, newUser);

        Intent intent = getIntent();
        finish();
        startActivity(intent);
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
