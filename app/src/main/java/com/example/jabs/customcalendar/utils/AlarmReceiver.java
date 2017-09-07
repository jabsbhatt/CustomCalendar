package com.example.jabs.customcalendar.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Vibrator;

import java.text.SimpleDateFormat;
import java.util.Date;


public class AlarmReceiver extends BroadcastReceiver {

    int returnedAlarmID;
    DBHelper db;
    Context cx;
    private String events_fromTime, events_ToTime, events_fromDate, events_ToDate;


    @Override
    public void onReceive(Context context, Intent intent) {
        Global.AssignGlobalVariables(context);
        cx = context;
        db = new DBHelper(context);

        Bundle bundle = intent.getExtras();
        if (intent != null) {
            returnedAlarmID = bundle.getInt("alrm_ID", CommonData.counter);

            if (checkTime(context)) {
                Vibrator vib = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
                vib.vibrate(2000);
                Global.sendNotification(returnedAlarmID);
            }


        }
    }

    public boolean checkTime(Context cx) {
        try {
            db = new DBHelper(cx);
            Cursor rs = db.getEventTime(returnedAlarmID);
            if (rs != null) {
                for (int i = 1; i <= rs.getCount(); i++) {


                    events_fromTime = rs.getString(rs.getColumnIndex(DBHelper.EVENTS_FROMTIME));
                    events_ToTime = rs.getString(rs.getColumnIndex(DBHelper.EVENTS_TOTIME));
                    events_fromDate = rs.getString(rs.getColumnIndex(DBHelper.EVENTS_FROMDATE));
                    events_ToDate = rs.getString(rs.getColumnIndex(DBHelper.EVENTS_TODATE));


                    String fromDate = events_fromDate.concat(" " + events_fromTime);
                    String toDate = events_ToDate.concat(" " + events_ToTime);

                    int fromDatelength = events_fromDate.toString().trim().length();
                    int toDateLength = events_ToDate.toString().trim().length();
                    if (fromDatelength == 10 || toDateLength == 10) {
                        SimpleDateFormat dateFormatFrom = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        SimpleDateFormat dateFormatTo = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                        Date strDate = dateFormatFrom.parse(fromDate);
                        Date strTo = dateFormatTo.parse(toDate);
                        if (strDate.compareTo(strTo) <= 0 ) {
                            return true;
                        }
                    } else if (fromDatelength == 9 || toDateLength == 9) {
                        SimpleDateFormat dateFormatFrom = new SimpleDateFormat("yyyy-M-dd HH:mm:ss");
                        SimpleDateFormat dateFormatTo = new SimpleDateFormat("yyyy-M-dd HH:mm:ss");

                        Date strDate = dateFormatFrom.parse(fromDate);
                        Date strTo = dateFormatTo.parse(toDate);
                        if (strDate.compareTo(strTo) <= 0 ) {
                            return true;
                        }
                    } else if (fromDatelength == 8 || toDateLength == 8) {
                        SimpleDateFormat dateFormatFrom = new SimpleDateFormat("yyyy-M-d HH:mm:ss");
                        SimpleDateFormat dateFormatTo = new SimpleDateFormat("yyyy-M-d HH:mm:ss");

                        Date strDate = dateFormatFrom.parse(fromDate);
                        Date strTo = dateFormatTo.parse(toDate);
                        if (strDate.compareTo(strTo) <= 0 ) {
                            return true;
                        }
                    }
                }
                if (!rs.isClosed()) {
                    rs.isClosed();
                    db.close();
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;

    }

}




