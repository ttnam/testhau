package io.yostajsc.core.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
/**
 * Created by Phuc-Hau Nguyen on 12/21/2016.
 */

public class AppUtils {

    public static final int H_MM = 0;
    public static final int DD_MM_YYYY = 1;

    public static final String EXTRA_TRIPS = "EXTRA_TRIPS";

    public static final String FONT_LATO_BLACK = "fonts/Lato-Black.ttf";
    public static final String FONT_LATO_BOLD = "fonts/Lato-Bold.ttf";
    public static final String FONT_LATO_HEAVY = "fonts/Lato-Heavy.ttf";
    public static final String FONT_LATO_LIGHT = "fonts/Lato-Light.ttf";
    public static final String FONT_LATO_MEDIUM = "fonts/Lato-Medium.ttf";
    public static final String FONT_LATO_THIN = "fonts/Lato-Thin.ttf";
    public static final String FONT_LATO_ITALIC = "fonts/Lato-Italic.ttf";

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(Activity activity) {
        return new Builder(activity);
    }

    public static Builder builder(Context context) {
        return new Builder(context);
    }

    public static class Builder {

        private Activity mActivity = null;
        private Context mContext = null;
        private Resources mResources = null;

        public Builder() {
            mResources = null;
            mContext = null;
            mActivity = null;
        }

        public Builder(Activity activity) {
            this.mActivity = activity;
            this.mResources = mActivity.getResources();
        }

        public Builder(Context context) {
            this.mContext = context;
            this.mResources = mContext.getResources();
        }



        public String getTime(long millis, int filter) {
            Timestamp stamp = new Timestamp(millis);
            Date date = new Date(stamp.getTime());
            SimpleDateFormat sdf = null;
            if (filter == DD_MM_YYYY)
                sdf = new SimpleDateFormat("dd/MM/yyyy");
            if (filter == H_MM)
                sdf = new SimpleDateFormat("h:mm a");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            return sdf.format(date);
        }

        public long getTimeStep(long millis) {
            long currTimes = System.currentTimeMillis();
            return (currTimes - millis);
        }

        public void setFont(@NonNull String fontName, @NonNull TextView... textViews) {
            Context context = textViews[0].getContext();
            Typeface font = Typeface.createFromAsset(context.getAssets(), fontName);
            for (TextView textView : textViews) {
                textView.setTypeface(font);
            }
        }

        public void setFont(@NonNull String fontName, @NonNull Button... buttons) {
            Context context = buttons[0].getContext();
            Typeface font = Typeface.createFromAsset(context.getAssets(), fontName);
            for (Button button : buttons) {
                button.setTypeface(font);
            }
        }

        public void setFont(@NonNull String fontName, @NonNull CheckBox... checkBoxes) {
            Context context = checkBoxes[0].getContext();
            Typeface font = Typeface.createFromAsset(context.getAssets(), fontName);
            for (CheckBox checkBox : checkBoxes) {
                checkBox.setTypeface(font);
            }
        }
    }

    public static String onGetAppVersion(Context context) {
        PackageManager manager = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (info != null) {
            return info.versionName;
        }
        return null;
    }

    public static String onGetKeyHash(Context context) {

        String keyHash = "";
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                keyHash = Base64.encodeToString(md.digest(), Base64.DEFAULT);
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException ignored) {

        }
        return keyHash;
    }

    public static void onCloseVirtualKeyboard(Activity activity) {
        InputMethodManager inputManager =
                (InputMethodManager) activity.
                        getSystemService(Context.INPUT_METHOD_SERVICE);

        View view = activity.getCurrentFocus();
        if (view != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
