package com.yosta.phuotngay.activities.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.view.Window;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.yosta.phuotngay.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Phuc-Hau Nguyen on 8/31/2016.
 */
public class DialogProgress extends Dialog {

    private Activity mOwnerActivity = null;

    @BindView(R.id.image_view)
    AppCompatImageView imageView;

    public DialogProgress(Context context) {
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
        setContentView(R.layout.view_dialog_progress);
        ButterKnife.bind(this);

        Glide.with(getOwnerActivity())
                .load(R.drawable.ic_launcher_anim)
                .into(imageView);
    }
}