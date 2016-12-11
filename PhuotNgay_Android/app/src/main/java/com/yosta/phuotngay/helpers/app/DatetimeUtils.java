package com.yosta.phuotngay.helpers.app;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Phuc-Hau Nguyen on 12/2/2016.
 */

public class DatetimeUtils {

    public static final int DD_MM_YYYY = 0;
    public static final int H_MM = 1;


    public static String getTime(long millis, int type) {
        Timestamp stamp = new Timestamp(millis);
        Date date = new Date(stamp.getTime());
        SimpleDateFormat sdf = null;
        if (type == DD_MM_YYYY)
            sdf = new SimpleDateFormat("dd/MM/yyyy");
        if (type == H_MM)
            sdf = new SimpleDateFormat("h:mm a");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        return sdf.format(date);
    }
}
