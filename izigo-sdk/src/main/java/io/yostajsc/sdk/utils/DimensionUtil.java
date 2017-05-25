package io.yostajsc.sdk.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by Phuc-Hau Nguyen on 3/3/2017.
 */

public class DimensionUtil {

    private static String mDensity = null;

    public static float dpFromPx(Context paramContext, float paramFloat) {
        return paramFloat / paramContext.getResources().getDisplayMetrics().density;
    }

    public static int dp2px(Context context, int dp) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        DisplayMetrics displaymetrics = new DisplayMetrics();
        display.getMetrics(displaymetrics);

        return (int) (dp * displaymetrics.density + 0.5f);
    }

    public static float getDensityNumber(Context paramContext) {
        return paramContext.getResources().getDisplayMetrics().density;
    }

    public static String getDeviceResolution(Context paramContext) {
        if (mDensity != null) {
            return mDensity;
        }
        if (paramContext == null) {
            return "dpi";
        }
        int i = paramContext.getResources().getDisplayMetrics().densityDpi;
        if (i > 480) {
            mDensity = "xxxhdpi";
        } else if (i > 320) {
            mDensity = "xxhdpi";
        } else if (i > 240) {
            mDensity = "xhdpi";
        } else if (i > 160) {
            mDensity = "hdpi";
        } else if (i > 120) {
            mDensity = "mdpi";
        } else {
            mDensity = "dpi";
        }
        return mDensity;
    }

    public static int getScreenHeight(Context paramContext) {
        return paramContext.getResources().getDisplayMetrics().heightPixels;
    }

    public static int getScreenWidth(Context paramContext) {
        return paramContext.getResources().getDisplayMetrics().widthPixels;
    }
}
