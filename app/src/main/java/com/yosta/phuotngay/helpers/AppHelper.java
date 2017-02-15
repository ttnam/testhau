package com.yosta.phuotngay.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.AnyRes;
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

import com.android.annotations.NonNull;
import com.yosta.phuotngay.R;
import com.yosta.phuotngay.adapters.MenuAdapter;
import com.yosta.phuotngay.models.menu.MenuItem;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Phuc-Hau Nguyen on 12/21/2016.
 */

public class AppHelper {

    public static final int H_MM = 0;
    public static final int DD_MM_YYYY = 1;

    public static final String EXTRA_INTENT = "EXTRA_INTENT";
    public static final String EXTRA_TRIP = "EXTRA_TRIP";
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

    public static class Builder {

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

    public static boolean isGPSEnable(Context context) {
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return (connectivityManager.getActiveNetworkInfo() != null);
    }

    private static boolean isWifiConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return (info.getState() == NetworkInfo.State.CONNECTED);
    }

    private static boolean isMobileDataConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return (info.getState() == NetworkInfo.State.CONNECTED);
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

    public static Serializable onReceiveDataThroughBundle(Activity activity, String key) {
        Serializable serializable = null;
        try {
            Intent intent = activity.getIntent();
            Bundle bundle = intent.getExtras();
            serializable = bundle.getSerializable(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serializable;
    }

    public static boolean onSendObjectThroughBundle(Context srcContext, Class destClass, String key, Serializable object, boolean isCall) {
        try {
            Intent intent = new Intent(srcContext, destClass);
            intent.putExtra(key, object);
            if (isCall) {
                srcContext.startActivity(intent);
                ((Activity) srcContext).overridePendingTransition(R.anim.anim_slide_in_up, R.anim.anim_slide_out_up);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
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

    public static int onGetScreenHeight(Activity activity) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.heightPixels;
    }

    public static int onGetScreenWidth(Activity activity) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.widthPixels;
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