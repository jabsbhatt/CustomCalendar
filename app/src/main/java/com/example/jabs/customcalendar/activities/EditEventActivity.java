package com.example.jabs.customcalendar.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditEventActivity extends AppCompatActivity {

    private EditText editeventname, editeventlocation, editeventdescription;
    private int myear;
    private TextView txtFromdate, txtFromTime, txtToDate, txtToTime;
    private int mmonth;
    private int mday;
    private String returnedDate;

    static final int FROMDATE_DIALOG_ID = 0;
    static final int TODATE_DIALOG_ID = 1;
    static final int FROMTIME_DIALOG_ID = 2;
    static final int TOTIME_DIALOG_ID = 3;
    private int hour;
    private int minute;
    private DBHelper mydb;
    int RQS_1;


    int compareYear, compareMonth, compareDay;
    int cal_year, cal_month, cal_day;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);
        editeventname = (EditText) findViewById(R.id.editeventname);
        editeventlocation = (EditText) findViewById(R.id.editeventlocation);
        editeventdescription = (EditText) findViewById(R.id.editeventdescription);
        txtFromdate = (TextView) findViewById(R.id.txtFromdate);
        txtFromTime = (TextView) findViewById(R.id.txtFromTime);
        txtToDate = (TextView) findViewById(R.id.txtToDate);
        txtToTime = (TextView) findViewById(R.id.txtToTime);


        mydb = new DBHelper(this);
        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            returnedDate = bundle.getString("Date");
            if (returnedDate.length() == 10) {
                compareYear = Integer.parseInt(returnedDate.substring(0, 4).trim());
                compareMonth = Integer.parseInt(returnedDate.substring(5, 7).trim());
                compareDay = Integer.parseInt(returnedDate.substring(8, 10).trim());
            } else if (returnedDate.length() == 9) {
                compareYear = Integer.parseInt(returnedDate.substring(0, 4).trim());
                compareMonth = Integer.parseInt(returnedDate.substring(5, 6).trim());
                compareDay = Integer.parseInt(returnedDate.substring(7, 9).trim());
            } else if (returnedDate.length() == 8) {
                compareYear = Integer.parseInt(returnedDate.substring(0, 4).trim());
                compareMonth = Integer.parseInt(returnedDate.substring(5, 6).trim());
                compareDay = Integer.parseInt(returnedDate.substring(7, 8).trim());
            }
            txtFromdate.setText(returnedDate);
        }

    }


    public void ClickFromDate(View v) {
        showDialog(FROMDATE_DIALOG_ID);
    }

    @Override
    protected Dialog onCreateDialog(int id) {


        switch (id) {

            case FROMTIME_DIALOG_ID:

                return new TimePickerDialog(this, FromtimePickerListener, hour, minute,
                        false);


            case TODATE_DIALOG_ID:
                final DatePickerDialog _toDate = new DatePickerDialog(this, TOPickerListener, compareYear, compareMonth - 1,
                        compareDay) {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (year < compareYear)
                            view.updateDate(compareYear, compareMonth - 1, compareDay);
                        if (monthOfYear < compareMonth - 1 && year == compareYear)
                            view.updateDate(compareYear, compareMonth - 1, compareDay);
                        if (dayOfMonth < compareDay && year == compareYear && monthOfYear == compareMonth - 1)
                            view.updateDate(compareYear, compareMonth - 1, compareDay);
                    }
                };
                return _toDate;


            case TOTIME_DIALOG_ID:

                return new TimePickerDialog(this, totimePickerListener, hour, minute,
                        false);


        }

        return null;
    }

    private DatePickerDialog.OnDateSetListener TOPickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            myear = selectedYear;
            mmonth = selectedMonth + 1;
            mday = selectedDay;
            txtToDate.setText(new StringBuilder().append(myear).append("-").append(mmonth)
                    .append("-").append(mday).append(" "));


        }


    };


    public void ClickFromTime(View v) {
        showDialog(FROMTIME_DIALOG_ID);
    }


    private TimePickerDialog.OnTimeSetListener totimePickerListener = new TimePickerDialog.OnTimeSetListener() {


        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
            // TODO Auto-generated method stub
            hour = hourOfDay;
            minute = minutes;
            updateTimeTO(hour, minute);
        }

    };
    private TimePickerDialog.OnTimeSetListener FromtimePickerListener = new TimePickerDialog.OnTimeSetListener() {


        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
            // TODO Auto-generated method stub
            hour = hourOfDay;
            minute = minutes;
            updateTimeFrom(hour, minute);
        }

    };

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

        txtFromTime.setText(aTime);
    }

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

        txtToTime.setText(aTime);

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

    public void ClickToDate(View v) {
        showDialog(TODATE_DIALOG_ID);
    }


    public void ClickToTime(View v) {
        showDialog(TOTIME_DIALOG_ID);
    }

    public void ClickSubmitEvent(View v) {
        String content_name = editeventname.getText().toString().trim();
        String content_location = editeventlocation.getText().toString().trim();
        String content_description = editeventdescription.getText().toString().trim();
        String content_fromDate = txtFromdate.getText().toString().trim().trim();
        String content_fromTime = convert12to24(txtFromTime.getText().toString().trim());
        String temp_toDate = txtToDate.getText().toString().trim();
        String content_todate = "";
        if (temp_toDate.length() == 10) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date d;
            try {

                d = df.parse(temp_toDate);
                df = new SimpleDateFormat("yyyy-MM-dd");
                content_todate = df.format(d);
            } catch (ParseException e) {

            }

        } else if (temp_toDate.length() == 9) {

            DateFormat df = new SimpleDateFormat("yyyy-M-dd");
            Date d;
            try {

                d = df.parse(temp_toDate);
                df = new SimpleDateFormat("yyyy-M-dd");
                content_todate = df.format(d);
            } catch (ParseException e) {

            }
        } else if (temp_toDate.length() == 8) {

            DateFormat df = new SimpleDateFormat("yyyy-M-d");
            Date d;
            try {

                d = df.parse(temp_toDate);
                df = new SimpleDateFormat("yyyy-M-d");
                content_todate = df.format(d);
            } catch (ParseException e) {

            }
        }

        String content_toTime = convert12to24(txtToTime.getText().toString());
        if (content_name.length() <= 0) {
            editeventname.setError(EditEventActivity.this.getResources().getString(R.string.validationname));
            editeventname.requestFocus();
        } else if (content_fromTime.length() <= 0) {
            txtFromTime.setError(EditEventActivity.this.getResources().getString(R.string.validationfromtime));
            txtFromTime.requestFocus();
        } else if (content_todate.length() <= 0) {
            txtToDate.setError(EditEventActivity.this.getResources().getString(R.string.validationtodate));
            txtToDate.requestFocus();

        } else if (content_toTime.length() <= 0) {
            txtToTime.setError(EditEventActivity.this.getResources().getString(R.string.validationtotime));
            txtToTime.requestFocus();

        } else if (content_location.length() <= 0) {
            editeventlocation.setError(EditEventActivity.this.getResources().getString(R.string.validationlocation));
            editeventlocation.requestFocus();

        } else if (content_description.length() <= 0) {
            editeventdescription.setError(EditEventActivity.this.getResources().getString(R.string.validationdecsription));
            editeventdescription.requestFocus();

        } else {
            Events ev = new Events();
//            ev.setAlarmID(RQS_1);
            ev.setEventsName(content_name);
            ev.setEventFromDate(content_fromDate);
            ev.setEventFromTime(content_fromTime);
            ev.setEventToDate(content_todate);
            ev.setEventToTime(content_toTime);
            ev.setEventLocation(content_location);
            ev.setEventDiscription(content_description);
            RQS_1 = (int) mydb.insertEvent(ev);
            setCalendar();
            mydb.close();
            Toast.makeText(EditEventActivity.this, R.string.eventAdded, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(EditEventActivity.this, MainActivity.class));

        }
    }


    public void setCalendar() {
        Calendar cal = Calendar.getInstance();


        int fromContent = txtFromdate.getText().toString().trim().length();
        if (fromContent == 10) {
            cal_year = Integer.parseInt(txtFromdate.getText().toString().trim().substring(0, 4).trim());
            cal_month = Integer.parseInt(txtFromdate.getText().toString().trim().substring(5, 7).trim());
            cal_day = Integer.parseInt(txtFromdate.getText().toString().trim().substring(8, 10).trim());
        } else if (fromContent == 9) {
            cal_year = Integer.parseInt(txtFromdate.getText().toString().trim().substring(0, 4).trim());
            cal_month = Integer.parseInt(txtFromdate.getText().toString().trim().substring(5, 6).trim());
            cal_day = Integer.parseInt(txtFromdate.getText().toString().trim().substring(7, 9).trim());
        } else if (fromContent == 8) {
            cal_year = Integer.parseInt(txtFromdate.getText().toString().trim().substring(0, 4).trim());
            cal_month = Integer.parseInt(txtFromdate.getText().toString().trim().substring(5, 6).trim());
            cal_day = Integer.parseInt(txtFromdate.getText().toString().trim().substring(7, 8).trim());
        }


        String CurrentString = convert12to24(txtFromTime.getText().toString().trim());
        String[] separated = CurrentString.split(":");
        String shr = separated[0];
        String smin = separated[1].substring(0, 2);


        int calc_hour = Integer.parseInt(shr);
        int calc_min = Integer.parseInt(smin);


        cal.set(cal_year, (cal_month - 1), cal_day, calc_hour, calc_min, 00);
        CommonData.setAlarm(EditEventActivity.this, cal, RQS_1);


    }


    @Override
    public void onBackPressed() {
        mydb.close();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();

    }

}
