package com.example.jabs.customcalendar.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jabs.customcalendar.R;
import com.example.jabs.customcalendar.adapter.CalendarAdapter;
import com.example.jabs.customcalendar.utils.DBHelper;
import com.melnykov.fab.FloatingActionButton;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


/*
* Create by jaydeep bhatt on 16/11/2015*/

public class MainActivity extends AppCompatActivity {

    public GregorianCalendar month, itemmonth;

    public CalendarAdapter adapter;
    public Handler handler;
    public ArrayList<String> items;
    private DBHelper myDB;
    boolean doubleBackToExitPressedOnce = false;
    String selectedGridDate;
    ArrayList eventList;
    Point p;
    private String sameDayFTime;
    private String sameDayTTime;
    private String sameDaynm;
    private String sameDayID;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setBackgroundColor(Color.TRANSPARENT);
        Locale.setDefault(Locale.US);
        month = (GregorianCalendar) GregorianCalendar.getInstance();
        itemmonth = (GregorianCalendar) month.clone();

        items = new ArrayList<String>();
        adapter = new CalendarAdapter(this, month);

        final GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(adapter);

        handler = new Handler();
        handler.post(calendarUpdater);

        TextView title = (TextView) findViewById(R.id.title);
        title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
        RelativeLayout previous = (RelativeLayout) findViewById(R.id.previous);

