package io.yostajsc.izigo.usecase.trip;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import io.yostajsc.izigo.R;
import io.yostajsc.sdk.api.model.trip.IgPlace;


/**
 * Created by nphau on 6/2/17.
 */

public class TimePickerView extends FrameLayout {

    public static final String TAG = TimePickerView.class.getSimpleName();

    private TextView textArriveName;
    private TextView textDepartName;
    private TextView textArriveTime;
    private TextView textArriveDate;
    private TextView textDepartTime;
    private TextView textDepartDate;

    private IgPlace from = new IgPlace(), to = new IgPlace();

    public TimePickerView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public TimePickerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TimePickerView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    void init(Context context) {
        inflate(context, R.layout.layout_time_picker, this);
        textArriveName = (TextView) findViewById(R.id.text_arrive);
        textDepartName = (TextView) findViewById(R.id.text_depart);
        textArriveTime = (TextView) findViewById(R.id.text_arrive_time);
        textArriveDate = (TextView) findViewById(R.id.text_arrive_date);
        textDepartTime = (TextView) findViewById(R.id.text_depart_time);
        textDepartDate = (TextView) findViewById(R.id.text_depart_date);

        // pick date
        textDepartDate.setOnClickListener(pickDate);
        textArriveDate.setOnClickListener(pickDate);

        // pick time
        textArriveTime.setOnClickListener(pickTime);
        textDepartTime.setOnClickListener(pickTime);
    }

    public void bind() {

    }

    View.OnClickListener pickDate = new OnClickListener() {
        @Override
        public void onClick(final View view) {
            AddTripActivityHelper.getInstance().selectDate(new AddTripActivityHelper.OnReceiveDate() {
                @Override
                public void date(Integer day, Integer month, Integer year, String result) {
                    if (view.getId() == R.id.text_depart_date) {
                        from.setDay(day);
                        from.setMonth(month);
                        from.setYear(year);
                        textDepartDate.setText(result);
                    } else if (view.getId() == R.id.text_arrive_date) {
                        to.setDay(day);
                        to.setMonth(month);
                        to.setYear(year);
                        textArriveDate.setText(result);
                    }
                }
            });
        }
    };
    View.OnClickListener pickTime = new OnClickListener() {
        @Override
        public void onClick(final View view) {
            AddTripActivityHelper.getInstance().selectTime(new AddTripActivityHelper.OnReceiveTime() {
                @Override
                public void time(Integer hour, Integer minute, String result) {
                    if (view.getId() == R.id.text_depart_time) {
                        from.setHour(hour);
                        from.setMinute(minute);
                        textDepartTime.setText(result);
                    } else if (view.getId() == R.id.text_arrive_time) {
                        to.setHour(hour);
                        to.setMinute(minute);
                        textArriveTime.setText(result);
                    }
                }
            });
        }
    };
}
