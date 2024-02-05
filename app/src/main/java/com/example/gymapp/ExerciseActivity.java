package com.example.gymapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gymapp.entity.Exercise;
import com.example.gymapp.entity.User;
import com.example.gymapp.entity.Weight;
import com.example.gymapp.manager.ExerciseManager;
import com.example.gymapp.manager.UserManager;
import com.example.gymapp.manager.WeightManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class ExerciseActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICKER = 128;
    private static final int REQUEST_CODE = 2;
    Context context;
    User currentUser;
    Exercise currentExercise;
    UserManager userManager = new UserManager();
    ExerciseManager exerciseManager = new ExerciseManager();
    WeightManager weightManager = new WeightManager();
    TextView nameExercise;
    TextView muscleGroup;
    TextView descriptionExercise;
    ImageView imgExercise;
    Spinner muscleGroupSpinner;
    LinearLayout listWeight;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        context = this;

        Intent intent = getIntent();
        int userId = intent.getIntExtra("userId", -1);
        int exerciseId = intent.getIntExtra("exerciseId", -1);

        currentUser = userManager.getById(context, userId);
        currentExercise = exerciseManager.getById(context, exerciseId);
        ArrayList<Weight> weightArrayList = weightManager.getByExerciseAndUserId(context, exerciseId, userId);

        nameExercise = findViewById(R.id.nameExercise);
        muscleGroup = findViewById(R.id.muscleGroup);
        descriptionExercise = findViewById(R.id.description);

        imgExercise = findViewById(R.id.imageExercise);

        imgExercise.setOnClickListener(v -> {
            Intent intentForPic2 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intentForPic2, REQUEST_IMAGE_PICKER);
            alertBoxSaveInfo(currentExercise, "img");
        });

        setPic(currentExercise.getName().replace(" ","") + ".png",imgExercise);

        nameExercise.setText(currentExercise.getName());
        nameExercise.setOnClickListener(v -> {
            alertBoxSaveInfo(currentExercise, "Name");
        });

        muscleGroup.setText(currentExercise.getMuscle());
        muscleGroup.setOnClickListener(v -> {
            alertBoxSaveInfo(currentExercise, "Muscle");
        });

        descriptionExercise.setText(currentExercise.getDescription());
        descriptionExercise.setOnClickListener(v -> {
            alertBoxSaveInfo(currentExercise, "Description");
        });

        View customActionBar = getLayoutInflater().inflate(R.layout.action_bar_delete, null);

        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(customActionBar, layoutParams);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        listWeight = findViewById(R.id.listWeight);
        for (int i = 0; i < weightArrayList.size(); i++) {
            View listOfWeights = getLayoutInflater().inflate(R.layout.weight_list, null);
            TextView weights = listOfWeights.findViewById(R.id.Weight);
            TextView reps = listOfWeights.findViewById(R.id.Reps);
            TextView date = listOfWeights.findViewById(R.id.Date);

            weights.setText(String.valueOf(weightArrayList.get(i).getLastWeight()));
            reps.setText(String.valueOf(weightArrayList.get(i).getNbReps()));
            date.setText(weightArrayList.get(i).getDate());

            if (i % 2 == 0) {
                String hexColor = "#878787";
                int color = Color.parseColor(hexColor);
                listOfWeights.setBackgroundColor(color);
            } else {
                String hexColor = "#2f2f2f";
                int color = Color.parseColor(hexColor);
                listOfWeights.setBackgroundColor(color);
            }

            listWeight.setPadding(10,10,10,10);

            listWeight.addView(listOfWeights);
        }

        Button addBtn = customActionBar.findViewById(R.id.btnAddSet);
        addBtn.setOnClickListener(v -> {
            View newWeightLayout = getLayoutInflater().inflate(R.layout.new_weight, null);

            TextView title1 = newWeightLayout.findViewById(R.id.title1);
            title1.setText("Weights used : ");

            TextView title2 = newWeightLayout.findViewById(R.id.title2);
            title2.setText("Number of Reps : ");

            EditText weightToAdd = newWeightLayout.findViewById(R.id.text1);
            EditText repToAdd = newWeightLayout.findViewById(R.id.text2);

            LinearLayout.LayoutParams layoutParamsForWeight = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, // width
                    LinearLayout.LayoutParams.MATCH_PARENT // height
            );
            layoutParamsForWeight.setMargins(20, 0, 20, 0);

            newWeightLayout.setPadding(30, 0, 30, 0);
            newWeightLayout.setLayoutParams(layoutParamsForWeight);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Add Weight : ");
            builder.setView(newWeightLayout);
            builder.setPositiveButton("Yes", (dialog, which) -> {

                if (!weightToAdd.getText().toString().equals("") || !repToAdd.getText().toString().equals("")) {
                    Weight newWeight = new Weight();
                    newWeight.setLastWeight(Integer.parseInt(weightToAdd.getText().toString()));
                    newWeight.setNbReps(Integer.parseInt(repToAdd.getText().toString()));
                    newWeight.setExeciseId(currentExercise.getId());
                    newWeight.setUserId(currentUser.getId());

                    LocalDate currentDate = LocalDate.now();
                    String formattedDate = currentDate.toString();

                    newWeight.setDate(formattedDate);

                    weightManager.add(context, newWeight);

                    Intent intentDelete = getIntent();
                    finish();
                    startActivity(intentDelete);
                }
            });

            builder.setNegativeButton("Cancel", (dialog, which) -> {
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });

        Button deleteBtn = customActionBar.findViewById(R.id.btnDelete);
        deleteBtn.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Are you sure?");
            builder.setPositiveButton("Yes", (dialog, which) -> {

                ExerciseManager.delete(context, currentExercise.getId());

                Intent intentDelete = getIntent();
                finish();
                startActivity(intentDelete);
            });

            builder.setNegativeButton("No", (dialog, which) -> {
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });
    }

    void setPic (String imgName, ImageView location) {
        String imageName = imgName; // Replace with the actual image name
        File imageFile = new File(context.getFilesDir(), imageName);

        if (imageFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            location.setImageBitmap(bitmap);
        }
    }

    void alertBoxSaveInfo(Exercise oldExercise, String type) {
        View dialogView = getLayoutInflater().inflate(R.layout.change_info_generic, null);

        TextView textView = dialogView.findViewById(R.id.textView);
        EditText editText = dialogView.findViewById(R.id.editText);

        View muscleGroupView = getLayoutInflater().inflate(R.layout.spinner_muscle_group, null);
        muscleGroupSpinner = muscleGroupView.findViewById(R.id.muscleGroup);

        textView.setText(type + " : ");
        String editTextString = "Nothing Found in DataBase";
        if (type.equals("Description")) {
            editTextString = oldExercise.getDescription();
        } else if (type.equals("Name")) {
            editTextString = oldExercise.getName();
        } else if (type.equals("img")) {
            editTextString = oldExercise.getImgName();
        }
        editText.setText(editTextString);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change " + type + " : ");

        if (type.equals("Muscle")) {
            builder.setView(muscleGroupView);
        } else {
            builder.setView(dialogView);
        }

        builder.setPositiveButton("OK", (dialog, which) -> {
            Exercise exercise = oldExercise;

            if (type.equals("Muscle")) {
                exercise.setMuscle(muscleGroupSpinner.getSelectedItem().toString());
            } else if (type.equals("Description")) {
                exercise.setDescription(String.valueOf(editText.getText()));
            } else if (type.equals("Name")) {
                exercise.setName(String.valueOf(editText.getText()));
            } else if (type.equals("img")) {
                exercise.setImgName(String.valueOf(editText.getText()));
            }

            if (!editText.getText().toString().equals("")) {
                exerciseManager.update(context, exercise);

                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> {
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICKER && resultCode == RESULT_OK && data != null) {
            // Retrieve the selected image URI
            Uri selectedImageUri = data.getData();

            // Set the selected image URI to the ImageView
            imgExercise.setImageURI(selectedImageUri);

            // Convert the selected image URI to a bitmap
            try {
                Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);

                // Save the bitmap to internal storage
                String imageName = currentExercise.getName().replace(" ", "") + ".png";
                FileOutputStream outputStream = openFileOutput(imageName, Context.MODE_PRIVATE);
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}

