package com.juqueen.flatshare;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by juqueen on 7/26/2016.
 */
public class localFlatDbManager extends SQLiteOpenHelper {

    private  static final String TABLE_NAME="LOCALFLATDATA";
    private  static final String COLUMN_ID="_id";
    private  static final String COLUMN_FLATID="FLATID";
    private  static final String COLUMN_MEMBERID="MEMBERID";
    private  static final String COLUMN_MEMBERNAME="MEMBERNAME";
    private  static final String COLUMN_MEMBERDOB="MEMEBERDOB";
    private  static final String COLUMN_MEMEBERDESIGNATION="DESIGNATION";
    private  static Context _ctx;
    public   static String Path = "/flatshare/data/flats/";

    public   static String DATABASE_NAME="LocalFlat.db";
    private   static final int DATABASE_VERSION=2;

    private static final String DB_CREATE= "CREATE TABLE "+ TABLE_NAME+ "(" + COLUMN_ID + "INTEGER PRIMARY KEY," + COLUMN_MEMBERID + " TEXT,"
            + COLUMN_MEMBERNAME + " TEXT," + COLUMN_FLATID+ " TEXT," + COLUMN_MEMBERDOB + " TEXT," + COLUMN_MEMEBERDESIGNATION+ " TEXT" + ");";



    public localFlatDbManager(Context context) {


       super(new DatabaseContext(context,Path), DATABASE_NAME, null, DATABASE_VERSION);
         // super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this._ctx = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(DB_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);

    }


    public void addRow(flatMemberData flatDataObject)
    {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_MEMBERDOB,flatDataObject.getMemberDOB());
        values.put(COLUMN_MEMBERNAME,flatDataObject.getMemberName());
        values.put(COLUMN_FLATID,flatDataObject.getFlatId());
        values.put(COLUMN_MEMBERID,flatDataObject.getMemberID());
        values.put(COLUMN_MEMEBERDESIGNATION,flatDataObject.getMemberDesignation());

        db.insert(TABLE_NAME, null, values);

        db.close();

    }


    public List<flatMemberData> getAllRows()
    {
        List<flatMemberData> flatMemberDatas = new ArrayList<flatMemberData>();

        String selectQuery ="SELECT * FROM "+ TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery,null);
        if(cursor.moveToFirst()){
            do{
                flatMemberData flatMemberData = new flatMemberData();
                flatMemberData.setMemberID(cursor.getString(1));
                flatMemberData.setMemberName(cursor.getString(2));
                flatMemberData.setFlatId(cursor.getString(3));
                flatMemberData.setMemberDOB(cursor.getString(4));
                flatMemberData.setMemberDesignation(cursor.getString(1));


                flatMemberDatas.add(flatMemberData);
            }while (cursor.moveToNext());
        }

        return flatMemberDatas;

    }
}


