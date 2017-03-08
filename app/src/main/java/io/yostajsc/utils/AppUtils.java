package io.yostajsc.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.AnyRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.Html;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import io.yostajsc.izigo.R;
import io.yostajsc.izigo.adapters.MenuAdapter;
import io.yostajsc.izigo.models.menu.MenuItem;

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

        private int pluralsTimeIds[] = {R.plurals.seconds, R.plurals.minutes, R.plurals.hours,
                R.plurals.days, R.plurals.months, R.plurals.years};

        private int pluralsPastTimeIds[] = {R.plurals.seconds_ago, R.plurals.minutes_ago, R.plurals.hours_ago,
                R.plurals.days_ago, R.plurals.months_ago, R.plurals.years_ago};

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

        public Builder showSnackBarNotify(@NonNull View view, @NonNull String message) {
            String msg = view.getResources().getString(R.string.message_snack_bar, message);
            Snackbar.make(view, Html.fromHtml(msg), Snackbar.LENGTH_LONG).show();
            return this;
        }

        public Builder showSnackBarNotify(@NonNull View view, @NonNull String message, int duration,
                                          @NonNull String actionTitle, @NonNull View.OnClickListener listener) {
            String msg = view.getResources().getString(R.string.message_snack_bar, message);
            Snackbar.make(view, Html.fromHtml(msg), Snackbar.LENGTH_LONG)
                    .setDuration(duration)
                    .setAction(actionTitle, listener)
                    .setActionTextColor(ColorStateList.valueOf(Color.GREEN))
                    .show();
            return this;
        }

        public String getTime(long millis, int type) {
            Timestamp stamp = new Timestamp(millis);
            Date date = new Date(stamp.getTime());
            SimpleDateFormat sdf = null;
            if (type == DD_MM_YYYY)
                sdf = new SimpleDateFormat("dd/MM/yyyy");
            if (type == H_MM)
                sdf = new SimpleDateFormat("h:mm a");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            return sdf.format(date);
        }

        public long getTimeStep(long millis) {
            long currTimes = System.currentTimeMillis();
            return (currTimes - millis);
        }

        public String getTimeGap(long timeGap) {
            if (mActivity == null && mContext == null)
                return "";

            int timeConst[] = {1000, 60, 60, 24, 30, 12};
            int timeValue = (int) (timeGap / timeConst[0]);

            String res = mResources.getQuantityString(R.plurals.seconds, timeValue, timeValue);

            for (int i = 1; i < pluralsTimeIds.length; ++i) {
                timeValue /= timeConst[i];
                if (timeValue != 0) {
                    res = mResources.getQuantityString(pluralsTimeIds[i], timeValue, timeValue);
                }
            }
            mActivity = null;
            mContext = null;
            return res;
        }

        public String getPastTimeGap(long timeGap) {
            if (mActivity == null && mContext == null)
                return "";

            int timeConst[] = {1000, 60, 60, 24, 30, 12};
            int timeValue = (int) (timeGap / timeConst[0]);

            String res = mResources.getQuantityString(R.plurals.seconds, timeValue, timeValue);

            for (int i = 1; i < pluralsPastTimeIds.length; ++i) {
                timeValue /= timeConst[i];
                if (timeValue != 0) {
                    res = mResources.getQuantityString(pluralsPastTimeIds[i], timeValue, timeValue);
                }
            }
            mActivity = null;
            mContext = null;
            return res;
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

    public static
    @Nullable
    MenuAdapter LoadListMenuAction(Context context, @AnyRes int textArrID, @AnyRes int iconArrID) {
        try {
            String[] text = context.getResources().getStringArray(textArrID);
            TypedArray icon = context.getResources().obtainTypedArray(iconArrID);
            int iIcon = icon.length();
            if (iIcon > 0) {

                ArrayList<MenuItem> arrayList = new ArrayList<>();
                for (int i = 0; i < iIcon; i++)
                    arrayList.add(new MenuItem(icon.getResourceId(i, -1), text[i]));

                return new MenuAdapter(context, arrayList);
            }
            // Recycle the typed array
            icon.recycle();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
