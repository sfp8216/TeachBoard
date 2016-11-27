package com.rit.sfp.teachboard;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by steve on 11/27/2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE = "Teachboard.db";
    public static final String TEACHBOARD_TABLE = "teachboard";
    public static final String TEACHBOARD_DATA = "teachboard_data";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "NAME";
    public static final String COL_3 = "OWNER";
    public static final String COL_4 = "PUBLIC";

    public static final String TBD_COL_1 = "BOARDID";
    public static final String TBD_COL_2= "USERID";
    public static final String TBD_COL_3 = "IMAGEBLOB";





    public DatabaseHelper(Context context) {
        super(context, DATABASE, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE table " + TEACHBOARD_TABLE + "("+COL_1+" INTEGER PRIMARY KEY AUTOINCREMENT, "+COL_2+" TEXT, "+COL_3+" INTEGER, "+COL_4+" INTEGER)");
        db.execSQL("CREATE table " + TEACHBOARD_DATA + "("+TBD_COL_1+" INTEGER PRIMARY KEY, "+TBD_COL_2+" INTEGER, "+TBD_COL_3+" BLOB)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TEACHBOARD_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TEACHBOARD_DATA);
        onCreate(db);
    }

    public boolean createTeachboard(String name, String owner, String boardStatus){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,name);
        contentValues.put(COL_3,owner);
        contentValues.put(COL_4,boardStatus);
        long result = db.insert(TEACHBOARD_TABLE,null,contentValues);
        if(result == -1) {
            return false;
        }else{
            return true;
        }
    }

    public Cursor getImageData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT IMAGEBLOB FROM " +TEACHBOARD_DATA,null);
        return res;
    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TEACHBOARD_TABLE, null);
        return res;
    }

    public Cursor getBoardData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TEACHBOARD_DATA,null);
        return res;
    }

    public boolean saveBoardAsImage(int boardId, int userId, byte[] byteArray){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TBD_COL_1,boardId);
        contentValues.put(TBD_COL_2,userId);
        contentValues.put(TBD_COL_3, byteArray);
        long result = db.insert(TEACHBOARD_DATA, null, contentValues);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }
}
