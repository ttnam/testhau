package io.yostajsc.core.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by nphau on 3/17/17.
 */

public class AssetsUtils {

    public static String getJsonAsString(String filename, Context context) throws Exception {
        AssetManager manager = context.getAssets();
        StringBuilder buf = new StringBuilder();
        InputStream json = manager.open(filename);
        BufferedReader in = new BufferedReader(new InputStreamReader(json, "UTF-8"));
        String str;

        while ((str = in.readLine()) != null) {
            buf.append(str);
        }

        in.close();

        return buf.toString();
    }
}

