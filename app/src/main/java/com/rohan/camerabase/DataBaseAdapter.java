package com.rohan.camerabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rohan Sampson on 5/24/2016.
 */
public class DataBaseAdapter {
    static final String DATABASE_NAME = "login.db";
    static final int DATABASE_VERSION = 1;
    public static final int NAME_COLUMN = 1;
    static final String DATABASE_CREATE = "Create table LOGIN(ID integer primary key autoincrement, USERNAME text, PASSWORD text, NAME text, AGE integer, EMAIL text, PICTURE BLOB);";
    public SQLiteDatabase db;
    private final Context context;
    private DatabaseHelper databaseHelper;

    public DataBaseAdapter(Context context) {
        this.context = context;
        databaseHelper = new DatabaseHelper(this.context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DataBaseAdapter open() throws SQLException {
        db = databaseHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        db.close();
    }

    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }

    public void InsertEntry(String username, String password, String name, String email, String age, byte[] image) {
        ContentValues newValues = new ContentValues();
        newValues.put("USERNAME",username);
        newValues.put("PASSWORD",password);
        newValues.put("NAME",name);
        newValues.put("EMAIL",email);
        newValues.put("AGE",Integer.parseInt(age));
        newValues.put("PICTURE",image);
        db.insert("LOGIN", null, newValues);
    }

    public int deleteEntry(String UserName){
        String where = "USERNAME=?";
        int numberOfEntriesDeleted = db.delete("LOGIN",where, new String[]{UserName});
        return numberOfEntriesDeleted;
    }

    public String getPasswordEntry(String UserName){
        Cursor cursor = db.query("LOGIN", null, "USERNAME=?", new String[]{UserName},null,null,null);
        if(cursor.getCount()<1){
            cursor.close();
            return "NOT EXIST";
        }
        cursor.moveToFirst();
        String password = cursor.getString(cursor.getColumnIndex("PASSWORD"));
        cursor.close();
        return password;

    }

    public String getEmailEntry(String UserName){
        Cursor cursor = db.query("LOGIN", null, "USERNAME=?", new String[]{UserName},null,null,null);
        if(cursor.getCount()<1){
            cursor.close();
            return "NOT EXIST";
        }
        cursor.moveToFirst();
        String email = cursor.getString(cursor.getColumnIndex("EMAIL"));
        cursor.close();
        return email;

    }

    public String getNameEntry(String UserName){
        Cursor cursor = db.query("LOGIN", null, "USERNAME=?", new String[]{UserName},null,null,null);
        if(cursor.getCount()<1){
            cursor.close();
            return "NOT EXIST";
        }
        cursor.moveToFirst();
        String name = cursor.getString(cursor.getColumnIndex("NAME"));
        cursor.close();
        return name;

    }

    public String getAgeEntry(String UserName){
        Cursor cursor = db.query("LOGIN", null, "USERNAME=?", new String[]{UserName},null,null,null);
        if(cursor.getCount()<1){
            cursor.close();
            return "NOT EXIST";
        }
        cursor.moveToFirst();
        String age = cursor.getString(cursor.getColumnIndex("AGE"));
        cursor.close();
        return age;

    }

    public String[] getAllNames(){
        List<String> list = new ArrayList<String>();
        Cursor cursor = db.rawQuery("SELECT * FROM LOGIN",null);
        if(cursor.moveToFirst()){
            while (cursor.moveToNext()) {
                list.add(cursor.getString(3));
            }
        }
        String[] names = new String[list.size()];
        int i =0;
        for(String s: list){
            names[i] = s.toString();
            i++;
        }
        return names;
    }

    public String[] getAllEmails(){
        List<String> list = new ArrayList<String>();
        Cursor cursor = db.rawQuery("SELECT * FROM LOGIN",null);
        if(cursor.moveToFirst()){
            while (cursor.moveToNext()) {
                list.add(cursor.getString(5));
            }
        }
        String[] emails = new String[list.size()];
        int i =0;
        for(String s: list){
            emails[i] = s.toString();
            i++;
        }
        return emails;
    }

    public void updateEntry(String UserName, String Password, String Name, String Email, String Age){
        ContentValues updatedValues = new ContentValues();
        updatedValues.put("USERNAME",UserName);
        updatedValues.put("PASSWORD",Password);
        updatedValues.put("NAME",Name);
        updatedValues.put("EMAIL",Email);
        updatedValues.put("AGE",Integer.parseInt(Age));
        String where = "USERNAME=?";
        db.update("LOGIN",updatedValues,where,new String[]{UserName});
    }

    public void updateImage(byte[] image, String Username) {
        ContentValues updatedValues = new ContentValues();
        updatedValues.put("PICTURE",image);
        String where = "USERNAME=?";
        db.update("LOGIN",updatedValues,where,new String[]{Username});
    }

//    public Bitmap receiveImage(String Username){
//        String where = "USERNAME=?";
//        Cursor cursor = db.rawQuery("SELECT * FROM LOGIN",new String[]{where}); //CRASHING...
//        byte[] image = cursor.getBlob(6);
//        Bitmap bitmap = DbBitmapUtility.getImage(image);
//        cursor.close();
//        return bitmap;
//    }

    public Bitmap receiveImage(String Username){
        Cursor cursor = db.query("LOGIN", null, "USERNAME=?", new String[]{Username},null,null,null);
        if(cursor.getCount()<1){
            cursor.close();
            return null;
        }
        cursor.moveToFirst();
        byte[] image = cursor.getBlob(cursor.getColumnIndex("PICTURE"));
        cursor.close();
        Bitmap bitmap = DbBitmapUtility.getImage(image);
        return bitmap;

    }
}
