package io.yostajsc.sdk.fragments;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by nphau on 3/19/17.
 */

public class CoreFragment extends Fragment {

    protected Context mContext = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getContext();
    }
}
