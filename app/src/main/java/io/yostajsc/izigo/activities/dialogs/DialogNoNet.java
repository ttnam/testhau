package io.yostajsc.izigo.activities.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.yostajsc.izigo.R;
import io.yostajsc.utils.AppUtils;
import io.yostajsc.utils.NetworkUtils;

/**
 * Created by Phuc-Hau Nguyen on 8/31/2016.
 */
public class DialogNoNet extends Dialog {

    private Activity mOwnerActivity = null;

    public DialogNoNet(Context context) {
        super(context, R.style.AppTheme_CustomDialog);
        Window window = getWindow();
        if (window != null) {
            window.getAttributes().windowAnimations = R.style.AppTheme_AnimDialog_Grow;
        }
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
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button)
    public void onConfirm() {
        if (NetworkUtils.isNetworkConnected(mOwnerActivity)) {
            dismiss();
        }
    }
}