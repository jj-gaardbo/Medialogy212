package com.example.jensjakupgaardbo.medialogy212;
//// TODO: 19-04-2017 Latitude cannot store negative coordiantes, wheres longitude can


//This is the database handler, it does what its name implies,
//it has a whole bunch of mysql in it, if you have any questions you can bring em to me(Rasmus)
//though i dont know much of this either


// This class handles all the database activities

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

public class AlarmDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 8;
    private static final String DATABASE_NAME = "alarmDB.db"; // names the file that the data is saved to
    public static final String TABLE_ALARMS = "alarms";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ALARMNAME = "alarmname";
    public static final String COLUMN_DATA = "data";




    public AlarmDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE IF NOT EXISTS" + TABLE_ALARMS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ALARMNAME + " TEXT, " +
                COLUMN_DATA + " TEXT " +
                ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALARMS);
        onCreate(db);
    }

    public boolean alarmExists(String name){
        boolean exists = false;
        ArrayList<String> names = getAlarmNames();
        for(int i = 0; i < names.size(); i++){
            if(names.get(i).equals(name)){
                exists = true;
            }
        }
        return exists;
    }

    //add a new row to database
    public void addAlarm(Alarm alarm){
        ContentValues values = new ContentValues();
        values.put(COLUMN_ALARMNAME, alarm.get_alarmname() );
        Gson gson = new GsonBuilder().create();
        String string = gson.toJson(alarm);
        values.put(COLUMN_DATA, string);
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_ALARMS,null,values);
        db.close();
    }

    //delete an alarm from the database
    public void deleteAlarm(String alarmname){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_ALARMS + " WHERE " + COLUMN_ALARMNAME + "=\"" + alarmname + "\";");
    }

    public void updateAlarm(String alarmname, Alarm alarm){
        SQLiteDatabase db = getWritableDatabase();
        Gson gson = new GsonBuilder().create();
        String alarmString = gson.toJson(alarm);
        db.execSQL("UPDATE "+ TABLE_ALARMS + " SET " + COLUMN_DATA + "=\""+ alarmString + "\" WHERE" + COLUMN_ALARMNAME + "=\"" + alarmname + "\"");
    }

    public ArrayList<String> getAlarmNames() {
        ArrayList<String> alarmNames = new ArrayList<>();

        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_ALARMS + " WHERE 1";

        //Cursor points to a location in your results
        Cursor recordSet = db.rawQuery(query, null);
        //Move to the first row in your results
        recordSet.moveToFirst();

        //Position after the last row means the end of the results
        while (!recordSet.isAfterLast()) {

            // null could happen if we used our empty constructor
            if (recordSet.getString(recordSet.getColumnIndex("alarmname")) != null) {


                alarmNames.add(
                        recordSet.getString(recordSet.getColumnIndex("alarmname"))
                );
            }

            recordSet.moveToNext();
        }
        db.close();
        return alarmNames;
    }


    public ArrayList<Alarm> getAlarms(){                //returns all alarms in the database as an arraylist
        ArrayList<Alarm> alarms = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Gson gson = new GsonBuilder().create();

        String query = "SELECT * FROM " + TABLE_ALARMS + " WHERE 1";
        //Cursor points to a location in your results
        Cursor recordSet = db.rawQuery(query, null);
        //Move to the first row in your results
        recordSet.moveToFirst();

        //Position after the last row means the end of the results
        while (!recordSet.isAfterLast()) {

            // null could happen if we used our empty constructor
            if (recordSet.getString(recordSet.getColumnIndex("alarmname")) != null) {
                String alarmData = recordSet.getString(recordSet.getColumnIndex("data"));
                Alarm gottenAlarm = gson.fromJson(alarmData,Alarm.class);
                alarms.add(gottenAlarm);


                recordSet.moveToNext();
            }
        }
        db.close();


        return alarms;
    }



}
