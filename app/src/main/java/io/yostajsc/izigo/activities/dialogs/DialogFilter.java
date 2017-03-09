package io.yostajsc.izigo.activities.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.view.Window;

import com.yosta.materialspinner.MaterialSpinner;

import org.greenrobot.eventbus.EventBus;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.yostajsc.constants.TransferType;
import io.yostajsc.izigo.R;

/**
 * Created by Phuc-Hau Nguyen on 8/31/2016.
 */
public class DialogFilter extends Dialog {

    @BindView(R.id.spinner_during_time)
    MaterialSpinner mSpinnerDuringTime;

    @BindView(R.id.spinner_sort_by)
    MaterialSpinner mSpinnerSortBy;

    @BindView(R.id.button_people)
    AppCompatImageView buttonPeople;

    @BindView(R.id.button_bicycle)
    AppCompatImageView buttonBicycle;

    @BindView(R.id.button_bus)
    AppCompatImageView buttonBus;


    @TransferType
    private int mTransfer = TransferType.WALK;

    private Activity mOwnerActivity = null;
    private List<String> mDuringTimes = null, mSortBy = null, mVehicles = null;

    public DialogFilter(Context context) {
        super(context, R.style.AppTheme_CustomDialog);
        this.mOwnerActivity = (context instanceof Activity) ? (Activity) context : null;
        if (this.mOwnerActivity != null)
            setOwnerActivity(mOwnerActivity);
        Window window = getWindow();
        if (window != null) {
            window.getAttributes().windowAnimations = R.style.AppTheme_AnimDialog_SlideUpDown;
        }
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mOwnerActivity = getOwnerActivity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.view_dialog_filter);
        ButterKnife.bind(this);

        onApplyData();
    }

    private void onApplyData() {

        // Set default
        onPeopleClick();

        this.mDuringTimes = Arrays.asList(this.mOwnerActivity.getResources().getStringArray(R.array.arr_during_times));
        this.mSortBy = Arrays.asList(this.mOwnerActivity.getResources().getStringArray(R.array.arr_sort_by));
        this.mVehicles = Arrays.asList(this.mOwnerActivity.getResources().getStringArray(R.array.arr_vehicle));
        this.mSpinnerSortBy.setItems(mSortBy);
        this.mSpinnerDuringTime.setItems(mDuringTimes);
    }

    @OnClick(R.id.tv_apply)
    public void onCallToApply() {
        EventBus.getDefault().post(new Filter(
                mSortBy.get(this.mSpinnerSortBy.getSelectedIndex()),
                mDuringTimes.get(this.mSpinnerDuringTime.getSelectedIndex())
        ));
        dismiss();
    }

    @OnClick(R.id.tv_cancel)
    public void onCallToCancel() {
        dismiss();
    }

    private class Filter {

        public String mSortBy;
        public String mDuringTime;

        public Filter(String sortBy, String duringTime) {
            this.mSortBy = sortBy;
            this.mDuringTime = duringTime;
        }
    }

    @OnClick(R.id.button_people)
    public void onPeopleClick() {
        mTransfer = TransferType.WALK;
        updateTransferState();
    }

    @OnClick(R.id.button_bus)
    public void onBusClick() {
        mTransfer = TransferType.BUS;
        updateTransferState();
    }

    @OnClick(R.id.button_bicycle)
    public void onBicycleClick() {
        mTransfer = TransferType.BICYCLE;
        updateTransferState();
    }

    private void updateTransferState() {
        switch (mTransfer) {
            case TransferType.BICYCLE:
                buttonBicycle.setImageResource(R.drawable.ic_vector_motobike);
                buttonPeople.setImageResource(R.drawable.ic_vector_people_dark);
                buttonBus.setImageResource(R.drawable.ic_vector_bus_dark);
                break;
            case TransferType.BUS:
                buttonBicycle.setImageResource(R.drawable.ic_vector_motobike_dark);
                buttonPeople.setImageResource(R.drawable.ic_vector_people_dark);
                buttonBus.setImageResource(R.drawable.ic_vector_bus);
                break;
            case TransferType.WALK:
                buttonBicycle.setImageResource(R.drawable.ic_vector_motobike_dark);
                buttonPeople.setImageResource(R.drawable.ic_vector_people);
                buttonBus.setImageResource(R.drawable.ic_vector_bus_dark);
                break;
        }
    }
}