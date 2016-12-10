package com.yosta.phuotngay.helpers.app;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

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
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(date);
    }
}
