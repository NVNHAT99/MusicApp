package com.example.musicplayermyown;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Database_Music extends SQLiteOpenHelper {
    public Database_Music(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // sql Create ,Insert,Update Delete
    public void QueryData(String Sql){
        SQLiteDatabase database = getWritableDatabase();
        try{
            database.execSQL(Sql);
        }catch (Exception e){

        }
    }
    public Cursor GetData(String Sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(Sql,null);
    }
    // select


    // create all table need to run app
    public void CreateTable(){

        this.QueryData("CREATE TABLE IF NOT EXISTS PlayList(Id INTEGER PRIMARY KEY AUTOINCREMENT , " +
                "PlayListName NVARCHAR(255) " +
                ")");

        this.QueryData("CREATE TABLE IF NOT EXISTS PlayListDetail(PlayListId INTEGER, " +
                "SongId INTEGER, " +
                " PRIMARY KEY (PlayListId,SongId) " +
                ")");

    }
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
