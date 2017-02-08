package com.rit.sfp.teachboard;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by steve on 11/27/2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE = "Teachboard.db";
    public static final String TEACHBOARD_TABLE = "teachboard";
    public static final String TEACHBOARD_DATA = "teachboard_data";
    public static final String TEACHBOARD_USERS = "teachboard_users";
    public static final String TEACHBOARD_CHAT = "teachboard_chat";

    public static final String TBT_1 = "ID";
    public static final String TBT_2 = "NAME";
    public static final String TBT_3 = "OWNER";
    public static final String TBT_4 = "PUBLIC";
    public static final String TBT_5 = "IMAGEBLOB";

    //Data columns
    public static final String TBD_COL_1 = "BOARDID";
    public static final String TBD_COL_2 = "USERID";
    public static final String TBD_COL_3 = "IMAGEBLOB";
    //User columns
    public static final String TBU_1 = "USERID";
    public static final String TBU_2 = "USERNAME";
    public static final String TBU_3 = "PASSWORD";
    public static final String TBU_4 = "EMAIL";


    public DatabaseHelper(Context context) {
        super(context, DATABASE, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE table " + TEACHBOARD_TABLE + "(" + TBT_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TBT_2 + " TEXT, " + TBT_3 + " INTEGER, " + TBT_4 + " INTEGER, " + TBT_5 + " BLOB)");
        db.execSQL("CREATE table " + TEACHBOARD_USERS + "(" + TBU_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TBU_2 + " TEXT UNIQUE, " + TBU_3 + " TEXT, " + TBU_4 + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TEACHBOARD_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TEACHBOARD_USERS);
        onCreate(db);
    }

    //Create Board
    public boolean createTeachboard(String name, String owner, String boardStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TBT_2, name);
        contentValues.put(TBT_3, owner);
        contentValues.put(TBT_4, boardStatus);
        long result = db.insert(TEACHBOARD_TABLE, null, contentValues);
        return result != -1;
    }

    //Get list of boards
    public Cursor getAllTeachboards() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TEACHBOARD_TABLE, null);
        return res;
    }

    public Cursor getImageData(String boardId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT IMAGEBLOB FROM " + TEACHBOARD_TABLE + " WHERE " + TBT_1 + " = " + boardId, null);
        return res;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TEACHBOARD_TABLE, null);
        return res;
    }

    public boolean saveBoardAsImage(String boardId, String userId, byte[] byteArray) {
        SQLiteDatabase db = this.getWritableDatabase();
        //Check to see if drawable already exists in the database before entering it
        Cursor res = db.rawQuery("SELECT * FROM " + TEACHBOARD_TABLE + " WHERE " + TBT_1 + " = " + boardId, null);
        if (res.getCount() == 1) {
            ContentValues updateValues = new ContentValues();
            updateValues.put(TBD_COL_3, byteArray);
            int success = db.update(TEACHBOARD_TABLE, updateValues, TBT_1 + " = " + boardId, null);
            if (success == 1) {
                Log.i("SUCCESS", "UPDATE");
                return true;
            } else {
                Log.i("FAIL", "UPDATE");
                return false;
            }

        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put(TBD_COL_3, byteArray);
            long result = db.insert(TEACHBOARD_TABLE, null, contentValues);
            if (result == -1) {
                Log.i("SUCCESS", "INSERT");
                return false;
            } else {
                Log.i("FAIL", "INSERT");
                return true;
            }
        }
    }

    public void clearBoard(String boardId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.putNull(TBT_5);
        db.update(TEACHBOARD_TABLE, contentValues, TBT_1 + "=" + boardId, null);
        Log.i("Database", "Cleared Board");
    }

    /*User Logic */
    public boolean createAccount(String username, String password, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TBU_2, username);
        contentValues.put(TBU_3, password);
        contentValues.put(TBU_4, email);
        long result = db.insert(TEACHBOARD_USERS, null, contentValues);
        return result != -1;
    }

    public boolean checkLogin(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TEACHBOARD_USERS + " WHERE USERNAME='" + username + "' AND PASSWORD='" + password + "'", null);
        return res.getCount() == 1;
    }

}
