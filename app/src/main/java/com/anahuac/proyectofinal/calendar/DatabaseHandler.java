package com.anahuac.proyectofinal.calendar;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.util.Calendar;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String NAME = "CalendarDatabase";
    private static final String CALENDAR_TABLE = "calendar";
    private static final String ID = "id";
    private static final String EVENT = "event";
    private static final String DATE = "date";
    private static final String TIME = "time";
    private static final String CREATE_CALENDAR_TABLE = "CREATE TABLE " + CALENDAR_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DATE + " TEXT, "
            + EVENT + " TEXT, " + TIME + " TEXT)";

    private SQLiteDatabase db;

    public DatabaseHandler(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_CALENDAR_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + CALENDAR_TABLE);
        // Create tables again
        onCreate(db);
    }

    public void openDatabase() {
        db = this.getWritableDatabase();
    }

    public void insertEvent(EventModel event){
        ContentValues cv = new ContentValues();
        cv.put(EVENT, event.getEvent());
        cv.put(DATE, event.getDate());
        cv.put(TIME, event.getTime());
        db.insert(CALENDAR_TABLE, null, cv);
    }

    @SuppressLint("Range")
    public List<EventModel> getAllEvents(String date){
        List<EventModel> eventList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try{
            cur = db.rawQuery("SELECT * FROM "+ CALENDAR_TABLE + " WHERE "+ DATE + " = '" + date + "'", null);
            if(cur != null){
                if(cur.moveToFirst()){
                    do{
                        EventModel event = new EventModel();
                        event.setId(cur.getInt(cur.getColumnIndex(ID)));
                        event.setEvent(cur.getString(cur.getColumnIndex(EVENT)));
                        event.setDate(cur.getString(cur.getColumnIndex(DATE)));
                        event.setTime(cur.getString(cur.getColumnIndex(TIME)));
                        eventList.add(event);
                    }
                    while(cur.moveToNext());
                }
            }
        }
        finally {
            db.endTransaction();
            assert cur != null;
            cur.close();
        }
        return eventList;
    }

    public void updateTime(int id, String time){
        ContentValues cv = new ContentValues();
        cv.put(TIME, time);
        db.update(CALENDAR_TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void updateEvent(int id, String event) {
        ContentValues cv = new ContentValues();
        cv.put(EVENT, event);
        db.update(CALENDAR_TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void updateDate(int id, String date) {
        ContentValues cv = new ContentValues();
        cv.put(DATE, date);
        db.update(CALENDAR_TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void deleteEvent(int id){
        db.delete(CALENDAR_TABLE, ID + "= ?", new String[] {String.valueOf(id)});
    }
}
