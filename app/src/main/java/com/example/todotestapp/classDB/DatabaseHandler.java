package com.example.todotestapp.classDB;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.todotestapp.Otherclass.ToDoModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    // SQLite helper for storing SWOT strengths, weaknesses, opportunities and threats

    private static final int VERSION = 1;
    private static final String NAME = "SWOT2";
    private String SWOT_TABLE;
    private static final String ID = "id";
    private static final String TASK = "task";
    private static final String USER = "user";


    private SQLiteDatabase db;

    public DatabaseHandler(Context context, String name ){
        super(context,NAME,null,VERSION);
        SWOT_TABLE =name;
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE strength ("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+TASK+" TEXT, "+USER+" TEXT)");
        db.execSQL("CREATE TABLE weakness ("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+TASK+" TEXT, "+USER+" TEXT)");
        db.execSQL("CREATE TABLE opportunity ("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+TASK+" TEXT, "+USER+" TEXT)");
        db.execSQL("CREATE TABLE threat ("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+TASK+" TEXT, "+USER+" TEXT)");

    }

    @Override
    public  void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS strength");
        db.execSQL("DROP TABLE IF EXISTS weakness");
        db.execSQL("DROP TABLE IF EXISTS opportunity");
        db.execSQL("DROP TABLE IF EXISTS threat");
        onCreate(db);
    }

    public void openDatabase(){
        db=this.getWritableDatabase();
    }

    public void insertTask(ToDoModel task){
        ContentValues cv = new ContentValues();
        cv.put(TASK,task.getTask());
        cv.put(USER,task.getUser());
        db.insert(SWOT_TABLE,null,cv);
    }



    @SuppressLint("Range")
    public List<ToDoModel> getALLTasks(String usnme){
        List<ToDoModel> tasklist = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try{
            cur = db.rawQuery("SELECT * FROM "+SWOT_TABLE+" WHERE user = ?", new String[]{usnme});
            if (cur != null){
                if (cur.moveToFirst()){
                    do{
                        ToDoModel task = new ToDoModel();
                        task.setId(cur.getInt( (Integer) cur.getColumnIndex(ID)));
                        task.setTask(cur.getString(cur.getColumnIndex(TASK)));
                        task.setUser(cur.getString(cur.getColumnIndex(USER)));
                        tasklist.add(task);
                    }while(cur.moveToNext());
                }
            }
        }
        finally {
            db.endTransaction();
            cur.close();
        }
        return  tasklist;
    }

    public void updateTask(int id,String task){
        ContentValues cv = new ContentValues();
        cv.put(TASK,task);
        db.update(SWOT_TABLE,cv,ID+"=?",new String[] {String.valueOf(id)});
    }

    public void deleteTask(int id){
        db.delete(SWOT_TABLE,ID+"=?", new String[]{String.valueOf(id)});
    }

    public int getTableCount(String table ,String usnme){
        Cursor cs = db.rawQuery("SELECT COUNT(*) FROM "+table+" WHERE user =?", new String[]{usnme});
        cs.moveToFirst();
        return cs.getInt(0);
    }



}
