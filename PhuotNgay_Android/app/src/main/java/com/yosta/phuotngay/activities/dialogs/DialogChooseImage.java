package com.yosta.phuotngay.activities.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import com.yosta.phuotngay.R;

import butterknife.ButterKnife;

/**
 * Created by Phuc-Hau Nguyen on 8/31/2016.
 */
public class DialogChooseImage extends Dialog {

    public DialogChooseImage(Context context) {
        super(context, R.style.AppTheme_CustomDialog);
        getWindow().getAttributes().windowAnimations = R.style.AppTheme_AnimDialog_SlideLeftRight;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.view_dialog_choose_image);
        ButterKnife.bind(this);
    }
}