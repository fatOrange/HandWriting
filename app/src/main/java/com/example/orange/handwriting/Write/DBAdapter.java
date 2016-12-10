package com.example.orange.handwriting.Write;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by orange on 16/11/24.
 */
public class DBAdapter {
    private static final String DB_NAME = "number.db";
    private static final String DB_Table = "number_info";
    private static final int DB_Version = 1;
    private static final String KEY_ID = "_id";
    private SQLiteDatabase db;
    private final Context context;
    private DBOpenHelper dbOpenHelper;
    public DBAdapter(Context _context){
        context = _context;
    }
    public void open() throws SQLiteException{
        dbOpenHelper = new DBOpenHelper(context,DB_NAME,null,DB_Version);
        try{
            db =dbOpenHelper.getWritableDatabase();
        }catch (SQLiteException e){
            db=dbOpenHelper.getReadableDatabase();
        }
    }

    public void insert(String s){
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_ID,s);
        db.insert(DB_Table,null,newValues);
    }

    private String[] ConvertToString(Cursor cursor){
        int resultCounts = cursor.getCount();
        System.out.println("11111111");
        if(resultCounts == 0||!cursor.moveToFirst()){
            return null;
        }
        String[] a =new String[resultCounts];
        for (int i =0;i<resultCounts;i++){
            a[i]=cursor.getString(0);
            cursor.moveToNext();
        }
        return a;
    }

    public String[] getAllData(){
        Cursor result = db.query(DB_Table,new String[]{KEY_ID},null,null,null,null,null,null);
        return ConvertToString(result);
    }


    private static class DBOpenHelper extends SQLiteOpenHelper{

        public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }
        private static final String DB_CREATE ="create table "+DB_Table+" ( "+KEY_ID+" varchar(256))";
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }




}


