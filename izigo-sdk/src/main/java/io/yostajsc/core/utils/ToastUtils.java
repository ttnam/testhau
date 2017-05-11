package io.yostajsc.core.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by nphau on 5/11/17.
 */

public class ToastUtils {

    public static void showToast(Activity activity, String message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

}
