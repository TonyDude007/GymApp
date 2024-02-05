package com.example.gymapp.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.example.gymapp.entity.User;
import com.example.gymapp.service.ConnexionDb;

import java.util.ArrayList;

public class UserManager {

    public ArrayList<User> getAll(Context context) {
        SQLiteDatabase db = ConnexionDb.getDb(context);
        String request = "SELECT * FROM User";
        ArrayList<User> users = null;
        Cursor cursor = db.rawQuery(request, null);
        if (cursor.isBeforeFirst()) {
            users = new ArrayList<>();
            while (cursor.moveToNext()) {
                User user = new User();
                user.setId(cursor.getInt(cursor.getColumnIndexOrThrow("ID")));
                user.setWeight(cursor.getInt(cursor.getColumnIndexOrThrow("Weight")));
                user.setName(cursor.getString(cursor.getColumnIndexOrThrow("Name")));
                user.setAge(cursor.getInt(cursor.getColumnIndexOrThrow("Age")));
                user.setUserImg(cursor.getString(cursor.getColumnIndexOrThrow("UserImg")));
                users.add(user);
            }
        }
        return users;
    }

    public void AddUser(Context context, User newUser) {
        ArrayList<User> users = getAll(context);

        SQLiteDatabase db = ConnexionDb.getDb(context);
        int id = users.size() + 1;
        int weight = newUser.getWeight();
        String name = newUser.getName();
        int age = newUser.getAge();
        String userImg = "noUser.png";

        String query = "INSERT INTO User VALUES (?, ?, ?, ?,?);";
        SQLiteStatement statement = db.compileStatement(query);
        statement.bindLong(1, id);
        statement.bindString(2, name);
        statement.bindLong(3, age);
        statement.bindLong(4, weight);
        statement.bindString(5,userImg);

        statement.executeInsert();
    }

    public User getById(Context context, int id) {

        SQLiteDatabase db = ConnexionDb.getDb(context);
        String sql = "SELECT * FROM User WHERE ID = ?";

        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(id)});

        if (cursor.moveToFirst()) {

            User user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow("ID")));
            user.setWeight(cursor.getInt(cursor.getColumnIndexOrThrow("Weight")));
            user.setName(cursor.getString(cursor.getColumnIndexOrThrow("Name")));
            user.setAge(cursor.getInt(cursor.getColumnIndexOrThrow("Age")));
            user.setUserImg(cursor.getString(cursor.getColumnIndexOrThrow("UserImg")));

            cursor.close();
            return user;
        } else {
            cursor.close();
            return null;
        }
    }

    public static void updateUser(Context context, User userToUpdate) {
        ContentValues contentValues = new ContentValues();

        contentValues.put("ID", userToUpdate.getId());
        contentValues.put("weight", userToUpdate.getWeight());
        contentValues.put("Name", userToUpdate.getName());
        contentValues.put("Age", userToUpdate.getAge());
        contentValues.put("UserImg",userToUpdate.getUserImg());

        SQLiteDatabase db = ConnexionDb.getDb(context);
        db.update("User", contentValues, "id = ?", new String[]{String.valueOf(userToUpdate.getId())});
    }

    public static void delete(Context context, int idUser){
        SQLiteDatabase db = ConnexionDb.getDb(context);
        db.delete("User","id = ?" , new String[]{String.valueOf(idUser)});
    }
}
