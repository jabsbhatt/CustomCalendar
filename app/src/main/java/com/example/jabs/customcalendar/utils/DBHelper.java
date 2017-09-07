package com.example.jabs.customcalendar.utils;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.jabs.customcalendar.Property.Events;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "EventDB";
    public static final String EVENTS_TABLE_NAME = "events";
    public static final String EVENTS_ID = "Id";
    public static final String EVENTS_NAME = "Name";
    public static final String EVENTS_FROMDATE = "FromDate";
    public static final String EVENTS_FROMTIME = "FromTime";
    public static final String EVENTS_TODATE = "ToDate";
    public static final String EVENTS_TOTIME = "ToTime";
    public static final String EVENTS_LOCATION = "Location";
    public static final String EVENTS_DESCRIPTION = "Description";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table events " +
                        "(Id integer primary key,Name text,FromDate DATE,FromTime TIME,ToDate DATE,ToTime TIME,Location text,Description text)"
        );
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS events");
        onCreate(db);
    }

    public long insertEvent(Events event) {
        long id;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", event.getEventsName());
        contentValues.put("fromdate", event.getEventFromDate());
        contentValues.put("fromtime", event.getEventFromTime());
        contentValues.put("todate", event.getEventToDate());
        contentValues.put("totime", event.getEventToTime());
        contentValues.put("location", event.getEventLocation());
        contentValues.put("description", event.getEventDiscription());
        id = db.insert("events", null, contentValues);

        return id;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from events where id=" + id + "", null);
        return res;
    }

    public boolean updateEvents(Integer id, Events event) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", event.getEventsName());
        contentValues.put("fromdate", event.getEventFromDate());
        contentValues.put("fromtime", event.getEventFromTime());
        contentValues.put("todate", event.getEventToDate());
        contentValues.put("totime", event.getEventToTime());
        contentValues.put("location", event.getEventLocation());
        contentValues.put("description", event.getEventDiscription());
        db.update("events", contentValues, "id = ? ", new String[]{Integer.toString(id)});
        return true;
    }

    public Integer deleteEvent(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("events",
                "id = ? ",
                new String[]{Integer.toString(id)});
    }

    public ArrayList<String> getAllEvents() {
        ArrayList<String> array_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from events", null);
        res.moveToFirst();
        if (res != null && res.moveToFirst()) {
            while (res.isAfterLast() == false) {
                array_list.add(res.getString(res.getColumnIndex(EVENTS_FROMDATE)));
                res.moveToNext();
            }
        }
        return array_list;
    }

    public ArrayList<String> getPopEvent(String date) {
        ArrayList<String> array_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from events where FromDate=?", new String[]{date});
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex(EVENTS_NAME)));
            res.moveToNext();
        }
        return array_list;
    }

    public Cursor getEventTime(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select FromTime,ToTime,FromDate,ToDate from events where Id=?", new String[]{Integer.toString(id)});
        if (res != null) {
            res.moveToFirst();
        }
        return res;
    }


    public Cursor getPopupEventDetail(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select FromTime,ToTime,Name,Id from events where FromDate=?", new String[]{date});
        return res;
    }

    public Cursor getDetailOfEvent(String id, String event) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from events where Id=? and Name=?", new String[]{String.valueOf(id), String.valueOf(event)});
        return res;

    }

    public Cursor getDetailOfEventForNotification(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select Name,Location from events where Id=? ", new String[]{String.valueOf(id)});
        return res;

    }


}
