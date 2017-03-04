package com.yosta.utils.validate;

import android.support.design.widget.TextInputEditText;

import java.util.regex.Pattern;

/**
 * Created by Phuc-Hau Nguyen on 8/31/2016.
 */
public class EmailValidate implements IValidate {

    public static String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    public static String ERROR = "Email is not valid";

    @Override
    public boolean IsValid(String text) {
        return Pattern.compile(EMAIL_PATTERN).matcher(text).matches();
    }

    @Override
    public boolean IsValid(TextInputEditText editText, boolean isShowErr) {
        boolean isValid = Pattern.compile(EMAIL_PATTERN).matcher(editText.getText().toString()).matches();
        if (!isValid && isShowErr) {
            editText.setError(ERROR);
        }
        return isValid;
    }
}
