package io.yostajsc.sdk.api.cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.LruCache;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.yostajsc.sdk.consts.CallBack;
import io.yostajsc.sdk.api.model.trip.IgImage;
import io.yostajsc.sdk.api.model.trip.IgTrip;
import io.yostajsc.sdk.utils.LogUtils;

/**
 * Created by nphau on 4/13/17.
 */

public class IgCache {

    public static final String TAG = IgCache.class.getSimpleName();

    public static final int SIZE_DEFAULT_UNIT = 1024;

    private static IgCache mInstance = null;

    private IgCache() {

    }

    public static IgCache connect() {
        if (mInstance == null)
            mInstance = new IgCache();
        return mInstance;
    }

    public static class BitmapsCache {

        private static int maxMemory = 0;
        private static BitmapsCache mInstance = null;
        private static LruCache<String, Bitmap> mBitmapCaching = null;

        private BitmapsCache() {

            maxMemory = (int) (Runtime.getRuntime().maxMemory() / SIZE_DEFAULT_UNIT);

            // Use 1/8th of the available memory for this memory cache.
            int cacheSize = maxMemory / 8;

            mBitmapCaching = new LruCache<String, Bitmap>(cacheSize) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    // The cache size will be measured in kilobytes rather than
                    // number of items.
                    return bitmap.getByteCount() / SIZE_DEFAULT_UNIT;
                }
            };
        }

        public static BitmapsCache askForMemory() {
            if (mInstance == null)
                mInstance = new BitmapsCache();
            return mInstance;
        }

        private void cache(String key, Bitmap bitmap) {
            if (get(key) == null) {
                mBitmapCaching.put(key, bitmap);
            }
        }

        public Bitmap get(String key) {
            if (mBitmapCaching == null)
                return null;
            return mBitmapCaching.get(key);
        }

        public List<IgImage> getAll() {
            if (mBitmapCaching == null)
                return null;
            Log.e(TAG, "Size: " + mBitmapCaching.maxSize());
            Map<String, Bitmap> maps = mBitmapCaching.snapshot();
            if (maps.size() < 1)
                return null;
            List<IgImage> res = new ArrayList<>();
            for (Map.Entry<String, Bitmap> entry : maps.entrySet()) {
                res.add(new IgImage(entry.getKey(), entry.getValue()));
            }
            return res;
        }

        public static void clear() {
            mInstance = null;
            mBitmapCaching = null;
        }

        public void cache(IgImage... cachingItems) {
            try {
                new BitmapWorkerTask().execute(cachingItems);
            } catch (Exception e) {
                LogUtils.log(TAG, e.getMessage());
            }
        }

        private class BitmapWorkerTask extends AsyncTask<IgImage, Void, Boolean> {

            @Override
            protected Boolean doInBackground(IgImage... cachingItems) {

                try {

                    for (IgImage cachingItem : cachingItems) {

                        String key = cachingItem.getId();                // Key
                        URL imageUrl = new URL(cachingItem.getUrl());    // Url

                        // Downloading
                        HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();
                        connection.setDoInput(true);
                        connection.connect();
                        InputStream is = connection.getInputStream();

                        // Convert to bitmap
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inPreferredConfig = Bitmap.Config.RGB_565;
                        Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);

                        // Add to mem cache
                        cache(key, bitmap);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
                return true;
            }
        }
    }

    public static class TripCache {

        private static IgTrip mIgTrip = null;

        public static void cacheAlbum(List<IgImage> igImages) {
            if (mIgTrip == null)
                mIgTrip = new IgTrip();
            mIgTrip.setAlbum(igImages);
        }

        public static List<IgImage> getAlbum() {
            if (mIgTrip == null)
                return null;
            return mIgTrip.getAlbum();
        }
    }

    public void clearAll() {
        BitmapsCache.clear();
    }
}
