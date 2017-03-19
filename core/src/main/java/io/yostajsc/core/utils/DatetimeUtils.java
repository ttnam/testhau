package io.yostajsc.core.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Phuc-Hau Nguyen on 3/3/2017.
 */

public class DatetimeUtils {
    public static int getMonth() {
        return 1 + Calendar.getInstance(TimeZone.getDefault()).get(Calendar.MONTH);
    }

    public static int getYear() {
        return Integer.parseInt(String.valueOf(Calendar.getInstance(TimeZone.getDefault()).get(Calendar.YEAR)).substring(2));
    }
    public static String getTime(long milliseconds) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return dateFormat.format(new Date(milliseconds));
    }

    public static String getDate(long milliseconds) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd", Locale.getDefault());
        return dateFormat.format(new Date(milliseconds));
    }

    public static long getDateAsHeaderId(long milliseconds) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy", Locale.getDefault());
        return Long.parseLong(dateFormat.format(new Date(milliseconds)));
    }
}
