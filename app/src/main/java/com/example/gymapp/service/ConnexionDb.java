package com.example.gymapp.service;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.gymapp.helper.MuscleBuddyDbHelper;

public class ConnexionDb {
    private static int version = 1;
    private static String dbName = "MuscleBuddy.db";
    public static SQLiteDatabase db = null;
    private static MuscleBuddyDbHelper helper;

    public static SQLiteDatabase getDb(Context context){
        if(helper == null){
            helper = new MuscleBuddyDbHelper(context, dbName, null, version);
        }
        db = helper.getWritableDatabase();
        return db;
    }
    public static void close(){
        if(db!= null &&  !db.isOpen()){
            db.close();
        }
    }
}
