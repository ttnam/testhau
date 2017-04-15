package io.yostajsc.izigo.activities.trip;

import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import io.yostajsc.constants.RoleType;
import io.yostajsc.core.utils.DatetimeUtils;
import io.yostajsc.izigo.R;
import io.yostajsc.utils.UiUtils;
import io.yostajsc.core.glide.CropCircleTransformation;

/**
 * Created by nphau on 4/7/17.
 */

public class TripDetailActivityView {

    private TripDetailActivity mActivity = null;
    private static TripDetailActivityView mInstance = null;

    private TripDetailActivityView(TripDetailActivity activity) {
        this.mActivity = activity;
    }

    public static TripDetailActivityView inject(TripDetailActivity activity) {
        mInstance = new TripDetailActivityView(activity);
        return mInstance;
    }

    public TripDetailActivityView setOwnerAvatar(String url) {
        Glide.with(mActivity)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .animate(R.anim.anim_down_from_top)
                .bitmapTransform(new CropCircleTransformation(mActivity))
                .into(mActivity.imageCreatorAvatar);
        return this;
    }

    public TripDetailActivityView setTripCover(String url) {
        Glide.with(mActivity).load(url)
                .priority(Priority.IMMEDIATE)
                .animate(R.anim.anim_fade_in)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(mActivity.imageCover);
        return this;
    }

    public TripDetailActivityView setVehicle(int transfer) {
        UiUtils.showTransfer(transfer, mActivity.imageVehicle, mActivity.textVehicle);
        return this;
    }

    public TripDetailActivityView showTripDescription(String content) {
        UiUtils.showTextCenterInWebView(mActivity.webView, content);
        return this;
    }

    public TripDetailActivityView setTripName(String name) {
        mActivity.textTripName.setText(name);
        mActivity.textTripName.setSelection(mActivity.textTripName.getText().length());
        return this;
    }

    public TripDetailActivityView setOwnerName(String name) {
        mActivity.textCreatorName.setText(name);
        return this;
    }

    public TripDetailActivityView setViews(int numberOfViews) {
        mActivity.textViews.setText(String.valueOf(numberOfViews));
        return this;
    }

    public TripDetailActivityView setTime(long depart, long arrive) {
        mActivity.textTimeStart.setText(DatetimeUtils.getDate(depart));
        mActivity.textTimeEnd.setText(DatetimeUtils.getDate(arrive));
        return this;
    }

    public TripDetailActivityView switchMode(int roleType) {
        switch (roleType) {
            case RoleType.MEMBER:
            case RoleType.GUEST:
                mActivity.buttonMore.setVisibility(View.GONE);
                mActivity.textEdit.setVisibility(View.GONE);
                mActivity.textTripName.setEnabled(false);
                mActivity.buttonUpdate.setVisibility(View.GONE);
                break;
            case RoleType.ADMIN: {
                mActivity.buttonMore.setVisibility(View.VISIBLE);
                mActivity.textEdit.setVisibility(View.VISIBLE);
                mActivity.textTripName.setEnabled(true);
                mActivity.buttonUpdate.setVisibility(View.VISIBLE);
                mActivity.registerForContextMenu(mActivity.buttonMore);
                mActivity.registerForContextMenu(mActivity.imageCover);
                break;
            }
        }
        return this;
    }

    public TripDetailActivityView setFromTo(String from, String to) {
        mActivity.textFrom.setText(from);
        mActivity.textTo.setText(to);
        return this;
    }
}
