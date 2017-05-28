package io.yostajsc.izigo.usecase.map;

import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;

import io.yostajsc.izigo.R;
import io.yostajsc.izigo.usecase.map.utils.Info;
import io.yostajsc.izigo.usecase.map.utils.MapUtils;
import io.yostajsc.izigo.usecase.map.utils.RouteParserTask;

/**
 * Created by nphau on 5/27/17.
 */

public class MapsActivityView {

    private static final String ERROR_UNBOUND = "You must bind first!";

    private static MapsActivity mActivity = null;
    private static MapsActivityView mInstance = null;

    private MapsActivityView(MapsActivity activity) {
        mActivity = activity;

        mActivity.ownToolbar.setBinding(
                R.drawable.ic_vector_back_blue,
                R.drawable.ic_vector_menu_blue,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mActivity.onBackPressed();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mActivity.openMenu();
                    }
                });
    }

    public static synchronized MapsActivityView bind(MapsActivity activity) {
        mInstance = new MapsActivityView(activity);
        return mInstance;
    }

    public static void setSuggestion(String name, String cover, String description, String type,
                                     LatLng from, LatLng to) {
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
            MapUtils.Map.direction(from, to, new RouteParserTask.OnDirectionCallBack() {
                @Override
                public void onSuccess(Info info, Polyline polyline) {
                    if (!TextUtils.isEmpty(info.strDistance)) {
                        mActivity.textSuggestDistance.setText(info.strDistance);
                    }
                }
            });
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
