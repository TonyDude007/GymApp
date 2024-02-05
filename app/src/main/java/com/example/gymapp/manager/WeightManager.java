package com.example.gymapp.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.gymapp.entity.Weight;
import com.example.gymapp.service.ConnexionDb;

import java.util.ArrayList;

public class WeightManager {
    public ArrayList<Weight> getByExerciseAndUserId(Context context, int exerciseId, int userId) {
        SQLiteDatabase db = ConnexionDb.getDb(context);
        String sql = "SELECT * FROM weights WHERE ExerciseId = ? AND UserId = ?";

        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(exerciseId), String.valueOf(userId)});

        ArrayList<Weight> weights = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                Weight weight = new Weight();
                weight.setId(cursor.getInt(cursor.getColumnIndexOrThrow("ID")));
                weight.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow("UserId")));
                weight.setExeciseId(cursor.getInt(cursor.getColumnIndexOrThrow("ExerciseId")));
                weight.setLastWeight(cursor.getInt(cursor.getColumnIndexOrThrow("LastWeight")));
                weight.setNbReps(cursor.getInt(cursor.getColumnIndexOrThrow("NbReps")));
                weight.setDate(cursor.getString(cursor.getColumnIndexOrThrow("Date")));
                weights.add(weight);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return weights;
    }

    public ArrayList<Weight> getAll(Context context) {

        SQLiteDatabase db = ConnexionDb.getDb(context);
        String sql = "SELECT * FROM weights";

        Cursor cursor = db.rawQuery(sql,null);

        ArrayList<Weight> weights = new ArrayList<>();

        if (cursor.moveToFirst()) {
            Weight weight = new Weight();
            weight.setId(cursor.getInt(cursor.getColumnIndexOrThrow("ID")));
            weight.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow("UserId")));
            weight.setExeciseId(cursor.getInt(cursor.getColumnIndexOrThrow("ExerciseId")));
            weight.setLastWeight(cursor.getInt(cursor.getColumnIndexOrThrow("LastWeight")));
            weight.setNbReps(cursor.getInt(cursor.getColumnIndexOrThrow("NbReps")));
            weight.setDate(cursor.getString(cursor.getColumnIndexOrThrow("Date")));
            weights.add(weight);
        }
        return weights;
    }

    public long add(Context context, Weight newWeight){
        ContentValues contentValues = new ContentValues();

        contentValues.put("UserId", newWeight.getUserId());
        contentValues.put("ExerciseId", newWeight.getExeciseId());
        contentValues.put("LastWeight", newWeight.getLastWeight());
        contentValues.put("NbReps", newWeight.getNbReps());
        contentValues.put("Date", newWeight.getDate());

        SQLiteDatabase bd = ConnexionDb.getDb(context);
        return bd.insert("weights", null, contentValues);
    }

}
