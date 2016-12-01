package com.yosta.phuotngay.helpers.validate;

import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;

import com.yosta.phuotngay.helpers.validate.IValidate;

/**
 * Created by Phuc-Hau Nguyen on 8/31/2016.
 */
public class EmptyValidate implements IValidate {

    public static String ERROR = "This field is required";

    @Override
    public boolean IsValid(String text) {
        return !TextUtils.isEmpty(text);
    }

    @Override
    public boolean IsValid(TextInputEditText editText, boolean isShowErr) {
        boolean isValid = TextUtils.isEmpty(editText.getText().toString());
        if (isValid && isShowErr)
            editText.setError(ERROR);
        return !isValid;
    }
}
