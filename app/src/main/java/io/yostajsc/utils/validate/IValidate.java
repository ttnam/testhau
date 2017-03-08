package io.yostajsc.utils.validate;

import android.support.design.widget.TextInputEditText;

/**
 * Created by Phuc-Hau Nguyen on 8/31/2016.
 */
public interface IValidate {
    boolean IsValid(String text);

    boolean IsValid(TextInputEditText editText, boolean isShowErr);
}
