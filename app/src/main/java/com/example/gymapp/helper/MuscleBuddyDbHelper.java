package com.example.gymapp.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MuscleBuddyDbHelper extends SQLiteOpenHelper {
    public MuscleBuddyDbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE User (\n" +
                "ID INTEGER NOT NULL DEFAULT 1 UNIQUE PRIMARY KEY AUTOINCREMENT," +
                "Name TEXT," +
                "Age INTEGER," +
                "Weight INTEGER," +
                "UserImg TEXT" +
                ");");

        db.execSQL("CREATE TABLE IF NOT EXISTS Exercises (\n" +
                "ID INTEGER NOT NULL DEFAULT 1 UNIQUE PRIMARY KEY," +
                "Name TEXT NOT NULL," +
                "Description TEXT," +
                "Muscle TEXT," +
                "ImgName TEXT" +
                ");");

        db.execSQL("CREATE TABLE IF NOT EXISTS weights (\n" +
                "ID INTEGER NOT NULL UNIQUE PRIMARY KEY AUTOINCREMENT," +
                "UserId INTEGER NOT NULL," +
                "ExerciseId INTEGER NOT NULL," +
                "LastWeight INTEGER NOT NULL," +
                "NbReps INTEGER NOT NULL," +
                "Date TEXT NOT NULL," +
                "FOREIGN KEY(UserId) REFERENCES User(ID)" +
                ");");

            }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
