package com.example.jabs.customcalendar.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.jabs.customcalendar.R;
import com.example.jabs.customcalendar.utils.DBHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DisplayEventDetailActivity extends AppCompatActivity {

    private String returnedEventName, returenedEventID;

    private TextView tvEventName, tvEventdate, tvEventtime, tvEventLocation, tvEventdescription;


    DBHelper mydb;
    String evName, evFDate, evTDate, evFTime, evTTime, evLocation, evDescription, evReminder;
    ImageView imgdelete, imgedit;
    private Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displayevent);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tvEventName = (TextView) findViewById(R.id.tvEventName);
        tvEventdate = (TextView) findViewById(R.id.tvEventdate);
        tvEventtime = (TextView) findViewById(R.id.tvEventtime);
        tvEventLocation = (TextView) findViewById(R.id.tvEventLocation);
        tvEventdescription = (TextView) findViewById(R.id.tvEventdescription);


        mydb = new DBHelper(DisplayEventDetailActivity.this);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            returnedEventName = (bundle.getString("EventName"));
            returenedEventID = (bundle.getString("EventID"));
        }

        Cursor rs = mydb.getDetailOfEvent(returenedEventID, returnedEventName);
        if (rs != null && rs.moveToFirst()) {
            evName = rs.getString(rs.getColumnIndex(DBHelper.EVENTS_NAME));
            evFDate = rs.getString(rs.getColumnIndex(DBHelper.EVENTS_FROMDATE));
            evTDate = rs.getString(rs.getColumnIndex(DBHelper.EVENTS_TODATE));
            evFTime = rs.getString(rs.getColumnIndex(DBHelper.EVENTS_FROMTIME));
            evTTime = rs.getString(rs.getColumnIndex(DBHelper.EVENTS_TOTIME));
            evLocation = rs.getString(rs.getColumnIndex(DBHelper.EVENTS_LOCATION));
            evDescription = rs.getString(rs.getColumnIndex(DBHelper.EVENTS_DESCRIPTION));

            tvEventName.setText(evName);
            if (evTDate.length() < 0 && evTTime.length() < 0) {
                tvEventdate.setText(evFDate);
                tvEventtime.setText(evFTime);
            } else {
                tvEventdate.setText(evFDate + " TO " + evTDate);
                tvEventtime.setText(Convert24to12(evFTime) + " TO " + Convert24to12(evTTime));
            }
            tvEventLocation.setText(evLocation);
            tvEventdescription.setText(evDescription);
        }

        if (!rs.isClosed()) {
            rs.close();
        }


    }


    private void showDeleteDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure....?");

        alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                int i = Integer.parseInt(returenedEventID);
                mydb.deleteEvent(i);
                mydb.close();
                Toast.makeText(DisplayEventDetailActivity.this, DisplayEventDetailActivity.this.getResources().getString(R.string.deleted), Toast.LENGTH_LONG).show();
                Intent itoHour = new Intent(DisplayEventDetailActivity.this, MainActivity.class);
                itoHour.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(itoHour);
                finish();
            }
        });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent itoHour = new Intent(DisplayEventDetailActivity.this, MainActivity.class);
                itoHour.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(itoHour);
                finish();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }






    public static String Convert24to12(String time) {
        String convertedTime = "";
        try {
            SimpleDateFormat displayFormat = new SimpleDateFormat("hh:mm a");
            SimpleDateFormat parseFormat = new SimpleDateFormat("HH:mm:ss");
            Date date = parseFormat.parse(time);
            convertedTime = displayFormat.format(date);
        } catch (final ParseException e) {
            e.printStackTrace();
        }
        return convertedTime;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menueditdelete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_edit) {
            Bundle bundle = new Bundle();
            Intent i = new Intent(DisplayEventDetailActivity.this, UpdateEventDetailActivity.class);
            bundle.putString("EventID", returenedEventID);
            bundle.putString("EventName", returnedEventName);
            i.putExtras(bundle);
            startActivity(i);
            mydb.close();
            finish();

            return true;
        }

        if (id == R.id.action_delete) {
            showDeleteDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent itoHour = new Intent(DisplayEventDetailActivity.this, MainActivity.class);
        itoHour.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(itoHour);
        this.finish();
        super.onBackPressed();


    }



}

