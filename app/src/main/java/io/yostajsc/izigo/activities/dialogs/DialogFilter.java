package io.yostajsc.izigo.activities.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import com.yosta.materialspinner.MaterialSpinner;

import org.greenrobot.eventbus.EventBus;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.yostajsc.izigo.R;

/**
 * Created by Phuc-Hau Nguyen on 8/31/2016.
 */
public class DialogFilter extends Dialog {

    @BindView(R.id.spinner_during_time)
    MaterialSpinner spinnerDuringTime;

    @BindView(R.id.spinner_sort_by)
    MaterialSpinner spinnerSortBy;

    private Activity mOwnerActivity = null;
    private List<String> mDuringTimes = null, mSortBy = null;

    public DialogFilter(Context context) {
        super(context, R.style.AppTheme_CustomDialog);
        Window window = getWindow();
        if (window != null) {
            window.getAttributes().windowAnimations = R.style.AppTheme_AnimDialog_SlideUpDown;
        }
        this.mOwnerActivity = (context instanceof Activity) ? (Activity) context : null;
        if (this.mOwnerActivity != null)
            setOwnerActivity(mOwnerActivity);
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

        onInitializeData();
    }

    private void onInitializeData() {

        this.mDuringTimes = Arrays.asList(this.mOwnerActivity.getResources().getStringArray(R.array.arr_during_times));
        this.mSortBy = Arrays.asList(this.mOwnerActivity.getResources().getStringArray(R.array.arr_sort_by));
        this.spinnerSortBy.setItems(mSortBy);
        this.spinnerDuringTime.setItems(mDuringTimes);
    }

    @OnClick(R.id.tv_apply)
    public void onCallToApply() {
        EventBus.getDefault().post(new Filter(
                mSortBy.get(this.spinnerSortBy.getSelectedIndex()),
                mDuringTimes.get(this.spinnerDuringTime.getSelectedIndex())
        ));
        dismiss();
    }

    @OnClick(R.id.tv_cancel)
    public void onCallToCancel() {
        dismiss();
    }

    public class Filter {

        public String mSortBy;
        public String mDuringTime;

        public Filter(String sortBy, String duringTime) {
            this.mSortBy = sortBy;
            this.mDuringTime = duringTime;
        }
    }
}