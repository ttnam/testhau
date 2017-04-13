package io.yostajsc.izigo.activities.user;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import io.yostajsc.izigo.R;

/**
 * Created by nphau on 4/11/17.
 */

public class LoginActivityView {

    private static LoginActivityView mInstance = null;

    private LoginActivityView() {
    }

    public static LoginActivityView inject() {
        mInstance = new LoginActivityView();
        return mInstance;
    }


    public LoginActivityView setLogo(LoginActivity activity) {
        Glide.with(activity)
                .load(R.drawable.ic_loading)
                .skipMemoryCache(false)
                .error(R.drawable.ic_launcher)
                .priority(Priority.IMMEDIATE)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(activity.imageLogo);
        return this;
    }
}
