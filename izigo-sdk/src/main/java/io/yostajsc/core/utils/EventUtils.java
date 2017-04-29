package io.yostajsc.core.utils;

import android.text.Editable;
import android.text.TextWatcher;

import io.yostajsc.core.interfaces.CallBack;

/**
 * Created by Phuc-Hau Nguyen on 2/16/2017.
 */

public class EventUtils {

    private static EventUtils mInstance = null;

    private EventUtils() {

    }

    public static EventUtils connect() {
        if (mInstance == null) {
            mInstance = new EventUtils();
        }
        return mInstance;
    }

    public TextWatcher addAfterTextChangeListener(final CallBack back) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                back.run();
            }
        };
    }


}
