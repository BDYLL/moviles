package com.moviles.diego.proyectomoviles;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;


/**
 * Created by diego on 22/02/17.
 */

public class DBHelper extends SQLiteOpenHelper{

    private static final String
            DB_NAME="activities2.db",
            TABLE_NAME="ACTIVITY",
            ID_FIELD="ID",
            NOMBRE_FIELD="NOMBRE",
            START_TIME_FIELD="START_TIME",
            START_TIME_DAYS_FIELD="START_TIME_DAYS_FIELD",
            TOTAL_TIME_FIELD="TOTAL_TIME",
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
                TOTAL_TIME_FIELD+" INTEGER DEFAULT 0 , "+
                ID_PARENT_FIELD+" INTEGER, "+
                "FOREIGN KEY(" +ID_PARENT_FIELD+") REFERENCES " +TABLE_NAME+"("+ID_FIELD+") )";


        db.execSQL(creation);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*String[] name={TABLE_NAME};
        db.execSQL("DROP TABLE IF EXISTS ?",name);
        onCreate(db);
        */
        db.delete(TABLE_NAME,null,null);
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

    public Map<String,Integer> getChartInfo(){

        Map<String,Integer> result = new HashMap<>();

        result.put("Lunes",0);
        result.put("Martes",0);
        result.put("Miercoles",0);
        result.put("Jueves",0);
        result.put("Viernes",0);
        result.put("Sabado",0);
        result.put("Domingo",0);


        long DAY_IN_MS = 1000 * 60 * 60 * 24;

        long sevenDays=1000*60*60*24*7;

        Date weekAgo=new Date(System.currentTimeMillis() - (7 * DAY_IN_MS));

        Date now=new Date();

        List<String> all=this.getAll();

        Date date;

        long diff;

        for(String row : all){

            try {


                date=new SimpleDateFormat("yyyy-MM-dd").parse(row.split(",")[1]);

                int time=Integer.parseInt(row.split(",")[2]);

                diff=now.getTime()-date.getTime();

                java.util.Calendar cal = Calendar.getInstance();

                cal.setTime(date);

                if(diff<=sevenDays){

                    int day = cal.get(Calendar.DAY_OF_WEEK);

                    String dayStr="";

                    switch (day){
                        case Calendar.MONDAY:
                            dayStr="Lunes";
                            break;
                        case Calendar.TUESDAY:
                            dayStr="Martes";
                            break;
                        case Calendar.WEDNESDAY:
                            dayStr="Miercoles";
                            break;
                        case Calendar.THURSDAY:
                            dayStr="Jueves";
                            break;
                        case Calendar.FRIDAY:
                            dayStr="Viernes";
                            break;
                        case Calendar.SATURDAY:
                            dayStr="Sabado";
                            break;
                        case Calendar.SUNDAY:
                            dayStr="Domingo";
                            break;
                    }

                    result.put(dayStr,time);

                }


            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        return result;
    }

    public int updateTime(int id, int time){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        int previousTime=this.getTime(id);

        //Log.d("previous",previousTime+"");

        int totalTime=time+previousTime;

        //Log.d("new time",totalTime+"");

        cv.put(TOTAL_TIME_FIELD,totalTime);

        String clause[]={time+""};

        return db.update(TABLE_NAME,cv,ID_FIELD+" = "+id,null);
    }

    public int getTime(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        String selection=ID_FIELD+" = ?";
        String[] args={Integer.toString(id)};
        String[] columns={TOTAL_TIME_FIELD};


        Cursor c = db.query(TABLE_NAME,columns,selection,args,null,null,null,null);

        int time=0;

        if(c.moveToFirst()){
            time+=c.getInt(c.getColumnIndex(TOTAL_TIME_FIELD));
        }


        c.close();
        return time;
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

        return result;
    }


    public int getId(String date){

        SQLiteDatabase db = this.getReadableDatabase();


        String selection = START_TIME_FIELD+" = ?";
        String[] args={date};
        String[] columns={ID_FIELD};
        Cursor c = db.query(TABLE_NAME,columns,selection,args,null,null,null,null);

        int result=-1;

        if(c.moveToFirst()){
            result=c.getInt(c.getColumnIndex(ID_FIELD));
        }

        return result;
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

        return result;
    }

    public List<String> getAll(){
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {NOMBRE_FIELD,START_TIME_FIELD,TOTAL_TIME_FIELD};

        Cursor c = db.query(TABLE_NAME,columns,null,null,null,null,null);

        List<String> result= new ArrayList<>();


        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            result.add(c.getString(c.getColumnIndex(NOMBRE_FIELD))+","
                    +c.getString(c.getColumnIndex(START_TIME_FIELD))+","+c.getInt(c.getColumnIndex(TOTAL_TIME_FIELD)));
        }

        c.close();

        return result;
    }
}
