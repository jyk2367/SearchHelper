package com.example.searchhelper;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Parcel;
import android.os.Parcelable;
import java.io.Serializable;
import java.util.ArrayList;

public class DBOpenHelper implements Serializable,Parcelable {

    private static final String DATABASE_NAME = "MyURLData.db";
    private static final int DATABASE_VERSION = 1;
    public static SQLiteDatabase mDB;
    private DatabaseHelper mDBHelper;
    private Context mCtx;
    private static DBOpenHelper instance;

    public static DBOpenHelper getInstance(Activity activity) {
        if(instance==null)
            instance=new DBOpenHelper(activity);
        return instance;
    }

    protected DBOpenHelper(Parcel in) {
    }

    public static final Creator<DBOpenHelper> CREATOR = new Creator<DBOpenHelper>() {
        @Override
        public DBOpenHelper createFromParcel(Parcel in) {
            return new DBOpenHelper(in);
        }

        @Override
        public DBOpenHelper[] newArray(int size) {
            return new DBOpenHelper[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }

    private static class DatabaseHelper extends SQLiteOpenHelper implements Serializable {

        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            db.execSQL(DataBases.CreateDB.getCreate0());

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            db.execSQL("DROP TABLE IF EXISTS "+DataBases.CreateDB._TABLENAME0);
            onCreate(db);
        }

    }

    public DBOpenHelper(Context context){
        this.mCtx = context;
    }

    public DBOpenHelper open() throws SQLException {
        mDBHelper = new DatabaseHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);
        mDB = mDBHelper.getWritableDatabase();
        return this;
    }

    public void create(){
        mDBHelper.onCreate(mDB);
    }
    public void close(){
        mDB.close();
    }


    public long insertColumn(String userid, String title, String url , String dates, String contents){
        ContentValues values = new ContentValues();
        Cursor c=mDB.rawQuery("SELECT * FROM "+DataBases.CreateDB._TABLENAME0+" WHERE url=\""+url+"\"",null);
        if(!c.moveToFirst()) {
            values.put(DataBases.CreateDB.USERID, userid);
            values.put(DataBases.CreateDB.TITLE, title);
            values.put(DataBases.CreateDB.URL, url);
            values.put(DataBases.CreateDB.DATE, dates);
            values.put(DataBases.CreateDB.CONTENTS,contents);
            return mDB.insertWithOnConflict(DataBases.CreateDB._TABLENAME0, null, values,SQLiteDatabase.CONFLICT_REPLACE);
        }
        return -1;
    }

    //Select DB
    public Cursor selectColumns(){
        return mDB.query(DataBases.CreateDB._TABLENAME0, null, null, null, null, null, null);
    }

    //Update DB
    public boolean updateColumn(String userid, String title, String url , String dates,String contents){
        ContentValues values = new ContentValues();
        values.put(DataBases.CreateDB.USERID, userid);
        values.put(DataBases.CreateDB.TITLE, title);
        values.put(DataBases.CreateDB.URL, url);
        values.put(DataBases.CreateDB.DATE, dates);
        values.put(DataBases.CreateDB.CONTENTS,contents);
        return mDB.update(DataBases.CreateDB._TABLENAME0, values, "url=\""+url+"\"", null) > 0;
    }

    // Delete All
    public void deleteAllColumns() {
        mDB.delete(DataBases.CreateDB._TABLENAME0, null, null);
    }

    // Delete Column by ID
    public boolean deleteColumnbyID(long id){
        return mDB.delete(DataBases.CreateDB._TABLENAME0, "_id="+id, null) > 0;
    }

    // Delete Column by URL
    public boolean deleteColumnbyURL(String url){
        return mDB.delete(DataBases.CreateDB._TABLENAME0, "url=\""+url+"\"", null) > 0;
    }


    public ArrayList<String> getTableNameList(){
        Cursor c = mDB.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name!='android_metadata' AND name!='sqlite_sequence' order by name", null);
        ArrayList<String> result=new ArrayList<>();
        if(c.moveToFirst()) {
            while (!c.isAfterLast()) {
                result.add(c.getString(0).toUpperCase());
                c.moveToNext();
            }
        }
        return result;
    }

    public void setTableName(String tableName){
        DataBases.CreateDB.setTableName(tableName);
    }

    public void deleteTable(String tableName){
        mDB.execSQL("DROP TABLE IF EXISTS "+tableName);
    }
}