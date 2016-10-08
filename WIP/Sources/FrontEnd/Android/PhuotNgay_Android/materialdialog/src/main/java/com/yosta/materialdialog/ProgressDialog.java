package com.yosta.materialdialog;

import android.content.Context;

public class ProgressDialog extends AbsDialog<ProgressDialog> {

    public ProgressDialog(Context context) {
        super(context);
    }

    public ProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    {
        setCancelable(false);
    }

    @Override
    protected int getLayout() {
        return R.layout.dialog_progress;
    }
}
