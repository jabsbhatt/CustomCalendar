package com.example.jabs.customcalendar.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jabs.customcalendar.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class CalendarAdapter extends BaseAdapter {
    private Context mContext;

    private java.util.Calendar month;
    public GregorianCalendar pmonth;
    public GregorianCalendar pmonthmaxset;
    private GregorianCalendar selectedDate;
    int firstDay;
    int maxWeeknumber;
    int maxP;
    int calMaxP;
    int lastWeekDay;
    int leftDays;
    int mnthlength;
    String itemvalue, curentDateString;
    DateFormat df;
    final static String TAG = "CalendarAdapter";

    private ArrayList<String> items;
    public static List<String> dayString;
    private View previousView;

    public CalendarAdapter(Context c, GregorianCalendar monthCalendar) {
        CalendarAdapter.dayString = new ArrayList<String>();
        Locale.setDefault(Locale.US);
        month = monthCalendar;
        selectedDate = (GregorianCalendar) monthCalendar.clone();
        mContext = c;
        month.set(GregorianCalendar.DAY_OF_MONTH, 1);
        this.items = new ArrayList<String>();
        int curentMonth = (GregorianCalendar.MONTH - 1);
        if (curentMonth < 11) {
            df = new SimpleDateFormat("yyyy-M-d", Locale.US);
            curentDateString = df.format(selectedDate.getTime());
        } else {
            df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            curentDateString = df.format(selectedDate.getTime());
        }

        refreshDays();
    }

    public void setItems(ArrayList<String> items) {
        for (int i = 0; i != items.size(); i++) {
            if (items.get(i).length() == 1) {
                items.set(i, "0" + items.get(i));
            }
        }
        this.items = items;
    }

    public int getCount() {
        return dayString.size();
    }

    public Object getItem(int position) {
        return dayString.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        TextView dayView;
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.calendar_item, null);

        }
        dayView = (TextView) v.findViewById(R.id.date);
        String[] separatedTime = dayString.get(position).split("-");
        String gridvalue = separatedTime[2].replaceFirst("^0*", "");
        if ((Integer.parseInt(gridvalue) > 1) && (position < firstDay)) {
            dayView.setTextColor(Color.WHITE);

            dayView.setClickable(false);
            dayView.setFocusable(false);
        } else if ((Integer.parseInt(gridvalue) < 7) && (position > 28)) {
            dayView.setTextColor(Color.WHITE);
            dayView.setClickable(false);
        } else {
            dayView.setTextColor(Color.BLUE);
        }
        if (dayString.get(position).equals(curentDateString)) {
            setSelected(v);
            previousView = v;
        } else {
            v.setBackgroundResource(R.drawable.list_item_background);
        }
        dayView.setText(gridvalue);
        String date = dayString.get(position);
        if (date.length() == 1) {
            date = "0" + date;
        }
        String monthStr = "" + (month.get(GregorianCalendar.MONTH) + 1);
        if (monthStr.length() == 1) {
            monthStr = "0" + monthStr;
        }
        ImageView iw = (ImageView) v.findViewById(R.id.date_icon);
        if (date.length() > 0 && items != null && items.contains(date)) {
            iw.setVisibility(View.VISIBLE);
        } else {
            iw.setVisibility(View.INVISIBLE);
        }
        return v;
    }

    public View setSelected(View view) {
        if (previousView != null) {
            previousView.setBackgroundResource(R.drawable.list_item_background);
        }
        previousView = view;
        view.setBackgroundResource(R.drawable.calendar_cel_selectl);
        return view;
    }

    public void refreshDays() {
        items.clear();
        dayString.clear();
        Locale.setDefault(Locale.US);
        pmonth = (GregorianCalendar) month.clone();
        firstDay = month.get(GregorianCalendar.DAY_OF_WEEK);
        maxWeeknumber = month.getActualMaximum(GregorianCalendar.WEEK_OF_MONTH);
        mnthlength = maxWeeknumber * 7;
        maxP = getMaxP();
        calMaxP = maxP - (firstDay - 1);
        pmonthmaxset = (GregorianCalendar) pmonth.clone();

        pmonthmaxset.set(GregorianCalendar.DAY_OF_MONTH, calMaxP + 1);
        for (int n = 0; n < mnthlength; n++) {

            itemvalue = df.format(pmonthmaxset.getTime());
            pmonthmaxset.add(GregorianCalendar.DATE, 1);
            dayString.add(itemvalue);

        }
    }

    private int getMaxP() {
        int maxP;
        if (month.get(GregorianCalendar.MONTH) == month
                .getActualMinimum(GregorianCalendar.MONTH)) {
            pmonth.set((month.get(GregorianCalendar.YEAR) - 1),
                    month.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            pmonth.set(GregorianCalendar.MONTH,
                    month.get(GregorianCalendar.MONTH) - 1);
        }
        maxP = pmonth.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);

        return maxP;
    }

}