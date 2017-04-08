package io.yostajsc.izigo.activities.trip;

import android.view.View;

import com.bumptech.glide.Glide;
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

    private TripDetailActivityView(TripDetailActivity tripDetailActivity) {
        this.mActivity = tripDetailActivity;
    }

    public static TripDetailActivityView inject(TripDetailActivity tripDetailActivity) {
        if (mInstance == null)
            mInstance = new TripDetailActivityView(tripDetailActivity);
        return mInstance;
    }

    public TripDetailActivityView setOwnerAvatar(String url) {
        // Avatar
        Glide.with(mActivity)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .bitmapTransform(new CropCircleTransformation(mActivity))
                .into(mActivity.imageCreatorAvatar);
        return this;
    }

    public TripDetailActivityView setTripCover(String url) {
        // Avatar
        Glide.with(mActivity).load(url)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(mActivity.imageCover);
        return this;
    }

    public TripDetailActivityView showTransfer(int transfer) {
        UiUtils.showTransfer(transfer, mActivity.imageTransfer);
        return this;
    }

    public TripDetailActivityView showTripDescription(String content) {
        UiUtils.showTextCenterInWebView(mActivity.webView, content);
        return this;
    }

    public TripDetailActivityView setTripName(String name) {
        mActivity.textTripName.setText(name);
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

    public TripDetailActivityView setComments(int numberOfComment) {
        mActivity.textNumberOfComments.setText(String.valueOf(numberOfComment));
        return this;
    }

    public TripDetailActivityView setActivities(int numberOfActivites) {
        mActivity.textNumberOfActivities.setText(mActivity.getResources().getQuantityString(R.plurals.comments,
                numberOfActivites, numberOfActivites));
        return this;
    }

    public TripDetailActivityView setTime(long depart, long arrive) {

        mActivity.textTime.setText(String.format("%s - %s",
                DatetimeUtils.getDate(depart),
                DatetimeUtils.getDate(arrive)
        ));

        return this;
    }

    public TripDetailActivityView setMembers(int numberOfMembers) {

        mActivity.textNumberOfMembers.setText(mActivity.getResources().getQuantityString(R.plurals.members,
                numberOfMembers, numberOfMembers));
        return this;
    }

    public TripDetailActivityView switchMode(int roleType, boolean isPublish) {
        switch (roleType) {
            case RoleType.MEMBER:
            case RoleType.GUEST:
                mActivity.buttonMore.setVisibility(View.INVISIBLE);
                mActivity.button.setImageResource(R.drawable.ic_add_user);
                mActivity.switchPublish.setVisibility(View.INVISIBLE);
                mActivity.textEdit.setVisibility(View.GONE);
                break;
            case RoleType.ADMIN: {
                mActivity.buttonMore.setVisibility(View.VISIBLE);
                mActivity.button.setImageResource(R.drawable.ic_marker);
                mActivity.switchPublish.setVisibility(View.VISIBLE);
                mActivity.textEdit.setVisibility(View.VISIBLE);
                mActivity.registerForContextMenu(mActivity.buttonMore);
                mActivity.registerForContextMenu(mActivity.imageCover);
                mActivity.switchPublish.setChecked(isPublish);
                break;
            }
        }
        return this;
    }

}
