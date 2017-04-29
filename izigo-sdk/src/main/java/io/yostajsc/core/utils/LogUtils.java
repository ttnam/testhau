package io.yostajsc.core.utils;

import android.util.Log;

import java.util.Calendar;

/**
 * Created by nphau on 4/29/17.
 */

public class LogUtils {
    public final static String TAG = LogUtils.class.getSimpleName();

    public static void log(String tag, String msg) {
        Log.e(tag, String.format("%s_%s", Calendar.getInstance().getTimeInMillis(), msg));
    }

    public static void log(String tag, int errorCode) {
        Log.e(tag, String.format("%s_ErrorCode:%d", Calendar.getInstance().getTimeInMillis(), errorCode));
    }

    public static void log(String msg) {
        log(TAG, msg);
    }

}
