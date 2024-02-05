package com.example.gymapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.gymapp.entity.Exercise;
import com.example.gymapp.entity.User;
import com.example.gymapp.entity.Weight;
import com.example.gymapp.manager.ExerciseManager;
import com.example.gymapp.manager.UserManager;
import com.example.gymapp.manager.WeightManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class UserActivity extends AppCompatActivity {

    UserManager userManager = new UserManager();
    int userId;
    Context context;
    Button addExercise;
    ExerciseManager exerciseManager = new ExerciseManager();
    LinearLayout linearLayoutExecise;
    Spinner filtreMuscle;
    EditText searchFiltre;
    User currentUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        context = this;

        Intent receivedIntent = getIntent();
        if (receivedIntent != null) {
            userId = receivedIntent.getIntExtra("userId", -1);
        }

        currentUser = userManager.getById(context, userId);

        View customActionBar = getLayoutInflater().inflate(R.layout.action_bar_user, null);

        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT
        );

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(customActionBar, layoutParams);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView title = customActionBar.findViewById(R.id.actionBarTitle);
        ImageView settingsImg = customActionBar.findViewById(R.id.imgSettings);

        title.setText(currentUser.getName() + "'s Page");

        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.settings);
        if (drawable != null) {
            settingsImg.setImageDrawable(drawable);
        }

        settingsImg.setOnClickListener(v -> {

        });

        linearLayoutExecise = findViewById(R.id.ExerciseList);

        ArrayList<Exercise> listDesExercises = exerciseManager.getAll(context);
        setList(linearLayoutExecise,listDesExercises);

        filtreMuscle = findViewById(R.id.filtreMuscle);
        filtreMuscle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedMuscle = parent.getItemAtPosition(position).toString();


                if (selectedMuscle.equals("All")) {
                    ArrayList<Exercise> listDesFav = exerciseManager.getAll(context);
                    setList(linearLayoutExecise,listDesFav);

                }else {
                    ArrayList<Exercise> listDesExecisesByMuscle = exerciseManager.getByMuscle(context, selectedMuscle);
                    setList(linearLayoutExecise, listDesExecisesByMuscle);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        searchFiltre = findViewById(R.id.searchFiltre);
        searchFiltre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String search = searchFiltre.getText().toString();
                ArrayList<Exercise> searchResults = new ArrayList<>();
                ExerciseManager exerciseManager = new ExerciseManager();

                if (!search.isEmpty()) {
                    String currentSearchText = search;
                    ArrayList<Exercise> searchResult = exerciseManager.searchByKeyword(context, currentSearchText);
                    searchResults.addAll(searchResult);
                    setList(linearLayoutExecise, searchResult);
                } else if (search.isEmpty()) {
                    ArrayList<Exercise> exercises = exerciseManager.getAll(context);
                    setList(linearLayoutExecise, exercises);
                }
            }
        });

        addExercise = findViewById(R.id.btnExercise);
        addExercise.setOnClickListener(v -> {
            View dialogView = getLayoutInflater().inflate(R.layout.new_exercise_input_alert, null);

            EditText exerciseName = dialogView.findViewById(R.id.nameExercise);
            EditText exerciseDescription = dialogView.findViewById(R.id.exerciseDescription);
            Spinner muscleGroup = dialogView.findViewById(R.id.muscleGroup);
            String selectedMuscle = muscleGroup.getSelectedItem().toString();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Add Exercise");
            builder.setMessage("Fill out info :");
            builder.setView(dialogView);
            builder.setPositiveButton("OK", (dialog, which) -> {
                Exercise exercise = new Exercise();
                exercise.setName(String.valueOf(exerciseName.getText()));
                exercise.setDescription(String.valueOf(exerciseDescription.getText()));
                exercise.setImgName(exercise.getName().replace(" ", "") + ".png");
                exercise.setMuscle(selectedMuscle);

                if (!exerciseName.getText().toString().equals("") || !exerciseDescription.getText().toString().equals("")) {
                    exerciseManager.add(context, exercise);

                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> {});
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });

        settingsImg.setOnClickListener(v -> {
            Intent intent = new Intent(UserActivity.this, ProfileActivity.class);
            intent.putExtra("userId", currentUser.getId());
            startActivity(intent);
        });
    }

    public void setList(LinearLayout destination, ArrayList<Exercise> listExecises) {

        destination.removeAllViews();

        for (int i = 0; i < listExecises.size(); i++) {

            LinearLayout exerciseList = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.exercise_layout, null);
            ImageView imgExercise = exerciseList.findViewById(R.id.imageExercise);
            TextView nameExercise = exerciseList.findViewById(R.id.nameExercise);
            TextView bestWeightExercise = exerciseList.findViewById(R.id.bestWeight);
            TextView nbRepsExercise = exerciseList.findViewById(R.id.numberReps);
            TextView dateExercise = exerciseList.findViewById(R.id.dateOfWeight);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, // width
                    500  // height
            );
            layoutParams.setMargins(10, 10, 10, 10);

            exerciseList.setPadding(20, 10, 20, 10);
            exerciseList.setLayoutParams(layoutParams);

            int backgroundColor = Color.rgb(32, 32, 32);
            exerciseList.setBackgroundColor(backgroundColor);

            destination.addView(exerciseList);

            WeightManager weightManager = new WeightManager();
            ArrayList<Weight> weights = weightManager.getByExerciseAndUserId(context,listExecises.get(i).getId(),userId);

            Weight topWeight = new Weight();
            topWeight.setLastWeight(0);

            for (int j = 0; j < weights.size(); j++) {
                if (j == 0 || topWeight.getLastWeight() <= weights.get(j).getLastWeight()) {
                    topWeight = weights.get(j);
                }
            }

            setPic(listExecises.get(i).getImgName(),imgExercise);
            //exercisePictureSearch(imgExercise,listExecises.get(i).getImgName());
            nameExercise.setText(listExecises.get(i).getName());
            if (topWeight.getLastWeight() == 0) {
                bestWeightExercise.setText("-");
                nbRepsExercise.setText("-");
                dateExercise.setText("-");
            } else {
                bestWeightExercise.setText(String.valueOf(topWeight.getLastWeight()) + "lbs");
                nbRepsExercise.setText(String.valueOf(topWeight.getNbReps()));
                dateExercise.setText(topWeight.getDate());
            }

            Exercise currentExercise = listExecises.get(i);
            exerciseList.setOnClickListener(v -> {
                Intent intent = new Intent(UserActivity.this, ExerciseActivity.class);
                intent.putExtra("userId", currentUser.getId());
                intent.putExtra("exerciseId", currentExercise.getId());
                startActivity(intent);
                finish();
            });
        }
    }

    void exercisePictureSearch(ImageView targetPosition,String picName) {
        try {
            InputStream inputStream = getAssets().open("Exercises/" + picName);

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