        previous.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setPreviousMonth();
                refreshCalendar();
            }
        });

        RelativeLayout next = (RelativeLayout) findViewById(R.id.next);
        next.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setNextMonth();
                refreshCalendar();

            }
        });
        gridview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((CalendarAdapter) parent.getAdapter()).setSelected(view);
                selectedGridDate = CalendarAdapter.dayString
                        .get(position);
                String[] separatedTime = selectedGridDate.split("-");
                String gridvalueString = separatedTime[2].replaceFirst("^0*",
                        "");
                int gridvalue = Integer.parseInt(gridvalueString);
                if ((gridvalue > 10) && (position < 8)) {
                    setPreviousMonth();
                    refreshCalendar();
                } else if ((gridvalue < 7) && (position > 28)) {
                    setNextMonth();
                    refreshCalendar();
                }
                ((CalendarAdapter) parent.getAdapter()).setSelected(view);
                getXYValues(view);
                eventList = myDB.getPopEvent(selectedGridDate);
                if (p != null && eventList.size() > 0) {
                    showPopup(MainActivity.this, p);
                }


            }
        });
        fab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedGridDate == null) {
                    Toast.makeText(MainActivity.this, MainActivity.this.getResources().getString(R.string.selectdate), Toast.LENGTH_SHORT).show();

                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("Date", selectedGridDate);
                    Intent itoDate = new Intent(MainActivity.this, EditEventActivity.class);
                    itoDate.putExtras(bundle);
                    startActivity(itoDate);
                }
            }
        });


    }

    private void getXYValues(View v) {
        p = new Point();
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        p.x = location[0];
        p.y = location[1];
    }

    private void showPopup(Context context, Point p) {

        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.row, null);
        ImageView close = (ImageView) layout.findViewById(R.id.imgpopupclose);
        TextView txtDisplaydate = (TextView) layout.findViewById(R.id.txtDisplaydate);
        LinearLayout scrollEventDetail = (LinearLayout) layout.findViewById(R.id.scrollEventDetail);
        eventList = myDB.getPopEvent(selectedGridDate);
        LinearLayout innerlayout[] = new LinearLayout[eventList.size()];
        LinearLayout[] view = new LinearLayout[eventList.size()];
        txtDisplaydate.setText(selectedGridDate);
        final PopupWindow popup = new PopupWindow(layout, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true);

        int OFFSET_X = 30;
        int OFFSET_Y = 30;
        popup.showAtLocation(layout, Gravity.NO_GRAVITY, p.x + OFFSET_X, p.y + OFFSET_Y);
        if (eventList != null) {
            Cursor rsTime = myDB.getPopupEventDetail(selectedGridDate);
            final String[] strings = new String[eventList.size()];
            if (rsTime != null && rsTime.moveToFirst()) {
                for (int i = 0; i < rsTime.getCount(); i++) {

                    strings[i] = eventList.get(i).toString();
                    sameDayFTime = (rsTime.getString(rsTime.getColumnIndex(DBHelper.EVENTS_FROMTIME)));
                    sameDayTTime = (rsTime.getString(rsTime.getColumnIndex(DBHelper.EVENTS_TOTIME)));
                    sameDaynm = (rsTime.getString(rsTime.getColumnIndex(DBHelper.EVENTS_NAME)));
                    sameDayID = (rsTime.getString(rsTime.getColumnIndex(DBHelper.EVENTS_ID)));

                    innerlayout[i] = (LinearLayout) layoutInflater.inflate(
                            R.layout.itempopup, null);

                    TextView fromTime = (TextView) innerlayout[i]
                            .findViewById(R.id.txtFtime);
                    TextView toTime = (TextView) innerlayout[i]
                            .findViewById(R.id.txtTtime);
                    final TextView eventName = (TextView) innerlayout[i]
                            .findViewById(R.id.txtEName);
                    final TextView EventID = (TextView) innerlayout[i].findViewById(R.id.txtEID);
                    EventID.setText(sameDayID);
                    EventID.setVisibility(View.GONE);


                    fromTime.setText(convert24to12(sameDayFTime));
                    toTime.setText(convert24to12(sameDayTTime));
                    eventName.setText(sameDaynm);

                    innerlayout[i].setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            Intent itoDisplay = new Intent(MainActivity.this, DisplayEventDetailActivity.class);
                            itoDisplay.putExtra("EventName", eventName.getText().toString());
                            itoDisplay.putExtra("EventID", EventID.getText().toString());
                            startActivity(itoDisplay);
                            finish();

                        }
                    });
                    scrollEventDetail.addView(innerlayout[i]);
                    rsTime.moveToNext();
                }
                if (!rsTime.isClosed()) {
                    rsTime.close();
                }

            }
        }
        close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });
    }


    protected void setNextMonth() {
        if (month.get(GregorianCalendar.MONTH) == month
                .getActualMaximum(GregorianCalendar.MONTH)) {
            month.set((month.get(GregorianCalendar.YEAR) + 1),
                    month.getActualMinimum(GregorianCalendar.MONTH), 1);
        } else {
            month.set(GregorianCalendar.MONTH,
                    month.get(GregorianCalendar.MONTH) + 1);
        }

    }

    protected void setPreviousMonth() {
        if (month.get(GregorianCalendar.MONTH) == month
                .getActualMinimum(GregorianCalendar.MONTH)) {
            month.set((month.get(GregorianCalendar.YEAR) - 1),
                    month.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            month.set(GregorianCalendar.MONTH,
                    month.get(GregorianCalendar.MONTH) - 1);
        }

    }

    public void refreshCalendar() {
        TextView title = (TextView) findViewById(R.id.title);

        adapter.refreshDays();
        adapter.notifyDataSetChanged();
        handler.post(calendarUpdater);

        title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
    }

    public Runnable calendarUpdater = new Runnable() {

        @Override
        public void run() {
            items.clear();

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            String itemvalue;
            myDB = new DBHelper(MainActivity.this);

            String events_date;
            ArrayList event_list = myDB.getAllEvents();
            if (event_list.size() >= 0) {
                for (int i = 1; i <= event_list.size(); i++) {
                    itemvalue = df.format(itemmonth.getTime());
                    Cursor rs = myDB.getData(i);
                    if (rs != null && rs.moveToFirst()) {
                        events_date = rs.getString(rs.getColumnIndex(DBHelper.EVENTS_FROMDATE));
                        itemmonth.add(GregorianCalendar.DATE, 1);
                        items.add(events_date);
                        if (!rs.isClosed()) {
                            rs.close();
                        }
                    }

                }

                adapter.setItems(items);
                adapter.notifyDataSetChanged();

            }

        }
    };


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            myDB.close();
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, MainActivity.this.getResources().getString(R.string.doubleclickexit), Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
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

}