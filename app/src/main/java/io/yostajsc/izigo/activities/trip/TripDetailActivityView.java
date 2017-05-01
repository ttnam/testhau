package io.yostajsc.izigo.activities.trip;

import android.content.res.Resources;
import android.net.Uri;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import io.yostajsc.constants.RoleType;
import io.yostajsc.core.interfaces.CallBack;
import io.yostajsc.core.interfaces.CallBackWith;
import io.yostajsc.core.utils.DatetimeUtils;
import io.yostajsc.izigo.R;
import io.yostajsc.sdk.model.IGCallback;
import io.yostajsc.sdk.api.IzigoSdk;
import io.yostajsc.firebase.FirebaseExecutor;
import io.yostajsc.utils.UiUtils;
import io.yostajsc.core.glide.CropCircleTransformation;

/**
 * Created by nphau on 4/7/17.
 */

public class TripDetailActivityView {

    private static final String ERROR_UN_BINDED = "You must bind first!";

    private static TripDetailActivity mActivity = null;
    private static TripDetailActivityView mInstance = null;
    private static Resources mResources = null;

    private TripDetailActivityView(TripDetailActivity activity) {
        mActivity = activity;
        mResources = mActivity.getResources();
    }

    public static synchronized TripDetailActivityView bind(TripDetailActivity activity) {
        mInstance = new TripDetailActivityView(activity);
        return mInstance;
    }

    public static void setOwnerAvatar(String url) {
        try {
            if (mInstance == null)
                throw new Exception(ERROR_UN_BINDED);
            Glide.with(mActivity)
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .animate(R.anim.anim_down_from_top)
                    .bitmapTransform(new CropCircleTransformation(mActivity))
                    .into(mActivity.imageCreatorAvatar);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setTripCover(String url) {
        try {
            if (mInstance == null)
                throw new Exception(ERROR_UN_BINDED);
            Glide.with(mActivity).load(url)
                    .priority(Priority.IMMEDIATE)
                    .animate(R.anim.anim_fade_in)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(mActivity.imageCover);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setVehicle(int transfer) {
        try {
            if (mInstance == null)
                throw new Exception(ERROR_UN_BINDED);
            UiUtils.showTransfer(transfer, mActivity.imageVehicle, mActivity.textVehicle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showTripDescription(String content) {
        try {
            if (mInstance == null)
                throw new Exception(ERROR_UN_BINDED);
            UiUtils.showTextCenterInWebView(mActivity.webView, content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setTripName(String name) {
        try {
            if (mInstance == null)
                throw new Exception(ERROR_UN_BINDED);
            mActivity.textTripName.setText(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setOwnerName(String name) {
        try {
            if (mInstance == null)
                throw new Exception(ERROR_UN_BINDED);
            mActivity.textCreatorName.setText(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setTime(long depart, long arrive) {
        try {
            if (mInstance == null)
                throw new Exception(ERROR_UN_BINDED);
            mActivity.textTimeStart.setText(DatetimeUtils.getDate(depart));
            mActivity.textTimeEnd.setText(DatetimeUtils.getDate(arrive));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void switchMode(int roleType) {
        try {
            if (mInstance == null)
                throw new Exception(ERROR_UN_BINDED);
            switch (roleType) {
                case RoleType.MEMBER:
                case RoleType.GUEST:
                    mActivity.textEdit.setVisibility(View.GONE);
                    mActivity.textPublish.setClickable(false);
                    break;
                case RoleType.ADMIN: {
                    mActivity.textEdit.setVisibility(View.VISIBLE);
                    mActivity.textPublish.setClickable(true);
                    mActivity.registerForContextMenu(mActivity.imageCover);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setFromTo(String from, String to) {
        try {
            if (mInstance == null)
                throw new Exception(ERROR_UN_BINDED);
            mActivity.textFrom.setText(from);
            mActivity.textTo.setText(to);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setPublishMode(boolean publishMode) {
        try {
            if (mInstance == null)
                throw new Exception(ERROR_UN_BINDED);
            mActivity.textPublish.setText(
                    publishMode ?
                            mResources.getString(R.string.str_published) :
                            mResources.getString(R.string.str_private));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void unbind() {
        mActivity = null;
        mResources = null;
        mInstance = null;
    }


    public static void changeTripCover(final String tripId, Uri uri, final CallBackWith<String> fail, final CallBack expired) {

        FirebaseExecutor.TripExecutor.changeCover(tripId, uri, new CallBackWith<Uri>() {
            @Override
            public void run(Uri uri) {

                IzigoSdk.TripExecutor.changeCover(tripId, uri.toString(), new IGCallback<Void, String>() {
                    @Override
                    public void onSuccessful(Void aVoid) {

                    }

                    @Override
                    public void onFail(String error) {
                        fail.run(error);
                    }

                    @Override
                    public void onExpired() {
                        expired.run();
                    }
                });

            }
        });
    }

    public static void publishTrip(String tripId,
                                   final boolean mIsPublic,
                                   final CallBackWith<String> fail,
                                   final CallBack expired) {
        IzigoSdk.TripExecutor.publishTrip(tripId, mIsPublic, new IGCallback<Void, String>() {
            @Override
            public void onSuccessful(Void aVoid) {
                setPublishMode(mIsPublic);
            }

            @Override
            public void onFail(String error) {
                fail.run(error);
            }

            @Override
            public void onExpired() {
                expired.run();
            }
        });
    }
}
