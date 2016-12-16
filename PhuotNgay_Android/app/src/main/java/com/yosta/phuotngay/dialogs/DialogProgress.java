package com.yosta.phuotngay.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.view.Window;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.yosta.phuotngay.R;
import com.yosta.phuotngay.interfaces.DialogBehavior;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Phuc-Hau Nguyen on 8/31/2016.
 */
public class DialogProgress extends Dialog implements DialogBehavior {


    @BindView(R.id.image_view)
    AppCompatImageView imgLauncher;

    private Activity ownerActivity = null;

    public DialogProgress(Context context) {
        super(context);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
    }

    public DialogProgress(Context context,
                          boolean isCanceledOnTouchOutside,
                          boolean isCancelable) {
        super(context, R.style.AppTheme_CustomDialog);
        onAttachedWindow(context);
        setCancelable(isCancelable);
        setCanceledOnTouchOutside(isCanceledOnTouchOutside);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        ownerActivity = getOwnerActivity();
    }
    public void onAttachedWindow(Context context) {
        Window window = getWindow();
        if (window != null) {
            window.getAttributes().windowAnimations = R.style.AppTheme_AnimDialog_Grow;
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
        Glide.with(ownerActivity)
                .load(R.drawable.ic_launcher_anim)
                .asGif()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .error(R.mipmap.ic_launcher)
                .crossFade()
                .into(imgLauncher);
    }

    @Override
    public void onApplyData() {

    }

    @Override
    public void onApplyEvents() {

    }
}