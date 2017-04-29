package io.yostajsc.core.utils;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Base64;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.DecimalFormat;

import static android.util.Base64.encodeToString;

/**
 * Created by Phuc-Hau Nguyen on 3/3/2017.
 */

public class StringUtils {

    private static StringUtils mInstance = null;

    private StringUtils(Activity activity) {

    }

    public static StringUtils inject(Activity activity) {
        if (mInstance == null)
            mInstance = new StringUtils(activity);
        return mInstance;
    }

    static final byte[] HEX_CHAR_TABLE = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102};

    public static String byteArrayToHexString(byte[] paramArrayOfByte) {
        int i = 0;
        byte[] arrayOfByte = new byte[2 * paramArrayOfByte.length];
        int j = paramArrayOfByte.length;
        int k = 0;
        while (i < j) {
            int m = 0xFF & paramArrayOfByte[i];
            int n = k + 1;
            arrayOfByte[k] = HEX_CHAR_TABLE[(m >>> 4)];
            k = n + 1;
            arrayOfByte[n] = HEX_CHAR_TABLE[(m & 0xF)];
            i++;
        }
        return new String(arrayOfByte);
    }

    private static String bin2hex(byte[] paramArrayOfByte) {
        String str = "%0" + 2 * paramArrayOfByte.length + "x";
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = new BigInteger(1, paramArrayOfByte);
        return String.format(str, arrayOfObject);
    }

    public static String md5(String paramString) {
        try {
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            localMessageDigest.update(paramString.getBytes());
            byte[] arrayOfByte = localMessageDigest.digest();
            StringBuilder localStringBuffer = new StringBuilder();
            for (byte anArrayOfByte : arrayOfByte) {
                localStringBuffer.append(Integer.toHexString(0xFF & anArrayOfByte));
            }
            return localStringBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String trim(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        String result = str.trim();
        return result.length() == 0 ? null : result;
    }

    public static String getAssetString(Activity activity, String filename) {
        try {
            InputStream inputStream = activity.getAssets().open(filename);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            return encodeToString(buffer, Base64.NO_WRAP);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String doubleToString(double paramDouble) {
        return doubleToString(paramDouble, 2);
    }

    private static String doubleToString(double paramDouble, int paramInt) {
        return new BigDecimal(paramDouble).setScale(paramInt, 4).toString();
    }

    public static String floatToString(float paramFloat) {
        return new DecimalFormat("0.00").format(paramFloat);
    }

}
