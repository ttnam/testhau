package io.yostajsc.izigo.usecase.trip.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.view.Window;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.usecase.trip.CustomTimePicker;

/**
 * Created by Phuc-Hau Nguyen on 8/31/2016.
 */
public class DialogAddActivity extends Dialog {


    private Activity mOwnerActivity = null;
    private OnReceiveNewActivity mDialogResult;

    @BindView(R.id.layout_time_picker)
    CustomTimePicker layoutTimePicker;

    @BindView(R.id.text_view)
    TextInputEditText textView;

    public DialogAddActivity(Context context) {
        super(context, R.style.CoreAppTheme_Dialog);
        this.mOwnerActivity = (context instanceof Activity) ? (Activity) context : null;
        if (this.mOwnerActivity != null)
            setOwnerActivity(mOwnerActivity);
        Window window = getWindow();
        if (window != null) {
            window.getAttributes().windowAnimations = R.style.CoreAppTheme_AnimDialog_Grow;
        }
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
        setContentView(R.layout.view_dialog_add_activity);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button)
    public void confirm() {

        dismiss();

        if (layoutTimePicker.getPlace() == null)
            return;

        String content = textView.getText().toString();
        if (TextUtils.isEmpty(content))
            return;

        if (mDialogResult != null) {
            mDialogResult.onSuccess(content, layoutTimePicker.getPlace().getTime());
        }
    }

    public void setDialogResult(OnReceiveNewActivity dialogResult) {
        mDialogResult = dialogResult;
    }

    public interface OnReceiveNewActivity {
        void onSuccess(String content, long time);
    }
}