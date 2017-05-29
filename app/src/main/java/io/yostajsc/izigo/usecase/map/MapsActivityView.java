package io.yostajsc.izigo.usecase.map;

import android.view.View;
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

    /*public static void setSuggestion(String name, String cover, String description, String type,
                                     LatLng from, LatLng to) {
        try {
            if (mInstance == null)
                throw new Exception(ERROR_UNBOUND);

            mActivity.layoutSuggestion.bind(name, cover, description, type, from, to);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    public static void unbind() {
        mActivity = null;
        mInstance = null;
    }

}
