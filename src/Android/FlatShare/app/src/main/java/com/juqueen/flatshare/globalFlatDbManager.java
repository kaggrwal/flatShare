package com.juqueen.flatshare;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by juqueen on 2/4/2016.
 */
public class globalFlatDbManager extends SQLiteOpenHelper {

    private  static final String TABLE_NAME="GLOBALFLATDATA";
    private  static final String COLUMN_ID="_id";
    private  static final String COLUMN_FLATNAME="name";
    private  static final String COLUMN_FLATNICKNAME="nickname";
    private  static final String COLUMN_FLATID="flatid";
    private  static final String COLUMN_FLATADDRESS="address";
    private  static final String COLUMN_FLATOWNER="owner";
    private  static final String COLUMN_FLATCOVERPIC="coverpic";
    private static Context _ctx;

    //private static String OPERATION_TAG =null;


    private  static final String DATABASE_NAME="GlobalFlat.db";
    private  static final int DATABASE_VERSION=3;

    private static final String DB_CREATE= "CREATE TABLE "+ TABLE_NAME+ "(" + COLUMN_ID + "INTEGER PRIMARY KEY," + COLUMN_FLATNAME + " TEXT,"
             + COLUMN_FLATNICKNAME + " TEXT," + COLUMN_FLATID+ " TEXT," + COLUMN_FLATADDRESS + " TEXT," + COLUMN_FLATOWNER+ " TEXT," + COLUMN_FLATCOVERPIC+ " TEXT" + ");";




    public globalFlatDbManager(Context context) {
     super(new DatabaseContext(context,"/flatshare/data/global/"), DATABASE_NAME, null, DATABASE_VERSION);
     //  super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this._ctx = context;

    }





       public void onCreate(SQLiteDatabase db) {

        db.execSQL(DB_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //drop older table

        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);

        onCreate(db);

    }

    public void addRow(flatData flatDataObject)
    {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_FLATNAME,flatDataObject.getName());
        values.put(COLUMN_FLATNICKNAME,flatDataObject.getNickName());
        values.put(COLUMN_FLATID,flatDataObject.getFlatId());
        values.put(COLUMN_FLATADDRESS,flatDataObject.getAddress());
        values.put(COLUMN_FLATOWNER,flatDataObject.getOwnerName());
        values.put(COLUMN_FLATCOVERPIC, flatDataObject.getCoverPicPath());

        db.insert(TABLE_NAME, null, values);

        db.close();

    }


    public List<flatData> getAllRows()
    {
        List<flatData> flatDatas = new ArrayList<flatData>();

        String selectQuery ="SELECT * FROM "+ TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery,null);
        if(cursor.moveToFirst()){
            do{
                flatData flatData = new flatData();
                flatData.setName(cursor.getString(1));
                flatData.setNickName(cursor.getString(2));
                flatData.setFlatId(cursor.getString(3));
                flatData.setAddress(cursor.getString(4));
                flatData.setOwnerName(cursor.getString(5));
                flatData.setCoverPicPath(cursor.getString(6));

                flatDatas.add(flatData);
            }while (cursor.moveToNext());
        }

        return flatDatas;

    }
}


