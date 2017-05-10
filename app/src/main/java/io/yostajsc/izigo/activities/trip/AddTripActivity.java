
package io.yostajsc.izigo.activities.trip;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.Calendar;

import io.yostajsc.constants.TransferType;
import io.yostajsc.core.interfaces.CallBackWith;
import io.yostajsc.core.code.MessageType;
import io.yostajsc.core.utils.DatetimeUtils;
import io.yostajsc.core.utils.ValidateUtils;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.activities.PickLocationActivity;
import io.yostajsc.izigo.activities.OwnCoreActivity;
import io.yostajsc.izigo.dialogs.DialogDatePicker;
import io.yostajsc.izigo.dialogs.DialogPickTransfer;
import io.yostajsc.izigo.dialogs.DialogTimePicker;
import io.yostajsc.AppConfig;
import io.yostajsc.sdk.model.trip.LocationPick;
import io.yostajsc.utils.UiUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddTripActivity extends OwnCoreActivity {


    @BindView(R.id.text_view_trip_name)
    TextInputEditText textGroupName;

    @BindView(R.id.edit_description)
    TextInputEditText editDescription;

    @BindView(R.id.text_arrive)
    TextView textArrive;

    @BindView(R.id.text_depart)
    TextView textDepart;

    @BindView(R.id.text_arrive_time)
    TextView textArriveTime;

    @BindView(R.id.text_arrive_date)
    TextView textArriveDate;

    @BindView(R.id.text_depart_time)
    TextView textDepartTime;

    @BindView(R.id.text_depart_date)
    TextView textDepartDate;

    @BindView(R.id.image_view)
    AppCompatImageView imageView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private DecimalFormat timeFormatter = new DecimalFormat("00");
    private final int YEAR = 0, MONTH = 1, DAY = 2, HOUR = 3, MINUTE = 4;
    private int[] arriveTime = new int[5], departTime = new int[5];

    private LocationPick from = new LocationPick();
    private LocationPick to = new LocationPick();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onApplyData();
    }

    @OnClick(R.id.text_depart)
    public void pickDepartLocation() {
        startActivityForResult(new Intent(AddTripActivity.this, PickLocationActivity.class), MessageType.PICK_LOCATION_FROM);
    }

    @OnClick(R.id.text_arrive)
    public void pickArriveLocation() {
        startActivityForResult(new Intent(AddTripActivity.this, PickLocationActivity.class), MessageType.PICK_LOCATION_TO);
    }

    @OnClick(R.id.text_arrive_date)
    public void pickArriveDate() {
        Calendar calendar = Calendar.getInstance();
        new DialogDatePicker(this, DatePickerDialog.THEME_HOLO_LIGHT,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        arriveTime[YEAR] = year;
                        arriveTime[MONTH] = month;
                        arriveTime[DAY] = dayOfMonth;

                        textArriveDate.setText(String.format("%s/%s/%s",
                                timeFormatter.format(dayOfMonth),
                                timeFormatter.format(month),
                                timeFormatter.format(year)));
                    }
                }).show();
    }

    @OnClick(R.id.text_arrive_time)
    public void pickArriveTime() {
        Calendar calendar = Calendar.getInstance();
        new DialogTimePicker(this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        arriveTime[HOUR] = hour;
                        arriveTime[MINUTE] = minute;
                        textArriveTime.setText(String.format("%s:%s",
                                timeFormatter.format(hour), timeFormatter.format(minute)));
                    }
                }).show();
    }

    @OnClick(R.id.text_depart_time)
    public void pickDepartTime() {
        new DialogTimePicker(this, 24, 60, true, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                departTime[HOUR] = hour;
                departTime[MINUTE] = minute;
                textDepartTime.setText(String.format("%s:%s",
                        timeFormatter.format(hour), timeFormatter.format(minute)));
            }
        }).show();
    }

    @OnClick(R.id.text_depart_date)
    public void pickDepartDate() {
        Calendar calendar = Calendar.getInstance();
        new DialogDatePicker(this, DatePickerDialog.THEME_HOLO_LIGHT,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        departTime[YEAR] = year;
                        departTime[MONTH] = month;
                        departTime[DAY] = dayOfMonth;

                        textDepartDate.setText(String.format("%s/%s/%s",
                                timeFormatter.format(dayOfMonth),
                                timeFormatter.format(month),
                                timeFormatter.format(year)));
                    }
                }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == MessageType.PICK_LOCATION_FROM) {
            from = (LocationPick) data.getSerializableExtra(AppConfig.KEY_PICK_LOCATION);
            textDepart.setText(from.getName());
        } else if (resultCode == Activity.RESULT_OK && requestCode == MessageType.PICK_LOCATION_TO) {
            to = (LocationPick) data.getSerializableExtra(AppConfig.KEY_PICK_LOCATION);
            textArrive.setText(to.getName());
        }
    }

    private void updateTime(int arr[], TextView txtDate, TextView txtTime) {
        Calendar calendar = Calendar.getInstance();
        arr[YEAR] = DatetimeUtils.getYear();
        arr[MONTH] = DatetimeUtils.getMonth();
        arr[DAY] = calendar.get(Calendar.DAY_OF_MONTH);
        arr[HOUR] = calendar.get(Calendar.HOUR);
        arr[MINUTE] = calendar.get(Calendar.MINUTE);
        txtDate.setText(String.format("%s/%s/%s",
                timeFormatter.format(arr[DAY]),
                timeFormatter.format(arr[MONTH]),
                timeFormatter.format(arr[YEAR])));
        txtTime.setText(String.format("%s:%s",
                timeFormatter.format(arr[HOUR]),
                timeFormatter.format(arr[MINUTE])));
    }

    @Override
    public void onApplyData() {
        updateTime(arriveTime, textArriveDate, textArriveTime); // Arrive
        updateTime(departTime, textDepartDate, textDepartTime); // Depart
    }

    @OnClick(R.id.image_view)
    public void pickTransfer() {
        DialogPickTransfer dialogPickTransfer = new DialogPickTransfer(this);
        dialogPickTransfer.setDialogResult(new CallBackWith<Integer>() {
            @Override
            public void run(@TransferType Integer type) {
                UiUtils.showTransfer(type, imageView);
            }
        });
        dialogPickTransfer.show();
    }

    @OnClick(R.id.button)
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @OnClick(R.id.button_confirm)
    public void onConfirm() {

        // Group name
        String groupName = textGroupName.getText().toString();
        String arriveName = textArrive.getText().toString();
        String departName = textDepart.getText().toString();
        String description = editDescription.getText().toString();

        if (!ValidateUtils.canUse(groupName, arriveName, departName, description)) {
            Toast.makeText(this, getString(R.string.error_message_empty), Toast.LENGTH_SHORT).show();
            return;
        }

        from.setTime(departTime[YEAR], departTime[MONTH], departTime[DAY],
                departTime[HOUR], departTime[MINUTE]);

        to.setTime(arriveTime[YEAR], arriveTime[MONTH], arriveTime[DAY],
                arriveTime[HOUR], arriveTime[MINUTE]);

        if (from.getTime() <= System.currentTimeMillis()) {
            Toast.makeText(this, "Thời gian đi không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (to.getTime() <= from.getTime()) {
            Toast.makeText(this, "Thời gian đến không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
/*
        IzigoApiManager.connect().addTrip(groupName, to.toString(), from.toString(), description,
                0,
                0, 1, new CallBack() {
                    @Override
                    public void run() {
                        hideProgress();
                        onExpired();
                    }
                }, new CallBackWith<String>() {
                    @Override
                    public void run(String groupId) {
                        hideProgress();
                        onSuccess(groupId);
                    }
                }, new CallBackWith<String>() {
                    @Override
                    public void run(String error) {
                        hideProgress();
                        Toast.makeText(AddTripActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                });*/
    }

    private void onSuccess(String tripId) {
        Intent intent = new Intent(AddTripActivity.this, TripDetailActivity.class);
        intent.putExtra(AppConfig.TRIP_ID, tripId);
        startActivity(intent);
        finish();
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onInternetDisConnected() {
        hideProgress();
    }

    @Override
    public void onInternetConnected() {

    }

}