package io.yostajsc.izigo.activities.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.yostajsc.izigo.R;

/**
 * Created by Phuc-Hau Nguyen on 8/31/2016.
 */
public class DialogAddFriends extends Dialog {

    private Activity mOwnerActivity = null;

    public DialogAddFriends(Context context) {
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
        setContentView(R.layout.view_dialog_add_friend);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button_sms)
    public void onInviteViaSMS() {
        Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("sms_body", "Link");
        mOwnerActivity.startActivity(Intent.createChooser(smsIntent, "SMS:"));
        dismiss();
    }

    @OnClick(R.id.button_email)
    public void onInviteViaEmail() {
        dismiss();
    }
}