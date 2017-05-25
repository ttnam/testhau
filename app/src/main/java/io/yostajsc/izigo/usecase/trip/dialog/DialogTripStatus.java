package io.yostajsc.izigo.usecase.trip.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.yostajsc.sdk.utils.DimensionUtil;
import io.yostajsc.izigo.R;
import io.yostajsc.sdk.api.model.trip.IgTripStatus;

/**
 * Created by Phuc-Hau Nguyen on 8/31/2016.
 */
public class DialogTripStatus extends Dialog {

    @BindView(R.id.radio_prepare)
    RadioButton radioPrepare;

    @BindView(R.id.radio_ongoing)
    RadioButton radioOngoing;

    @BindView(R.id.radio_finish)
    RadioButton radioFinish;

    private Activity mOwnerActivity = null;
    private OnSelectListener mListener = null;

    public DialogTripStatus(Context context, OnSelectListener listener) {
        super(context, R.style.CoreAppTheme_Dialog);

        this.mOwnerActivity = (context instanceof Activity) ? (Activity) context : null;
        if (this.mOwnerActivity != null)
            setOwnerActivity(mOwnerActivity);

        this.mListener = listener;
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
        setContentView(R.layout.view_dialog_status);
        ButterKnife.bind(this);
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.windowAnimations = R.style.CoreAppTheme_AnimDialog_Grow;
            params.width = DimensionUtil.getScreenWidth(mOwnerActivity) * 4 / 5;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(params);
        }
    }

    @OnClick(R.id.radio_ongoing)
    public void selectOngoing() {
        dismiss();
        mListener.select(IgTripStatus.ONGOING);
    }

    @OnClick(R.id.radio_finish)
    public void selectFinish() {
        dismiss();
        mListener.select(IgTripStatus.FINISHED);
    }

    @OnClick(R.id.radio_prepare)
    public void selectPrepare() {
        dismiss();
        mListener.select(IgTripStatus.PREPARED);
    }

    public void setCurrentStatus(int status) {
        switch (status) {
            case IgTripStatus.FINISHED:
                radioFinish.setChecked(true);
                radioPrepare.setChecked(false);
                radioOngoing.setChecked(false);
                break;
            case IgTripStatus.PREPARED:
                radioFinish.setChecked(false);
                radioPrepare.setChecked(true);
                radioOngoing.setChecked(false);
                break;
            case IgTripStatus.ONGOING:
                radioFinish.setChecked(false);
                radioPrepare.setChecked(false);
                radioOngoing.setChecked(true);
                break;
        }
    }

    public interface OnSelectListener {
        void select(int type);
    }
}