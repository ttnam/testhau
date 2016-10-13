package com.yosta.phuotngay.activities.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import com.yosta.phuotngay.R;
import com.yosta.phuotngay.activities.interfaces.DialogBehavior;

import butterknife.ButterKnife;

/**
 * Created by Phuc-Hau Nguyen on 8/31/2016.
 */
public class DialogProgress extends Dialog implements DialogBehavior {

    private Activity ownerActivity = null;

    public DialogProgress(Context context) {
        super(context, R.style.AppTheme_CustomDialog);
        setCancelable(true);
        onAttachedWindow(context);

    }

    public DialogProgress(Context context, boolean isCancelable) {
        super(context, R.style.AppTheme_CustomDialog);
        setCancelable(isCancelable);
        onAttachedWindow(context);

    }
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        ownerActivity = getOwnerActivity();
    }

    @Override
    public void onAttachedWindow(Context context) {
        Window window = getWindow();
        if (window != null) {
            window.getAttributes().windowAnimations =
                    R.style.AppTheme_AnimDialog_Grow;
        }
        ownerActivity = (context instanceof Activity) ? (Activity) context : null;
        if (ownerActivity != null)
            setOwnerActivity(ownerActivity);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.view_dialog_progress);
        ButterKnife.bind(this);
        onApplyComponents();
        onApplyEvents();
        onApplyData();
    }

    @Override
    public void onApplyComponents() {

    }

    @Override
    public void onApplyData() {

    }

    @Override
    public void onApplyEvents() {

    }
}