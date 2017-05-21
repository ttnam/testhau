package io.yostajsc.izigo.usecase.map;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Switch;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.yostajsc.core.utils.DimensionUtil;
import io.yostajsc.izigo.AppConfig;
import io.yostajsc.izigo.R;

/**
 * Created by Phuc-Hau Nguyen on 8/31/2016.
 */

public class DialogMapSetting extends Dialog {

    @BindView(R.id.switch_available)
    Switch switchAvailable;

    private Activity mOwnerActivity = null;
    private OnOnlineListener mListener = null;

    public DialogMapSetting(Context context, OnOnlineListener listener) {
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
        setContentView(R.layout.view_dialog_map_setting);
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

    @OnClick(R.id.switch_available)
    public void selectOngoing() {
        try {
            if (mListener == null)
                return;
            mListener.run(switchAvailable.isChecked());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void show() {
        super.show();
        switchAvailable.setChecked(AppConfig.getInstance().isAvailable());
    }

    public interface OnOnlineListener {
        void run(boolean isOnline);
    }
}