package io.yostajsc.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * Created by Phuc-Hau Nguyen on 3/3/2017.
 */

public class NetworkUtils {

    public static int getConnectionType(Context paramContext) {
        NetworkInfo localNetworkInfo = ((ConnectivityManager) paramContext.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if ((localNetworkInfo != null) && (localNetworkInfo.isConnected())) {
            if (localNetworkInfo.getType() == 1) {
                return ConnectivityManager.TYPE_WIFI;
            }
            if (localNetworkInfo.getType() == 0) {
                return ConnectivityManager.TYPE_MOBILE;
            }
        }
        return ConnectivityManager.TYPE_DUMMY;
    }

    public static String getSimOperator(Context paramContext) {
        return ((TelephonyManager) paramContext.getSystemService(Context.TELEPHONY_SERVICE)).getSimOperator();
    }

    public static boolean isConnectedMobile(Context paramContext) {
        NetworkInfo localNetworkInfo = ((ConnectivityManager) paramContext.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return (localNetworkInfo != null) && (localNetworkInfo.isConnected()) && (localNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE);
    }

    public static boolean isConnectedWifi(Context paramContext) {
        NetworkInfo localNetworkInfo = ((ConnectivityManager) paramContext.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return (localNetworkInfo != null) && (localNetworkInfo.isConnected()) && (localNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI);

    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return (connectivityManager.getActiveNetworkInfo() != null);
    }

    public static boolean isOnline(Context paramContext) {
        boolean bool = false;
        try {
            NetworkInfo localNetworkInfo = ((ConnectivityManager) paramContext.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
            if (localNetworkInfo != null) {
                bool = localNetworkInfo.isConnectedOrConnecting();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bool;
    }
}
