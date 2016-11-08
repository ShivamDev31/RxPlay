package com.shivamdev.rxplay.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by shivam on 7/11/16.
 */

public final class DateTimeUtils {

    public static String format(String pattern, Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date date = calendar.getTime();
        return sdf.format(date);
    }

    public static String getCurrentFullDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat(Formats.dd_MM_yyyy_HH_mm_ss);
        return sdf.format(new Date(System.currentTimeMillis()));
    }

    public static String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(Formats.dd_MM_yyyy);
        return sdf.format(new Date(System.currentTimeMillis()));
    }

    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat(Formats.HH_mm);
        return sdf.format(new Date(System.currentTimeMillis()));
    }

    public static String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat(Formats.dd_MM_yyyy_HH_mm);
        return sdf.format(new Date(System.currentTimeMillis()));
    }

    public interface Formats {
        String dd_MM_yyyy = "dd.MM.yyyy";
        String dd_MM_yyyy_HH_mm = "dd.MM.yyyy HH:mm";
        String HH_mm = "HH:mm";
        String dd_MM_yyyy_HH_mm_ss = "dd-MM-yyyy HH:mm:ss";
        String HH_mm_aa = "HH_mm_aa";
    }
}