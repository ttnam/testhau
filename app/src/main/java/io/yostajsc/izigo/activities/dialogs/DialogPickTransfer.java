package io.yostajsc.izigo.activities.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.view.Window;
import android.view.WindowManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.yostajsc.constants.TransferType;
import io.yostajsc.interfaces.CallBackWith;
import io.yostajsc.izigo.R;
import io.yostajsc.utils.DimensionUtil;

/**
 * Created by Phuc-Hau Nguyen on 8/31/2016.
 */
public class DialogPickTransfer extends Dialog {


    @BindView(R.id.button_people)
    AppCompatImageView buttonPeople;

    @BindView(R.id.button_bicycle)
    AppCompatImageView buttonBicycle;

    @BindView(R.id.button_bus)
    AppCompatImageView buttonBus;

    @TransferType
    private int mTransfer = TransferType.MOTORBIKE;

    private Activity mOwnerActivity = null;

    private CallBackWith<Integer> mDialogResult;

    public DialogPickTransfer(Context context) {
        super(context, R.style.AppTheme_CustomDialog);

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
        setContentView(R.layout.view_dialog_pick_transfer);
        ButterKnife.bind(this);
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.windowAnimations = R.style.AppTheme_AnimDialog_Grow;
            params.width = DimensionUtil.getScreenWidth(mOwnerActivity) * 4 / 5;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(params);
        }
        onApplyData();
    }

    private void onApplyData() {
        onBicycleClick();
    }

    @OnClick(R.id.button_people)
    public void onPeopleClick() {
        mTransfer = TransferType.WALK;
        dismiss();
    }

    @OnClick(R.id.button_bus)
    public void onBusClick() {
        mTransfer = TransferType.BUS;
        dismiss();
    }

    @OnClick(R.id.button_bicycle)
    public void onBicycleClick() {
        mTransfer = TransferType.MOTORBIKE;
        dismiss();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (mDialogResult != null) {
            mDialogResult.run(mTransfer);
        }
    }

    public void setDialogResult(CallBackWith<Integer> dialogResult) {
        mDialogResult = dialogResult;
    }
}