package io.yostajsc.utils;

import java.util.Calendar;
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
}
