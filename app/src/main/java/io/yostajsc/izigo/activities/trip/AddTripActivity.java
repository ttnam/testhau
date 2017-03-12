
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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.Calendar;

import io.yostajsc.constants.MessageType;
import io.yostajsc.constants.TransferType;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.activities.PickLocationActivity;
import io.yostajsc.izigo.activities.dialogs.DialogDatePicker;
import io.yostajsc.izigo.activities.dialogs.DialogPickTransfer;
import io.yostajsc.backend.core.APIManager;
import io.yostajsc.interfaces.ActivityBehavior;
import io.yostajsc.izigo.activities.dialogs.DialogTimePicker;
import io.yostajsc.izigo.configs.AppDefine;
import io.yostajsc.interfaces.CallBack;
import io.yostajsc.interfaces.CallBackWith;
import io.yostajsc.izigo.managers.EventManager;
import io.yostajsc.izigo.models.trip.LocationPick;
import io.yostajsc.utils.validate.ValidateUtils;
import io.yostajsc.utils.StorageUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddTripActivity extends ActivityBehavior {

    /*@BindView(R.id.recycler_view)
    RecyclerView rvFriends;*/

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

    @BindView(R.id.switch_publish)
    Switch switchPublish;

    @BindView(R.id.image_view)
    AppCompatImageView imageView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private DecimalFormat timeFormatter = new DecimalFormat("00");
    private final int YEAR = 0, MONTH = 1, DAY = 2, HOUR = 3, MINUTE = 4;
    private int[] arriveTime = new int[5], departTime = new int[5];
    /*private FriendAdapter adapter = null;*/
    /*private List<String> invites = null;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        ButterKnife.bind(this);
        onApplyViews();
        onApplyData();
    }

    @Override
    public void onApplyViews() {
        switchPublish.setOnCheckedChangeListener(EventManager.connect().addCheckedChangeListener(new CallBackWith<Boolean>() {
            @Override
            public void run(Boolean aBoolean) {
                if (aBoolean)
                    Toast.makeText(AddTripActivity.this, "Bạn đã bật chế độ công khai", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(AddTripActivity.this, "Bạn đã tắt chế độ công khai", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    private void updateTime(int arr[], TextView txtDate, TextView txtTime) {
        Calendar calendar = Calendar.getInstance();
        arr[YEAR] = calendar.get(Calendar.YEAR);
        arr[MONTH] = calendar.get(Calendar.MONTH);
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
    protected void onResume() {
        super.onResume();

        updateTime(arriveTime, textArriveDate, textArriveTime); // Arrive
        updateTime(departTime, textDepartDate, textDepartTime); // Depart

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

    private LocationPick from = new LocationPick();
    private LocationPick to = new LocationPick();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == MessageType.PICK_LOCATION_FROM) {
            from = (LocationPick) data.getSerializableExtra(AppDefine.KEY_PICK_LOCATION);
            textDepart.setText(from.getName());
        } else if (resultCode == Activity.RESULT_OK && requestCode == MessageType.PICK_LOCATION_TO) {
            to = (LocationPick) data.getSerializableExtra(AppDefine.KEY_PICK_LOCATION);
            textArrive.setText(to.getName());
        }
    }

    @Override
    public void onApplyData() {

        // invites = new ArrayList<>();
/*
        AccessToken token = AccessToken.getCurrentAccessToken();
        if (token != null) {
            String authorization = StorageUtils.inject(AddTripActivity.this).getString(AppDefine.AUTHORIZATION);
            String fbToken = token.getToken();
            APIManager.connect().getFriendsList(authorization, fbToken, new CallBackWith<List<Friend>>() {
                @Override
                public void run(List<Friend> friends) {
                    adapter.replaceAll(friends);
                }
            }, new CallBackWith<String>() {
                @Override
                public void run(String error) {
                    Toast.makeText(AddTripActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            }, new CallBack() {
                @Override
                public void run() {
                    onExpired();
                }
            });
        }*/
    }

    private void onApplyRecyclerViewFriends() {

      /*  this.adapter = new FriendAdapter(this, new ItemClick<Integer, Integer>() {
            @Override
            public void onClick(Integer type, Integer position) {
                if (type == MessageType.ITEM_CLICK_INVITE) {
                    invites.remove(adapter.getItem(position).getFbId());
                }
                if (type == MessageType.ITEM_CLICK_INVITED) {
                    invites.add(adapter.getItem(position).getFbId());
                }
            }
        });*/
       /* this.rvFriends.setAdapter(adapter);
        this.rvFriends.setHasFixedSize(true);
        this.rvFriends.setItemAnimator(new SlideInUpAnimator());
        this.rvFriends.setRecycledViewPool(new RecyclerView.RecycledViewPool());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        this.rvFriends.setNestedScrollingEnabled(false);
        this.rvFriends.setLayoutManager(layoutManager);*/
    }

    @OnClick(R.id.image_view)
    public void pickTransfer() {
        DialogPickTransfer dialogPickTransfer = new DialogPickTransfer(this);
        dialogPickTransfer.setDialogResult(new CallBackWith<Integer>() {
            @Override
            public void run(@TransferType Integer integer) {

                switch (integer) {
                    case TransferType.BUS:
                        imageView.setImageResource(R.drawable.ic_vector_bus);
                        break;
                    case TransferType.MOTORBIKE:
                        imageView.setImageResource(R.drawable.ic_vector_motor_bike);
                        break;
                    case TransferType.WALK:
                        imageView.setImageResource(R.drawable.ic_vector_walk);
                        break;
                }
            }
        });
        dialogPickTransfer.show();
    }

  /*  @OnClick(R.id.button_add_friend)
    public void onAddFriends() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image*//*");
        // intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        // intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        startActivity(Intent.createChooser(intent, getString(R.string.str_send_invite)));
    }*/

   /* private String makeMembers() {
        String members = "";
        if (invites.size() < 1)
            return members;
        for (int i = 0; i < invites.size(); i++) {
            members += invites.get(i) + ",";
        }
        return members.substring(0, members.length() - 1);
    }*/


    @OnClick(R.id.button)
    @Override
    public void onBackPressed() {
/*
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Do you want to discard?").setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("Discard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();*/
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
        String authorization = StorageUtils.inject(this).getString(AppDefine.AUTHORIZATION);

        progressBar.setVisibility(View.VISIBLE);

        APIManager.connect().createTrips(authorization, groupName,
                to.toString(), from.toString(), description,
                switchPublish.isChecked() ? 1 : 1,
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
                });
    }

    private void onSuccess(String groupId) {
        StorageUtils.inject(AddTripActivity.this).save(AppDefine.GROUP_ID, groupId);
        startActivity(new Intent(AddTripActivity.this, GroupDetailActivity.class));
        finish();
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onInternetDisconnected() {
        hideProgress();
    }

    @Override
    protected void onInternetConnected() {

    }

}