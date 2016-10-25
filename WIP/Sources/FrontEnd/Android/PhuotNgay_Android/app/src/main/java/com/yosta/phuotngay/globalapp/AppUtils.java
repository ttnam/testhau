package com.yosta.phuotngay.globalapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.AnyRes;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.Html;
import android.util.Base64;
import android.view.View;

import com.yosta.phuotngay.R;
import com.yosta.phuotngay.adapter.MenuAdapter;
import com.yosta.phuotngay.helper.menu.MenuItem;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class AppUtils {

    public static final String EXTRA_INTENT = "EXTRA_INTENT";

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

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return (connectivityManager.getActiveNetworkInfo() != null);
    }

    public static boolean isGPSEnable(Context context) {
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static void showSnackBarNotify(View v, String msg, int lenght) {
        Snackbar.make(v, Html.fromHtml("<font color=\"yellow\">" + msg + "</font>"), lenght).show();
    }

    public static void showSnackBarNotify(View v, String msg) {
        if (v == null)
            return;
        Snackbar.make(v, Html.fromHtml("<font color=\"yellow\">" + msg + "</font>"), Snackbar.LENGTH_LONG).show();
    }

    public static void showSnackBarNotifyWithAction(
            View v,
            String msg,
            String action,
            int duratian,
            View.OnClickListener listener) {

        Snackbar.make(v, Html.fromHtml("<font color=\"white\">" + msg + "</font>"), Snackbar.LENGTH_SHORT)
                .setAction(action, listener)
                .setDuration(duratian)
                .setActionTextColor(ColorStateList.valueOf(Color.GREEN))
                .show();
    }

    public static String getAppVersion(Context context) {
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

    public static Serializable receiveDataThroughBundle(Activity activity, String key) {
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

    public static boolean sendObjectThroughBundle(Context srcContext, Class destClass, String key, Serializable object, boolean isCall) {
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

    public static String[] StandardizeTime(String pattern) {

        String[] res = new String[4];

        String[] temp = pattern.split("-");

        res[0] = temp[0]; // year
        res[1] = temp[1]; // month
        res[2] = temp[2].substring(0, 2); // day
        res[3] = temp[2].substring(4, temp[2].length());

        return res;
    }

    public static boolean StandardizeLoginValue(String response) {
        return Boolean.parseBoolean(response.substring((response.indexOf(':') + 2), response.length() - 1));
    }

    /*
        public static void onCallLoginDialog(final Context context) {

            String msgContent = context.getString(R.string.message_login);
            String tittle = context.getString(R.string.str_login).toUpperCase();

            new StandardDialog(context).setIcon(R.drawable.ic_vector_person_white)
                    .setTopColorRes(R.color.colorPrimaryDark)
                    .setButtonsColorRes(R.color.colorPrimaryDark)
                    .setTitleGravity(Gravity.CENTER)
                    .setTitle(tittle)
                    .setMessage(msgContent)
                    .setPositiveButton(R.string.str_login, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            context.startActivity(new Intent(context, LoginActivity.class));
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .show();

        }*/
    public static
    @Nullable
    MenuAdapter LoadListMenuAction(Context context, @AnyRes int textArrID, @AnyRes int iconArrID) {
        try {
            String[] text = context.getResources().getStringArray(textArrID);
            TypedArray icon = context.getResources().obtainTypedArray(iconArrID);
            int iText = text.length;
            int iIcon = icon.length();
            if (iText == iIcon && iText > 0) {
                ArrayList<MenuItem> arrayList = new ArrayList<>();
                for (int i = 0; i < iText; i++)
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


    public static String getKeyHash(Context context) {

        String keyHash = "";
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                keyHash = Base64.encodeToString(md.digest(), Base64.DEFAULT);
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException ignored) {

        }
        return keyHash;
    }
}
