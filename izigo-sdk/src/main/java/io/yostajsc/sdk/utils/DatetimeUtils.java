package io.yostajsc.sdk.utils;

import android.content.Context;
import android.content.res.Resources;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import io.yostajsc.sdk.R;

/**
 * Created by Phuc-Hau Nguyen on 3/3/2017.
 */

public class DatetimeUtils {

    private static int pluralsTimeIds[] = {R.plurals.seconds, R.plurals.minutes, R.plurals.hours,
            R.plurals.days, R.plurals.months, R.plurals.years};

    private static int pluralsPastTimeIds[] = {R.plurals.seconds_ago, R.plurals.minutes_ago, R.plurals.hours_ago,
            R.plurals.days_ago, R.plurals.months_ago, R.plurals.years_ago};

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
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return dateFormat.format(new Date(milliseconds));
    }

    public static long getTimeStep(long millis) {
        long currTimes = System.currentTimeMillis();
        return (currTimes - millis);
    }

    public static String getTimeGap(Context context, long timeGap) {

        Resources resources = context.getResources();

        int timeConst[] = {1000, 60, 60, 24, 30, 12};
        int timeValue = (int) (timeGap / timeConst[0]);

        String res = resources.getQuantityString(R.plurals.seconds, timeValue, timeValue);

        for (int i = 1; i < pluralsTimeIds.length; ++i) {
            timeValue /= timeConst[i];
            if (timeValue != 0) {
                res = resources.getQuantityString(pluralsTimeIds[i], timeValue, timeValue);
            }
        }
        return res;
    }


}
