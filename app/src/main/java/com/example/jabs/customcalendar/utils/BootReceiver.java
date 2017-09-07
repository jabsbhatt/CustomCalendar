package com.example.jabs.customcalendar.utils;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * This BroadcastReceiver automatically (re)starts the alarm when the device is
 * rebooted. This receiver is set to be disabled (android:enabled="false") in the
 * application's manifest file. When the user sets the alarm, the receiver is enabled.
 * When the user cancels the alarm, the receiver is disabled, so that rebooting the
 * device will not trigger this receiver.
 */
// BEGIN_INCLUDE(autostart)
public class BootReceiver extends BroadcastReceiver {
    private final String BOOT_COMPLETED_ACTION = "android.intent.action.BOOT_COMPLETED";
    DBHelper db;
    String events_FromDate, events_fromTime, events_ToDate, events_Totime, event_id;
    String compareYear, compareMonth, compareDay;
    String compareHour, compareMin;
    Context cx;
    String TAG = "BootReceiver";


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equalsIgnoreCase(BOOT_COMPLETED_ACTION)) {
            if (intent.getAction().equals(BOOT_COMPLETED_ACTION)) {

                cx = context;
                checkTime(cx);

//				alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),interval, pendingIntent);
            }

        }
    }

    private void checkTime(Context cx) {
        db = new DBHelper(cx);
        ArrayList eventList = db.getAllEvents();
        if (eventList != null) {
            for (int i = 1; i <= eventList.size(); i++) {
                Cursor c = db.getData(i);
                if (c != null && c.moveToFirst()) {
                    event_id = c.getString(c.getColumnIndex(DBHelper.EVENTS_ID));
                    events_FromDate = c.getString(c.getColumnIndex(DBHelper.EVENTS_FROMDATE));
                    events_fromTime = c.getString(c.getColumnIndex(DBHelper.EVENTS_FROMTIME));
                    events_ToDate = c.getString(c.getColumnIndex(DBHelper.EVENTS_TODATE));
                    events_Totime = c.getString(c.getColumnIndex(DBHelper.EVENTS_TOTIME));


                    String CurrentString = events_fromTime.trim();
                    String[] separated = CurrentString.split(":");
                    String shr = separated[0];
                    String smin = separated[1].substring(0, 2);


                    int setHour = Integer.parseInt(shr);
                    int setMinute = Integer.parseInt(smin);


                    Calendar calendar = Calendar.getInstance();
                    int yr = calendar.get(Calendar.YEAR);
                    int mnth = calendar.get(Calendar.MONTH) + 1;
                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                    int hourofday = calendar.get(Calendar.HOUR_OF_DAY);
                    int min = calendar.get(Calendar.MINUTE);

                    compareYear = String.valueOf(yr);
                    compareMonth = String.valueOf(mnth);
                    compareDay = String.valueOf(day);
                    compareHour = String.valueOf(hourofday);
                    compareMin = String.valueOf(min);


                    String finalFromTime = shr + ":" + smin;
                    String finalTo = compareYear.concat("-" + compareMonth).concat("-" + compareDay).concat(" " + compareHour.concat(":" + compareMin).trim());

                    String fromDate = events_FromDate.concat(" " + finalFromTime);

                    int fromDtLength = events_FromDate.toString().trim().length();

//                    String toDate = events_ToDate.concat(" " + events_Totime);


//                    int toDateLength = toDate.toString().trim().length();

                    try {
                        if (fromDtLength == 10) {
                            SimpleDateFormat dateFormatFrom = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            SimpleDateFormat dateFormatTo = new SimpleDateFormat("yyyy-MM-dd HH:mm");

                            int setYear = Integer.parseInt(events_FromDate.substring(0, 4).trim());
                            int setMonth = Integer.parseInt(events_FromDate.substring(5, 7).trim());
                            int setDay = Integer.parseInt(events_FromDate.substring(8, 10).trim());

                            Date strDate = dateFormatFrom.parse(fromDate);
                            Date strTo = dateFormatTo.parse(finalTo);
                            if (strDate.compareTo(strTo) <= 0) {
                                CommonData.cancelAlarm(cx, Integer.parseInt(event_id));
                            } else {
                                Calendar cal = Calendar.getInstance();
                                cal.set(setYear, (setMonth - 1), setDay, setHour, setMinute, 00);
                                CommonData.setAlarm(cx, cal, Integer.parseInt(event_id));
                            }
                        } else if (fromDtLength == 9) {
                            SimpleDateFormat dateFormatFrom = new SimpleDateFormat("yyyy-M-dd HH:mm");
                            SimpleDateFormat dateFormatTo = new SimpleDateFormat("yyyy-M-dd HH:mm");

                            int setYear = Integer.parseInt(events_FromDate.substring(0, 4).trim());
                            int setMonth = Integer.parseInt(events_FromDate.substring(5, 6).trim());
                            int setDay = Integer.parseInt(events_FromDate.substring(7, 9).trim());


                            Date strDate = dateFormatFrom.parse(fromDate);
                            Date strTo = dateFormatTo.parse(finalTo);
                            if (strDate.compareTo(strTo) <= 0) {
                                CommonData.cancelAlarm(cx, Integer.parseInt(event_id));
                            } else {
                                Calendar cal = Calendar.getInstance();
                                cal.set(setYear, (setMonth - 1), setDay, setHour, setMinute, 00);
                                CommonData.setAlarm(cx, cal, Integer.parseInt(event_id));
                            }


                        } else if (fromDtLength == 8) {
                            SimpleDateFormat dateFormatFrom = new SimpleDateFormat("yyyy-M-d HH:mm");
                            SimpleDateFormat dateFormatTo = new SimpleDateFormat("yyyy-M-d HH:mm");

                            int setYear = Integer.parseInt(events_FromDate.substring(0, 4).trim());
                            int setMonth = Integer.parseInt(events_FromDate.substring(5, 6).trim());
                            int setDay = Integer.parseInt(events_FromDate.substring(7, 8).trim());

                            Date strDate = dateFormatFrom.parse(fromDate);
                            Date strTo = dateFormatTo.parse(finalTo);
                            if (strDate.compareTo(strTo) <= 0) {
                                CommonData.cancelAlarm(cx, Integer.parseInt(event_id));
                            } else {
                                Calendar cal = Calendar.getInstance();
                                cal.set(setYear, (setMonth - 1), setDay, setHour, setMinute, 00);
                                CommonData.setAlarm(cx, cal, Integer.parseInt(event_id));
                            }
                        }
                    } catch (Exception e) {
                    }
                }
                if (!c.isClosed()) {
                    c.close();
                }

            }
        }
    }
}