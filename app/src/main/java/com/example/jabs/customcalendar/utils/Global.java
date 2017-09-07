package com.example.jabs.customcalendar.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.jabs.customcalendar.R;
import com.example.jabs.customcalendar.activities.MainActivity;

/**
 * Created by nct121 on 1/11/2016.
 */
public class Global {
    public static Context mcontext;
    public static DBHelper db;
    public static int notificationid = 1;


    public static void AssignGlobalVariables(Context context) {
        mcontext = context;
    }


    public static void sendNotification(int alarmID) {
        db = new DBHelper(mcontext);
        Cursor rs = db.getDetailOfEventForNotification(alarmID);
        if (rs != null && rs.moveToFirst()) {


            String title = rs.getString(rs.getColumnIndex(DBHelper.EVENTS_NAME));
            String text = rs.getString(rs.getColumnIndex(DBHelper.EVENTS_LOCATION));


            Intent resultIntent = new Intent(mcontext, MainActivity.class);
            Bundle bundle = new Bundle();
//                bundle.putString("EventID", alID);
//                bundle.putString("EventName", title);
            Log.e("Global", "Received AlarmId is =>" + alarmID);
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK);


            resultIntent.putExtras(bundle);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(mcontext);
            stackBuilder.addParentStack(MainActivity.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(mcontext).setContentTitle(title).setContentText(text).setSmallIcon(R.mipmap.ic_launcher);
            builder.setContentIntent(resultPendingIntent);
            builder.setAutoCancel(true);
            builder.setDefaults(Notification.DEFAULT_SOUND
                    | Notification.DEFAULT_VIBRATE | Notification.FLAG_SHOW_LIGHTS);
            NotificationManager mNotificationManager =
                    (NotificationManager) mcontext.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(notificationid++, builder.build());
        }
        if (!rs.isClosed()) {
            rs.close();
            db.close();
        }


    }


}




