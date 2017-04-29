package io.yostajsc.core.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.lang.reflect.Field;

/**
 * Created by nphau on 3/23/17.
 */

public class FontUtils {


    private static String TAG = FontUtils.class.getSimpleName();

    public static void setFont(@NonNull String fontName, @NonNull TextView... textViews) {
        Context context = textViews[0].getContext();
        Typeface font = Typeface.createFromAsset(context.getAssets(), fontName);
        for (TextView textView : textViews) {
            textView.setTypeface(font);
        }
    }

    public static void setFont(@NonNull String fontName, @NonNull Button... buttons) {
        Context context = buttons[0].getContext();
        Typeface font = Typeface.createFromAsset(context.getAssets(), fontName);
        for (Button button : buttons) {
            button.setTypeface(font);
        }
    }

    public static void setFont(@NonNull String fontName, @NonNull CheckBox... checkBoxes) {
        Context context = checkBoxes[0].getContext();
        Typeface font = Typeface.createFromAsset(context.getAssets(), fontName);
        for (CheckBox checkBox : checkBoxes) {
            checkBox.setTypeface(font);
        }
    }

    public static void overrideFont(Context context, String defaultFontNameToOverride, String customFontFileNameInAssets) {
        try {
            final Typeface customFontTypeface = Typeface.createFromAsset(context.getAssets(), customFontFileNameInAssets);
            final Field defaultFontTypefaceField = Typeface.class.getDeclaredField(defaultFontNameToOverride);
            defaultFontTypefaceField.setAccessible(true);
            defaultFontTypefaceField.set(null, customFontTypeface);
        } catch (Exception e) {
            String tag = "Can not set custom font " + customFontFileNameInAssets + " instead of " + defaultFontNameToOverride;
            Log.e(TAG, tag);
        }
    }
}
