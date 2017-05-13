
package io.yostajsc.izigo.usecase.trip;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import io.yostajsc.core.utils.ToastUtils;
import io.yostajsc.izigo.constants.TransferType;
import io.yostajsc.core.interfaces.CallBackWith;
import io.yostajsc.core.code.MessageType;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.usecase.OwnCoreActivity;
import io.yostajsc.izigo.dialogs.DialogPickTransfer;
import io.yostajsc.izigo.AppConfig;
import io.yostajsc.sdk.model.trip.IgPlace;
import io.yostajsc.sdk.model.trip.IgTrip;
import io.yostajsc.izigo.utils.UiUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static io.yostajsc.izigo.usecase.trip.AddTripDefine.DAY;
import static io.yostajsc.izigo.usecase.trip.AddTripDefine.HOUR;
import static io.yostajsc.izigo.usecase.trip.AddTripDefine.MINUTE;
import static io.yostajsc.izigo.usecase.trip.AddTripDefine.MONTH;
import static io.yostajsc.izigo.usecase.trip.AddTripDefine.YEAR;

@RuntimePermissions
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


    private IgPlace from = new IgPlace(), to = new IgPlace();
    private int[] arriveTime = new int[5], departTime = new int[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        ButterKnife.bind(this);
        AddTripActivityHelper.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        AddTripActivityHelper.tickTimeUpdate(  // Arrive
                arriveTime,
                textArriveDate,
                textArriveTime);
        AddTripActivityHelper.tickTimeUpdate( // Depart
                departTime,
                textDepartDate,
                textDepartTime);
    }

    @OnClick({R.id.text_arrive, R.id.text_depart})
    public void pickArriveLocation(View view) {
        if (view.getId() == R.id.text_arrive)
            AddTripActivityPermissionsDispatcher.pickLocationWithCheck(this, MessageType.PICK_LOCATION_TO);
        else
            AddTripActivityPermissionsDispatcher.pickLocationWithCheck(this, MessageType.PICK_LOCATION_FROM);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AddTripActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission({Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    public void pickLocation(int type) {
        startActivityForResult(new Intent(AddTripActivity.this, PickLocationActivity.class), type);
    }

    @OnClick({R.id.text_depart_time, R.id.text_arrive_time})
    public void pickDepartTime(final View view) {
        AddTripActivityHelper.getInstance().selectTime(new AddTripActivityHelper.OnReceiveTime() {
            @Override
            public void hour(Integer hour) {
                if (view.getId() == R.id.text_depart_time) {
                    departTime[HOUR] = hour;
                } else if (view.getId() == R.id.text_arrive_time) {
                    arriveTime[HOUR] = hour;
                }
            }

            @Override
            public void minute(Integer minute) {
                if (view.getId() == R.id.text_depart_time) {
                    departTime[MINUTE] = minute;
                } else if (view.getId() == R.id.text_arrive_time) {
                    arriveTime[MINUTE] = minute;
                }
            }

            @Override
            public void text(String result) {
                if (view.getId() == R.id.text_depart_time) {
                    textDepartTime.setText(result);
                } else if (view.getId() == R.id.text_arrive_time) {
                    textArriveTime.setText(result);
                }
            }
        });
    }

    @OnClick({R.id.text_depart_date, R.id.text_arrive_date})
    public void pickDepartDate(final View view) {
        AddTripActivityHelper.getInstance().selectDate(
                new AddTripActivityHelper.OnReceiveDate() {
                    @Override
                    public void day(Integer day) {
                        if (view.getId() == R.id.text_depart_date) {
                            departTime[DAY] = day;
                        } else if (view.getId() == R.id.text_arrive_date) {
                            arriveTime[DAY] = day;
                        }
                    }

                    @Override
                    public void month(Integer month) {
                        if (view.getId() == R.id.text_depart_date) {
                            departTime[MONTH] = month;
                        } else if (view.getId() == R.id.text_arrive_date) {
                            arriveTime[MONTH] = month;
                        }
                    }

                    @Override
                    public void year(Integer year) {
                        if (view.getId() == R.id.text_depart_date) {
                            departTime[YEAR] = year;
                        } else if (view.getId() == R.id.text_arrive_date) {
                            arriveTime[YEAR] = year;
                        }
                    }

                    @Override
                    public void text(String result) {
                        if (view.getId() == R.id.text_depart_date) {
                            textDepartDate.setText(result);
                        } else if (view.getId() == R.id.text_arrive_date) {
                            textArriveDate.setText(result);
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == MessageType.PICK_LOCATION_FROM) {
            from = (IgPlace) data.getSerializableExtra(AppConfig.KEY_PICK_LOCATION);
            textDepart.setText(from.getName());
        } else if (resultCode == Activity.RESULT_OK && requestCode == MessageType.PICK_LOCATION_TO) {
            to = (IgPlace) data.getSerializableExtra(AppConfig.KEY_PICK_LOCATION);
            textArrive.setText(to.getName());
        }
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

        if (TextUtils.isEmpty(groupName) || TextUtils.isEmpty(arriveName) ||
                TextUtils.isEmpty(departName) || TextUtils.isEmpty(description)) {
            ToastUtils.showToast(this, getString(R.string.error_message_empty));
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
                        expired();
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
        intent.putExtra(IgTrip.TRIP_ID, tripId);
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