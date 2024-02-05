package com.example.gymapp.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.gymapp.entity.Exercise;
import com.example.gymapp.service.ConnexionDb;

import java.util.ArrayList;

public class ExerciseManager {

    public ArrayList<Exercise> getAll(Context context) {
        SQLiteDatabase db = ConnexionDb.getDb(context);
        String request = "SELECT * FROM Exercises";
        ArrayList<Exercise> exercises = null;
        Cursor cursor = db.rawQuery(request,null);
        if (cursor.isBeforeFirst()) {
            exercises = new ArrayList<>();
            while (cursor.moveToNext()) {
                Exercise exercise = new Exercise();
                exercise.setId(cursor.getInt(cursor.getColumnIndexOrThrow("ID")));
                exercise.setName(cursor.getString(cursor.getColumnIndexOrThrow("Name")));
                exercise.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("Description")));
                exercise.setMuscle(cursor.getString(cursor.getColumnIndexOrThrow("Muscle")));
                exercise.setImgName(cursor.getString(cursor.getColumnIndexOrThrow("ImgName")));
                exercises.add(exercise);
            }
        }
        return exercises;
    }
    public ArrayList<Exercise> getByMuscle(Context context,String muscle) {
        SQLiteDatabase db = ConnexionDb.getDb(context);
        String sql = "SELECT * FROM Exercises WHERE Muscle = ?";
        ArrayList<Exercise> exercises = null;

           Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(muscle)});
            exercises = new ArrayList<>();
            while (cursor.moveToNext()) {
                Exercise exercise = new Exercise();
                exercise.setId(cursor.getInt(cursor.getColumnIndexOrThrow("ID")));
                exercise.setName(cursor.getString(cursor.getColumnIndexOrThrow("Name")));
                exercise.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("Description")));
                exercise.setMuscle(cursor.getString(cursor.getColumnIndexOrThrow("Muscle")));
                exercise.setImgName(cursor.getString(cursor.getColumnIndexOrThrow("ImgName")));
                exercises.add(exercise);
            }

        return exercises;
    }

    public ArrayList<Exercise> searchByKeyword(Context context,String search) {

        SQLiteDatabase db = ConnexionDb.getDb(context);
        String sql = "SELECT * FROM Exercises WHERE Name LIKE ?";
        ArrayList<Exercise> exercises = null;

        Cursor cursor = db.rawQuery(sql, new String[]{"%" + search + "%"});
        exercises = new ArrayList<>();
        while (cursor.moveToNext()) {
            Exercise exercise = new Exercise();
            exercise.setId(cursor.getInt(cursor.getColumnIndexOrThrow("ID")));
            exercise.setName(cursor.getString(cursor.getColumnIndexOrThrow("Name")));
            exercise.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("Description")));
            exercise.setMuscle(cursor.getString(cursor.getColumnIndexOrThrow("Muscle")));
            exercise.setImgName(cursor.getString(cursor.getColumnIndexOrThrow("ImgName")));
            exercises.add(exercise);
        }

        return exercises;
    }

    public Exercise getById(Context context, int id) {

        SQLiteDatabase db = ConnexionDb.getDb(context);
        String sql = "SELECT * FROM Exercises WHERE ID = ?";

        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(id)});

        if (cursor.moveToFirst()) {

            Exercise exercise = new Exercise();
            exercise.setId(cursor.getInt(cursor.getColumnIndexOrThrow("ID")));
            exercise.setName(cursor.getString(cursor.getColumnIndexOrThrow("Name")));
            exercise.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("Description")));
            exercise.setMuscle(cursor.getString(cursor.getColumnIndexOrThrow("Muscle")));
            exercise.setImgName(cursor.getString(cursor.getColumnIndexOrThrow("ImgName")));

            cursor.close();
            return exercise;
        } else {
            cursor.close();
            return null;
        }
    }

    public static void update(Context context, Exercise exerciseToUpdate) {
        ContentValues contentValues = new ContentValues();

        contentValues.put("id", exerciseToUpdate.getId());
        contentValues.put("Name", exerciseToUpdate.getName());
        contentValues.put("Description", exerciseToUpdate.getDescription());
        contentValues.put("Muscle", exerciseToUpdate.getMuscle());
        contentValues.put("ImgName", exerciseToUpdate.getImgName());

        SQLiteDatabase db = ConnexionDb.getDb(context);
        db.update("Exercises", contentValues, "id = ?", new String[]{String.valueOf(exerciseToUpdate.getId())});
    }

    public static long add(Context context, Exercise exerciseToAdd){
        ContentValues contentValues = new ContentValues();
        contentValues.put("Name", exerciseToAdd.getName());
        contentValues.put("Description", exerciseToAdd.getDescription());
        contentValues.put("Muscle", exerciseToAdd.getMuscle());
        contentValues.put("ImgName",exerciseToAdd.getImgName());

        SQLiteDatabase db = ConnexionDb.getDb(context);
        return db.insert("Exercises", null, contentValues);
    }

    public static void delete(Context context, int idExercise){
        SQLiteDatabase db = ConnexionDb.getDb(context);
        db.delete("Exercises","id = ?" , new String[]{String.valueOf(idExercise)});
    }
}
