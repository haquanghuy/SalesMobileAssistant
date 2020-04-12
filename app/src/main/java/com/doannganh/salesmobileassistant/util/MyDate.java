package com.doannganh.salesmobileassistant.util;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class MyDate {
    public static DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    public static int WEEK_NOW = 0;
    public static int WEEK_LAST = -7;
    public static int WEEK_NEXT = 7;
    public static int MONTH_NOW = 0;

    public String[] getCurrentWeekDate(int week) {
        // 7: next week, -7: last week
        Calendar c = GregorianCalendar.getInstance();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
        c.add(Calendar.DAY_OF_WEEK, week);
        String startDate;
        String endDate;
        startDate = df.format(c.getTime());
        c.add(Calendar.DAY_OF_MONTH, 6);
        endDate = df.format(c.getTime());
        //String s = "Start Date = " + startDate + " End Date = " + endDate;
        String[] re = new String[2];
        re[0] = startDate;
        re[1] = endDate;
        return re;
    }

    public String[] getCurrentMonthDate() {
        // 7: next week, -7: last week
        Calendar c = GregorianCalendar.getInstance();

        String startDate;
        String endDate;
        c.set(Calendar.DAY_OF_MONTH, 1);
        startDate = df.format(c.getTime());
        c.set(Calendar.DATE, c.getActualMaximum(Calendar.DATE));
        endDate = df.format(c.getTime());
        //String s = "Start Date = " + startDate + " End Date = " + endDate;
        String[] re = new String[2];
        re[0] = startDate;
        re[1] = endDate;
        return re;
    }

    public String[] getCurrentMonthDate(int month, int year) {
        // 7: next week, -7: last week
        Calendar c = GregorianCalendar.getInstance();

        String startDate;
        String endDate;
        c.set(year, month-1, 1);
        startDate = df.format(c.getTime());
        c.set(Calendar.DATE, c.getActualMaximum(Calendar.DATE));
        endDate = df.format(c.getTime());
        //String s = "Start Date = " + startDate + " End Date = " + endDate;
        String[] re = new String[2];
        re[0] = startDate;
        re[1] = endDate;
        return re;
    }

    public boolean isIn (String[] re, String yourDate){
        try {
            Date startD = df.parse(re[0]);
            Date endD = df.parse(re[1]);
            Date your = df.parse(yourDate);

            if(your.getTime() >= startD.getTime() && your.getTime() <= endD.getTime()){

                return true;
            }
        } catch (ParseException e) {
            Log.d("LLLMyDateIsIn", e.getMessage());
        }
        return false;
    }
}
