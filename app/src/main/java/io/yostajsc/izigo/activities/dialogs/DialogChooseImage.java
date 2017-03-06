package io.yostajsc.izigo.activities.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.yostajsc.constants.MessageInfo;
import io.yostajsc.constants.MessageType;
import io.yostajsc.izigo.R;

/**
 * Created by Phuc-Hau Nguyen on 8/31/2016.
 */
public class DialogChooseImage extends Dialog {

    private Activity mOwnerActivity = null;

    public DialogChooseImage(Context context) {
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
        setContentView(R.layout.view_dialog_choose_image);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.layout_from_gallery_image)
    public void onSelectGallery() {
        EventBus.getDefault().post(new MessageInfo(MessageType.FROM_GALLERY));
        dismiss();
    }

    @OnClick(R.id.layout_take_photo)
    public void onTakePhoto() {
        EventBus.getDefault().post(new MessageInfo(MessageType.TAKE_PHOTO));
        dismiss();
    }
}