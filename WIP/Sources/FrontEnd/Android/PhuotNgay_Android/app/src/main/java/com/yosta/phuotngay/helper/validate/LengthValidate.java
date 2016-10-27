package com.yosta.phuotngay.helper.validate;

import android.support.design.widget.TextInputEditText;

/**
 * Created by Phuc-Hau Nguyen on 8/31/2016.
 */
public class LengthValidate implements IValidate {

    public static String ERROR = "";
    private int mMin = 1, mMax = 5;

    public LengthValidate(int max) {
        this.mMax = max;
    }

    public LengthValidate(int min, int max) {
        this.mMin = min;
        this.mMax = max;
    }

    @Override
    public boolean IsValid(String text) {
        int length = text.length();
        return (length >= mMin && length <= mMax);
    }

    @Override
    public boolean IsValid(TextInputEditText editText, boolean isShowErr) {
        int length = editText.getText().toString().length();
        boolean isValid = length <= mMax && length >= mMin;
        if (!isValid && isShowErr) {
            editText.setError(ERROR);
        }
        return isValid;
    }
}
