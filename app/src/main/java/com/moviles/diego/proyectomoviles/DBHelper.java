package com.moviles.diego.proyectomoviles;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by diego on 22/02/17.
 */

public class DBHelper extends SQLiteOpenHelper{

    private static final String DB_NAME="activities.db",
            TABLE_NAME="ACTIVITY",
            ID_FIELD="ID",
            NOMBRE_FIELD="NOMBRE",
            START_TIME_FIELD="START_TIME",
            START_TIME_DAYS_FIELD="START_TIME_DAYS_FIELD",
            ID_PARENT_FIELD="ID_PARENT_FIELD";
    private static final int DB_VERSION=1;

    public DBHelper(Context c){
        super(c,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String creation ="CREATE TABLE "+TABLE_NAME+" ("+ID_FIELD+" INTEGER PRIMARY KEY, "
                +NOMBRE_FIELD+" TEXT, "
                +START_TIME_FIELD+" DATETIME, " +
                START_TIME_DAYS_FIELD+" DATETIME, "+
                ID_PARENT_FIELD+" INTEGER, "+
                "FOREIGN KEY(" +ID_PARENT_FIELD+") REFERENCES " +TABLE_NAME+"("+ID_FIELD+") )";

        db.execSQL(creation);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String[] name={TABLE_NAME};
        db.execSQL("DROP TABLE IF EXISTS ?",name);
        onCreate(db);
    }

    public void add(String name,String startTime, int parent){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(NOMBRE_FIELD,name);
        cv.put(START_TIME_FIELD,startTime);

        SimpleDateFormat from = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat to = new SimpleDateFormat("yyyy-MM-dd");

        String hours=null;

        try{
            hours = to.format(from.parse(startTime));
        }
        catch(ParseException e){
            e.printStackTrace();
        }

        cv.put(START_TIME_DAYS_FIELD,hours);

        if(parent>=0){
         cv.put(ID_PARENT_FIELD,parent);
        }

        db.insert(TABLE_NAME,null,cv);
    }

    public List<String> find(String date){
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = START_TIME_DAYS_FIELD+" = ?";
        String[] args={date};
        String[] columns={NOMBRE_FIELD,START_TIME_FIELD};
        Cursor c = db.query(TABLE_NAME,columns,selection,args,null,null,null,null);

        List<String> result = new ArrayList<String>();

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            result.add(c.getString(c.getColumnIndex(NOMBRE_FIELD))+","+c.getString(c.getColumnIndex(START_TIME_FIELD)));
        }

        c.close();

        return Collections.unmodifiableList(result);
    }

    public List<String> find(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = ID_FIELD+" = ?";
        String[] args={Integer.toString(id)};
        String[] columns={NOMBRE_FIELD,START_TIME_FIELD};

        Cursor c = db.query(TABLE_NAME,columns,selection,args,null,null,null,null);

        List<String> result= new ArrayList<>();

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            result.add(c.getString(c.getColumnIndex(NOMBRE_FIELD))+","+c.getString(c.getColumnIndex(START_TIME_FIELD)));
        }

        c.close();

        return Collections.unmodifiableList(result);
    }
}
