package io.yostajsc.izigo.usecase.map;

import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import io.yostajsc.izigo.R;

/**
 * Created by nphau on 5/27/17.
 */

public class MapsActivityView {

    private static final String ERROR_UNBOUND = "You must bind first!";

    private static MapsActivity mActivity = null;
    private static MapsActivityView mInstance = null;

    private MapsActivityView(MapsActivity activity) {
        mActivity = activity;
    }

    public static synchronized MapsActivityView bind(MapsActivity activity) {
        mInstance = new MapsActivityView(activity);
        return mInstance;
    }

    public static void setSuggestion(String name, String cover, String description, String distance, String type) {
        try {
            if (mInstance == null)
                throw new Exception(ERROR_UNBOUND);

            Glide.with(mActivity).load(cover)
                    .priority(Priority.IMMEDIATE)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(mActivity.imageSuggestCover);

            if (!TextUtils.isEmpty(name)) {
                mActivity.textSuggestName.setText(name);
            }
            if (!TextUtils.isEmpty(type)) {
                mActivity.textSuggestType.setText(type);
            }
            /*if (!TextUtils.isEmpty(distance)) {
                mActivity.textSuggestDistance.setText(name);
            }*/
            if (!TextUtils.isEmpty(description)) {
                mActivity.textSuggestDescription.setText(description);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void unbind() {
        mActivity = null;
        mInstance = null;
    }

}
