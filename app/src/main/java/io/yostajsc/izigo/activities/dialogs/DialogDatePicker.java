package io.yostajsc.izigo.activities.dialogs;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Phuc-Hau Nguyen on 3/12/2017.
 */

public class DialogDatePicker extends DatePickerDialog {

    private DatePickerDialog mDatePicker;

    public DialogDatePicker(Context context, int theme, int year, int monthOfYear, int dayOfMonth, OnDateSetListener callBack) {
        super(context, theme, callBack, year, monthOfYear, dayOfMonth);
        mDatePicker = new DatePickerDialog(context, theme, callBack, year, monthOfYear, dayOfMonth);

        mDatePicker.getDatePicker().init(2013, 7, 16, this);

        updateTitle(year, monthOfYear);

    }

    public void onDateChanged(DatePicker view, int year,
                              int month, int day) {
        updateTitle(year, month);
    }

    private void updateTitle(int year, int month) {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
//       mCalendar.set(Calendar.DAY_OF_MONTH, day);
        mDatePicker.setTitle(getFormat().format(mCalendar.getTime()));
    }

    public DatePickerDialog getPicker() {

        return this.mDatePicker;
    }

    /*
     * the format for dialog tile,and you can override this method
     */
    public SimpleDateFormat getFormat() {
        return new SimpleDateFormat("MMM, yyyy");
    }
}
