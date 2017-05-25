package io.yostajsc.sdk.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Base64;
import android.view.View;

/**
 * Created by Phuc-Hau Nguyen on 3/3/2017.
 */

public class BitmapUtil {

    private static final Integer BARCODE_WIDTH = 1024;

    public static Bitmap base64ToImage(String paramString) {
        byte[] arrayOfByte = Base64.decode(paramString, 0);
        return BitmapFactory.decodeByteArray(arrayOfByte, 0, arrayOfByte.length);
    }

    private static String guessAppropriateEncoding(CharSequence contents) {
        for (int i = 0; i < contents.length(); i++) {
            if (contents.charAt(i) > 0xFF) {
                return "UTF-8";
            }
        }
        return null;
    }

    public static int getBitmapSize(Bitmap paramBitmap) {
        return paramBitmap.getRowBytes() * paramBitmap.getHeight();
    }

    public static Bitmap rotateImage(Bitmap paramBitmap, float paramFloat) {
        Matrix localMatrix = new Matrix();
        localMatrix.postRotate(paramFloat);
        try {
            return Bitmap.createBitmap(paramBitmap, 0, 0, paramBitmap.getWidth(), paramBitmap.getHeight(), localMatrix, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap capture(View view) {
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        return bitmap;
    }

}
