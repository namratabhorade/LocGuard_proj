package com.example.locguard;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Silent_DataBase";
    public DataBaseHelper(Context applicationcontext)
    {
        super(applicationcontext, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query;
        query="CREATE TABLE IF NOT EXISTS UserDetails(Id INTEGER PRIMARY KEY AUTOINCREMENT , Time1 VARCHAR, Time2 VARCHAR);";
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query;
        query="DROP TABLE IF EXISTS UserDetails";
        db.execSQL(query);
        onCreate(db);

    }
}
