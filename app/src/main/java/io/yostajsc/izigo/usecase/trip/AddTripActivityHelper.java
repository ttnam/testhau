package io.yostajsc.izigo.usecase.trip;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DecimalFormat;
import java.util.Calendar;

import io.yostajsc.core.utils.DatetimeUtils;
import io.yostajsc.izigo.dialogs.DialogDatePicker;
import io.yostajsc.izigo.dialogs.DialogTimePicker;

import static io.yostajsc.izigo.usecase.trip.AddTripDefine.DAY;
import static io.yostajsc.izigo.usecase.trip.AddTripDefine.HOUR;
import static io.yostajsc.izigo.usecase.trip.AddTripDefine.MINUTE;
import static io.yostajsc.izigo.usecase.trip.AddTripDefine.MONTH;
import static io.yostajsc.izigo.usecase.trip.AddTripDefine.YEAR;

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

    public static void tickTimeUpdate(int arr[], TextView txtDate, TextView txtTime) {
        try {
            if (mInstance == null)
                throw new Exception(ERROR_UNBOUND);

            Calendar calendar = Calendar.getInstance();
            arr[YEAR] = DatetimeUtils.getYear();
            arr[MONTH] = DatetimeUtils.getMonth();
            arr[DAY] = calendar.get(Calendar.DAY_OF_MONTH);
            arr[HOUR] = calendar.get(Calendar.HOUR);
            arr[MINUTE] = calendar.get(Calendar.MINUTE);
            txtDate.setText(String.format("%s/%s/%s",
                    mTimeFormatter.format(arr[DAY]),
                    mTimeFormatter.format(arr[MONTH]),
                    mTimeFormatter.format(arr[YEAR])));
            txtTime.setText(String.format("%s:%s",
                    mTimeFormatter.format(arr[HOUR]),
                    mTimeFormatter.format(arr[MINUTE])));

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
                onReceiveTime.hour(hour);
                onReceiveTime.minute(minute);
                onReceiveTime.text(
                        String.format("%s:%s",
                                mTimeFormatter.format(hour),
                                mTimeFormatter.format(minute))
                );
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
                        onReceiveDate.day(dayOfMonth);
                        onReceiveDate.month(month);
                        onReceiveDate.year(year);
                        onReceiveDate.text(String.format("%s/%s/%s",
                                mTimeFormatter.format(dayOfMonth),
                                mTimeFormatter.format(month),
                                mTimeFormatter.format(year)));
                    }
                }).show();
    }

    public interface OnReceiveTime {
        void hour(Integer hour);

        void minute(Integer minute);

        void text(String result);
    }

    public interface OnReceiveDate {
        void day(Integer day);

        void month(Integer month);

        void year(Integer year);

        void text(String result);
    }
}
