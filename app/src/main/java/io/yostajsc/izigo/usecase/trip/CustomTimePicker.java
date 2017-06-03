package io.yostajsc.izigo.usecase.trip;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DecimalFormat;
import java.util.Calendar;

import butterknife.BindView;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.usecase.trip.dialog.DialogDatePicker;
import io.yostajsc.izigo.usecase.trip.dialog.DialogTimePicker;
import io.yostajsc.sdk.api.model.trip.IgPlace;

/**
 * Created by nphau on 6/3/17.
 */

public class CustomTimePicker extends FrameLayout {

    public static final String TAG = CustomTimePicker.class.getSimpleName();

    @BindView(R.id.text_date)
    TextView textDate;

    @BindView(R.id.text_time)
    TextView textTime;

    private IgPlace mPlace = null;
    private DecimalFormat mTimeFormatter = null;

    public CustomTimePicker(@NonNull Context context) {
        super(context);
        init(context);
    }

    public CustomTimePicker(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomTimePicker(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    void init(Context context) {
        inflate(context, R.layout.item_time_picker, this);
        textDate = (TextView) findViewById(R.id.text_date);
        textTime = (TextView) findViewById(R.id.text_time);

        initData();

        textDate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate(new OnReceiveDate() {
                    @Override
                    public void date(Integer day, Integer month, Integer year, String result) {
                        mPlace.setDay(day);
                        mPlace.setMonth(month);
                        mPlace.setYear(year);
                        textDate.setText(result);
                    }
                });
            }
        });
        textTime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTime(new OnReceiveTime() {
                    @Override
                    public void time(Integer hour, Integer minute, String result) {
                        mPlace.setHour(hour);
                        mPlace.setMinute(minute);
                        textTime.setText(result);
                    }
                });
            }
        });
    }

    void initData() {
        mPlace = new IgPlace();
        mTimeFormatter = new DecimalFormat("00");
    }

    void selectDate(final OnReceiveDate onReceiveDate) {
        Calendar calendar = Calendar.getInstance();
        new DialogDatePicker(getContext(), DatePickerDialog.THEME_HOLO_LIGHT,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        onReceiveDate.date(dayOfMonth, month, year,
                                String.format("%s/%s/%s",
                                        mTimeFormatter.format(dayOfMonth),
                                        mTimeFormatter.format(month + 1),
                                        mTimeFormatter.format(year)));
                    }
                }).show();
    }

    void selectTime(final OnReceiveTime onReceiveTime) {
        Calendar calendar = Calendar.getInstance();
        new DialogTimePicker(getContext(),
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

    interface OnReceiveTime {
        void time(Integer hour, Integer minute, String result);
    }

    interface OnReceiveDate {
        void date(Integer day, Integer month, Integer year, String result);
    }

    public IgPlace getPlace() {
        return mPlace;
    }


}
