package com.example.todotestapp.classDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class LogRegDB extends SQLiteOpenHelper {
    //SQLite helper to handle user login features

    public LogRegDB( Context context) {
        super(context,"Udatabase1",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase logDB) {
        logDB.execSQL("CREATE TABLE user (username TEXT PRIMARY KEY , email TEXT UNIQUE , password TEXT, imagesrc TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase logDB, int i, int i1) {
        logDB.execSQL("DROP TABLE IF EXISTS user");
    }

    public Boolean insertUser(String username,String email, String password, String profilePic){
        SQLiteDatabase logDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username",username);
        contentValues.put("email",email);
        contentValues.put("password",password);
        contentValues.put ("imagesrc",profilePic);
        long result = logDB.insert("user",null,contentValues);

        if (result==-1){
            return false;
        }
        else{
            return  true;
        }
    }
    
    public Cursor getUser(String email){
        SQLiteDatabase logDB = this.getWritableDatabase();
        Cursor cs = logDB.rawQuery("SELECT * FROM user WHERE email = ?", new String[]{email});
        return cs;
    }
    
    
    public Boolean checkReg(String username){
        SQLiteDatabase logDB = this.getWritableDatabase();
        Cursor cs = logDB.rawQuery("SELECT * FROM user WHERE username = ?", new String[]{username});
        if (cs.getCount()>0){
            return true;
        }
        else{
            return false;
        }
    }

    public String checkLog(String username, String password){
        SQLiteDatabase logDB = this.getWritableDatabase();
        Cursor cs = logDB.rawQuery("SELECT * FROM user WHERE username = ? AND password = ?", new String[]{username,password});
        if (cs.getCount()>0){
            cs.moveToFirst();
            String email = cs.getString(1);
            return email;
        }
        else{
            return "nouser";
        }
    }
    

}
