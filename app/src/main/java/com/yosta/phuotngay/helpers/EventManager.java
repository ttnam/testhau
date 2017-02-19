package com.yosta.phuotngay.helpers;

import android.text.Editable;
import android.text.TextWatcher;

import com.yosta.phuotngay.interfaces.CallBack;

/**
 * Created by Phuc-Hau Nguyen on 2/16/2017.
 */

public class EventManager {

    private static EventManager mInstance = null;

    private TextWatcher textWatcher = null;

    private EventManager() {

    }

    public static EventManager connect() {
        if (mInstance == null) {
            mInstance = new EventManager();
        }
        return mInstance;
    }

    public TextWatcher addTextWatcherEvent(final CallBack back) {
        textWatcher = new TextWatcher() {
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
        return textWatcher;
    }
}
