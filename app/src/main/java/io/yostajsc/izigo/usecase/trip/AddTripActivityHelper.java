package io.yostajsc.izigo.usecase.trip;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.text.DecimalFormat;
import java.util.Calendar;

import io.yostajsc.sdk.utils.DatetimeUtils;
import io.yostajsc.izigo.usecase.trip.dialog.DialogDatePicker;
import io.yostajsc.izigo.usecase.trip.dialog.DialogTimePicker;

/**
 * Created by nphau on 5/12/17.
 */

public class AddTripActivityHelper {

    private static final String ERROR_UNBOUND = "You must bind first!";

    private static AddTripActivity mActivity = null;
    private static AddTripActivityHelper mInstance = null;

    private static DecimalFormat mTimeFormatter = null;

    private AddTripActivityHelper(AddTripActivity activity) {
        mActivity = activity;
        mTimeFormatter = new DecimalFormat("00");
    }

    public static synchronized AddTripActivityHelper bind(AddTripActivity activity) {
        mInstance = new AddTripActivityHelper(activity);
        return mInstance;
    }


    public static AddTripActivityHelper getInstance() {
        try {
            if (mInstance == null)
                throw new Exception(ERROR_UNBOUND);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mInstance;
    }

    public static void tickTimeUpdate(final OnReceiveDate onReceiveDate,
                                      final OnReceiveTime onReceiveTime) {
        try {
            if (mInstance == null)
                throw new Exception(ERROR_UNBOUND);

            int yyyy, mm, dd, h, m;

            Calendar calendar = Calendar.getInstance();

            m = calendar.get(Calendar.MINUTE);
            h = calendar.get(Calendar.HOUR);
            dd = calendar.get(Calendar.DAY_OF_MONTH);
            mm = DatetimeUtils.getMonth();
            yyyy = DatetimeUtils.getYear();

            onReceiveDate.date(dd, mm, yyyy, String.format("%s/%s/%s",
                    mTimeFormatter.format(dd),
                    mTimeFormatter.format(mm),
                    mTimeFormatter.format(yyyy)));

            onReceiveTime.time(h, m, String.format("%s:%s",
                    mTimeFormatter.format(h),
                    mTimeFormatter.format(m)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void selectTime(final OnReceiveTime onReceiveTime) {
        Calendar calendar = Calendar.getInstance();
        new DialogTimePicker(mActivity,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                onReceiveTime.time(hour, minute, String.format("%s:%s",
                        mTimeFormatter.format(hour),
                        mTimeFormatter.format(minute)));
            }
        }).show();
    }

    public void selectDate(final OnReceiveDate onReceiveDate) {
        Calendar calendar = Calendar.getInstance();
        new DialogDatePicker(mActivity, DatePickerDialog.THEME_HOLO_LIGHT,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        onReceiveDate.date(dayOfMonth, month + 1, year,
                                String.format("%s/%s/%s",
                                        mTimeFormatter.format(dayOfMonth),
                                        mTimeFormatter.format(month + 1),
                                        mTimeFormatter.format(year)));
                    }
                }).show();
    }

    public interface OnReceiveTime {
        void time(Integer hour, Integer minute, String result);
    }

    public interface OnReceiveDate {
        void date(Integer day, Integer month, Integer year, String result);
    }
}
