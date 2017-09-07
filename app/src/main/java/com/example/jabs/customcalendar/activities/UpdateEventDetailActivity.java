package com.example.jabs.customcalendar.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.jabs.customcalendar.Property.Events;
import com.example.jabs.customcalendar.R;
import com.example.jabs.customcalendar.utils.CommonData;
import com.example.jabs.customcalendar.utils.DBHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UpdateEventDetailActivity extends AppCompatActivity {

    private String returnedEventName, returenedEventID;
    DBHelper mydb;
    private int myear;
    private int mmonth;
    private int mday;
    static final int FROMDATE_DIALOG_ID = 0;
    static final int TODATE_DIALOG_ID = 1;
    static final int FROMTIME_DIALOG_ID = 2;
    static final int TOTIME_DIALOG_ID = 3;
    private int hour;
    private int minute;
    private EditText updateeventname, updateeventlocation, updateeventdescription;
    String evName, evFDate, evTDate, evFTime, evTTime, evLocation, evDescription, evReminder;
    TextView txtupdateFromdate, txtupdateFromTime, txtupdateToDate, txtupdateToTime;
    int cal_year, cal_month, cal_day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_event_detail);
        updateeventname = (EditText) findViewById(R.id.updateeventname);
        txtupdateFromdate = (TextView) findViewById(R.id.txtupdateFromdate);
        txtupdateFromTime = (TextView) findViewById(R.id.txtupdateFromTime);
        txtupdateToDate = (TextView) findViewById(R.id.txtupdateToDate);
        txtupdateToTime = (TextView) findViewById(R.id.txtupdateToTime);
        updateeventlocation = (EditText) findViewById(R.id.updateeventlocation);
        updateeventdescription = (EditText) findViewById(R.id.updateeventdescription);
        mydb = new DBHelper(UpdateEventDetailActivity.this);
        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            returenedEventID = bundle.getString("EventID");
            returnedEventName = bundle.getString("EventName");

        }
        setCurrentDateOnView();
        assignValuesFromSQLite();


    }

    public void setCurrentDateOnView() {
        final Calendar c = Calendar.getInstance();
        myear = c.get(Calendar.YEAR);
        mmonth = c.get(Calendar.MONTH);
        mday = c.get(Calendar.DAY_OF_MONTH);
    }

    private void assignValuesFromSQLite() {
        Cursor rs = mydb.getDetailOfEvent(returenedEventID, returnedEventName);
        if (rs != null && rs.moveToFirst()) {
            evName = rs.getString(rs.getColumnIndex(DBHelper.EVENTS_NAME));
            evFDate = rs.getString(rs.getColumnIndex(DBHelper.EVENTS_FROMDATE));
            evTDate = rs.getString(rs.getColumnIndex(DBHelper.EVENTS_TODATE));
            evFTime = rs.getString(rs.getColumnIndex(DBHelper.EVENTS_FROMTIME));
            evTTime = rs.getString(rs.getColumnIndex(DBHelper.EVENTS_TOTIME));
            evLocation = rs.getString(rs.getColumnIndex(DBHelper.EVENTS_LOCATION));
            evDescription = rs.getString(rs.getColumnIndex(DBHelper.EVENTS_DESCRIPTION));
            updateeventname.setText(evName);
            txtupdateFromdate.setText(evFDate);
            txtupdateFromTime.setText(convert24to12(evFTime));
            txtupdateToDate.setText(evTDate);
            txtupdateToTime.setText(convert24to12(evTTime));
            updateeventlocation.setText(evLocation);
            updateeventdescription.setText(evDescription);
        }

        if (!rs.isClosed()) {
            rs.close();
        }

    }

    public void clickUpdateFromDate(View v) {
        showDialog(FROMDATE_DIALOG_ID);

    }

    public void clickUpdateFromTime(View v) {
        showDialog(FROMTIME_DIALOG_ID);
    }

    public void clickUpdateToDate(View v) {
        showDialog(TODATE_DIALOG_ID);
    }

    public void clickUpdateToTime(View v) {
        showDialog(TOTIME_DIALOG_ID);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {

            case FROMDATE_DIALOG_ID:
                return new DatePickerDialog(this, FromPickerListener, myear, mmonth,
                        mday) {

                };


            case FROMTIME_DIALOG_ID:

                return new

                        TimePickerDialog(this, fromtimePickerListener, hour, minute,
                        false);

            case TODATE_DIALOG_ID:
                final DatePickerDialog _TOdate = new DatePickerDialog(this, ToPickerListener, myear, mmonth,
                        mday) {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (year < myear)
                            view.updateDate(myear, mmonth, mday);
                        if (monthOfYear < mmonth && year == myear)
                            view.updateDate(myear, mmonth, mday);
                        if (dayOfMonth < mday && year == myear && monthOfYear == mmonth)
                            view.updateDate(myear, mmonth, mday);
                    }
                };
                return _TOdate;


            case TOTIME_DIALOG_ID:

                return new

                        TimePickerDialog(this, totimePickerListener, hour, minute,
                        false);
        }

        return null;
    }

    private DatePickerDialog.OnDateSetListener FromPickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            myear = selectedYear;
            mmonth = selectedMonth;
            mday = selectedDay;
            txtupdateFromdate.setText(new StringBuilder().append(myear).append("-").append(mmonth + 1)
                    .append("-").append(mday));


        }
    };
    private TimePickerDialog.OnTimeSetListener fromtimePickerListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
            // TODO Auto-generated method stub
            hour = hourOfDay;
            minute = minutes;
            updateTimeFrom(hour, minute);
        }

    };
    private DatePickerDialog.OnDateSetListener ToPickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            myear = selectedYear;
            mmonth = selectedMonth;
            mday = selectedDay;
            txtupdateToDate.setText(new StringBuilder().append(myear).append("-").append(mmonth + 1)
                    .append("-").append(mday));


        }
    };
    private TimePickerDialog.OnTimeSetListener totimePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
            // TODO Auto-generated method stub
            hour = hourOfDay;
            minute = minutes;
            updateTimeTO(hour, minute);
        }

    };

    private void updateTimeTO(int hours, int mins) {
        String timeSet = "";

        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";
        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();
        txtupdateToTime.setText(aTime);

    }

    private void updateTimeFrom(int hours, int mins) {

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";


        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();
        txtupdateFromTime.setText(aTime);

    }


    public void clickCancelUpdate(View v) {
        Intent itoisplay = new Intent(UpdateEventDetailActivity.this, MainActivity.class);
        itoisplay.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(itoisplay);
        this.finish();
    }

    public void ClickUpdate(View v) {
        Events ev = new Events();
        ev.setEventsName(updateeventname.getText().toString());
        ev.setEventFromDate(txtupdateFromdate.getText().toString().trim());
        ev.setEventFromTime(convert12to24(txtupdateFromTime.getText().toString().trim()));
        ev.setEventToDate(txtupdateToDate.getText().toString().trim());
        ev.setEventToTime(convert12to24(txtupdateToTime.getText().toString().trim()));
        ev.setEventLocation(updateeventlocation.getText().toString());
        ev.setEventDiscription(updateeventdescription.getText().toString());

        mydb.updateEvents(Integer.parseInt(returenedEventID), ev);
        setCalendar();
        Toast.makeText(UpdateEventDetailActivity.this, getResources().getString(R.string.eventupdated), Toast.LENGTH_SHORT).show();
        mydb.close();
        Intent itoHour = new Intent(UpdateEventDetailActivity.this, MainActivity.class);
        itoHour.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(itoHour);
        finish();
    }


    public void setCalendar() {
        Calendar cal = Calendar.getInstance();

        int fromcontent = txtupdateFromdate.getText().toString().trim().length();
        if (fromcontent == 10) {
            cal_year = Integer.parseInt(txtupdateFromdate.getText().toString().trim().substring(0, 4).trim());
            cal_month = Integer.parseInt(txtupdateFromdate.getText().toString().trim().substring(5, 7).trim());
            cal_day = Integer.parseInt(txtupdateFromdate.getText().toString().trim().substring(8, 10).trim());
        } else if (fromcontent == 9) {
            cal_year = Integer.parseInt(txtupdateFromdate.getText().toString().trim().substring(0, 4).trim());
            cal_month = Integer.parseInt(txtupdateFromdate.getText().toString().trim().substring(5, 6).trim());
            cal_day = Integer.parseInt(txtupdateFromdate.getText().toString().trim().substring(7, 9).trim());
        } else if (fromcontent == 8) {
            cal_year = Integer.parseInt(txtupdateFromdate.getText().toString().trim().substring(0, 4).trim());
            cal_month = Integer.parseInt(txtupdateFromdate.getText().toString().trim().substring(5, 6).trim());
            cal_day = Integer.parseInt(txtupdateFromdate.getText().toString().trim().substring(7, 8).trim());
        }


        String CurrentString = convert12to24(txtupdateFromTime.getText().toString().trim());
        String[] separated = CurrentString.split(":");
        String shr = separated[0];
        String smin = separated[1].substring(0, 2);


        int calc_hour = Integer.parseInt(shr);
        int calc_min = Integer.parseInt(smin);


        cal.set(cal_year, (cal_month - 1), cal_day, calc_hour, calc_min, 00);
        CommonData.setAlarm(UpdateEventDetailActivity.this, cal, Integer.parseInt(returenedEventID));

    }

    public static String convert12to24(String time) {
        String convertedTime = "";
        try {
            SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");
            Date date = parseFormat.parse(time);
            convertedTime = displayFormat.format(date);
        } catch (final ParseException e) {
            e.printStackTrace();
        }
        return convertedTime;

    }

    public static String convert24to12(String time) {
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
    public void onBackPressed() {
        Intent itoHour = new Intent(UpdateEventDetailActivity.this, MainActivity.class);
        itoHour.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(itoHour);
        this.finish();
        super.onBackPressed();
        mydb.close();
    }

}
