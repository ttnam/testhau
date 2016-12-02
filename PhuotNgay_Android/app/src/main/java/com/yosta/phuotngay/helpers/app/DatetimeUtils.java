package com.yosta.phuotngay.helpers.app;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Phuc-Hau Nguyen on 12/2/2016.
 */

public class DatetimeUtils {

    public static Date timeStampToDate(long milis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milis);
        return calendar.getTime();
    }

    public static String getTime(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd yyyy");// java.util.Locale.getDefault());
        return dateFormat.format(date);
    }
}
