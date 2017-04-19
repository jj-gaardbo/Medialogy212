package com.example.jensjakupgaardbo.medialogy212;

//This is the database handler, it does what its name implies,
//it has a whole bunch of mysql in it, if you have any questions you can bring em to me(Rasmus)
//though i dont know much of this either


// This class handles all the database activities
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;

public class AlarmDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "alarmDB.db"; // names the file that the data is saved to
    public static final String TABLE_ALARMS = "alarms";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ALARMNAME = "alarmname";


    public AlarmDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_ALARMS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ALARMNAME + " TEXT " +
                ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALARMS);
        onCreate(db);
    }

    //add a new row to database
    public void addAlarm(Alarm alarm){
        ContentValues values = new ContentValues();
        values.put(COLUMN_ALARMNAME, alarm.get_alarmname() );
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_ALARMS,null,values);
        db.close();
    }

    //delete an alarm from the database
    public void deleteAlarm(String alarmname){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_ALARMS + " WHERE " + COLUMN_ALARMNAME + "=\"" + alarmname + "\";");
    }

    public String databaseToString() {
        String dbString = "";


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


                dbString += recordSet.getString(recordSet.getColumnIndex("alarmname"));
                dbString += "\n";
            }

            recordSet.moveToNext();
        }
        db.close();

        return dbString;
    }


}
