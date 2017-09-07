package com.example.jabs.customcalendar.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.Calendar;

/**
 * Created by jabs on 1/9/2016.
 */
public class CommonData {
    public static int counter;

    public static void setAlarm(Context ctx, Calendar targetCal, int RQS_1) {
        Intent intent = new Intent(ctx, AlarmReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putInt("alrm_ID", RQS_1);
        intent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, RQS_1, intent, 0);
        AlarmManager alarmManager = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);
        counter = RQS_1;
        RQS_1++;

    }


    public static void cancelAlarm(Context ctx, int RQS_1) {
        Intent intentlocal = new Intent(ctx, AlarmReceiver.class);
        PendingIntent pilocal = PendingIntent.getBroadcast(ctx,
                RQS_1, intentlocal, 0);
        counter = RQS_1;
        AlarmManager alarmManager = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pilocal);
        pilocal.cancel();

    }


}
