package io.yostajsc.core.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import io.yostajsc.core.R;
import io.yostajsc.core.utils.DimensionUtil;
import io.yostajsc.core.utils.NetworkUtils;

/**
 * Created by Phuc-Hau Nguyen on 8/31/2016.
 */
public class DialogNoNet extends Dialog {

    private Activity mOwnerActivity = null;

    private
    @DrawableRes
    int mIcon;

    public DialogNoNet(Context context) {
        super(context, R.style.CoreAppTheme_Dialog);

        this.mOwnerActivity = (context instanceof Activity) ? (Activity) context : null;
        if (this.mOwnerActivity != null)
            setOwnerActivity(mOwnerActivity);
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
        setContentView(R.layout.view_dialog_no_net);
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.windowAnimations = R.style.CoreAppTheme_AnimDialog_Grow;
            params.width = DimensionUtil.getScreenWidth(mOwnerActivity) * 4 / 5;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(params);
        }
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtils.isNetworkConnected(mOwnerActivity)) {
                    dismiss();
                }
            }
        });
        ((AppCompatImageView) findViewById(R.id.image_view)).setImageResource(mIcon);
    }

    public void setIcon(@DrawableRes int icon) {
        mIcon = icon;
    }
}