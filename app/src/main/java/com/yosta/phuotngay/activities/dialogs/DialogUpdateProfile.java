package com.yosta.phuotngay.activities.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import com.yosta.phuotngay.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Phuc-Hau Nguyen on 8/31/2016.
 */
public class DialogUpdateProfile extends Dialog {

    private Activity mOwnerActivity = null;

    public DialogUpdateProfile(Context context) {
        super(context, R.style.AppTheme_CustomDialog);
        Window window = getWindow();
        if (window != null) {
            window.getAttributes().windowAnimations = R.style.AppTheme_AnimDialog_Grow;
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
        setContentView(R.layout.view_dialog_user_info);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button_left)
    @Override
    public void dismiss() {
        super.dismiss();
    }

    @OnClick(R.id.button_right)
    public void onSave() {
        Toast.makeText(mOwnerActivity, "Thành công", Toast.LENGTH_SHORT).show();
        dismiss();
    }
}