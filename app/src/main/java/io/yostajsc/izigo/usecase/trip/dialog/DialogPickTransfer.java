package io.yostajsc.izigo.usecase.trip.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.yostajsc.izigo.constants.TransferType;
import io.yostajsc.core.interfaces.CallBackWith;
import io.yostajsc.core.utils.DimensionUtil;
import io.yostajsc.izigo.R;

/**
 * Created by Phuc-Hau Nguyen on 8/31/2016.
 */
public class DialogPickTransfer extends Dialog {

    @TransferType
    private int mTransfer = TransferType.MOTORBIKE;

    private Activity mOwnerActivity = null;

    private CallBackWith<Integer> mDialogResult;

    public DialogPickTransfer(Context context) {
        super(context, R.style.CoreAppTheme_Dialog);

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
            params.windowAnimations = R.style.CoreAppTheme_AnimDialog_Grow;
            params.width = DimensionUtil.getScreenWidth(mOwnerActivity) * 4 / 5;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(params);
        }
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