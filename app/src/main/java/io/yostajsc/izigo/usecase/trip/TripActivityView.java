package io.yostajsc.izigo.usecase.trip;

import android.content.res.Resources;
import android.net.Uri;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import io.yostajsc.izigo.constants.RoleType;
import io.yostajsc.core.interfaces.CallBack;
import io.yostajsc.core.interfaces.CallBackWith;
import io.yostajsc.core.utils.DatetimeUtils;
import io.yostajsc.izigo.R;
import io.yostajsc.sdk.model.IgCallback;
import io.yostajsc.sdk.api.IzigoSdk;
import io.yostajsc.izigo.usecase.firebase.FirebaseExecutor;
import io.yostajsc.sdk.model.trip.IgTripStatus;
import io.yostajsc.izigo.utils.UiUtils;
import io.yostajsc.core.glide.CropCircleTransformation;

/**
 * Created by nphau on 4/7/17.
 */

public class TripActivityView {

    private static final String ERROR_UNBOUND = "You must bind first!";

    private static TripActivity mActivity = null;
    private static TripActivityView mInstance = null;
    private static Resources mResources = null;

    private TripActivityView(TripActivity activity) {
        mActivity = activity;
        mResources = mActivity.getResources();
    }

    public static synchronized TripActivityView bind(TripActivity activity) {
        mInstance = new TripActivityView(activity);
        return mInstance;
    }

    public static void setOwnerAvatar(String url) {
        try {
            if (mInstance == null)
                throw new Exception(ERROR_UNBOUND);
            Glide.with(mActivity)
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .animate(R.anim.anim_fade_in)
                    .error(R.drawable.ic_profile_holder)
                    .bitmapTransform(new CropCircleTransformation(mActivity))
                    .into(mActivity.imageCreatorAvatar);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setTripCover(String url) {
        try {
            if (mInstance == null)
                throw new Exception(ERROR_UNBOUND);
            Glide.with(mActivity).load(url)
                    .priority(Priority.IMMEDIATE)
                    .animate(R.anim.anim_fade_in)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(mActivity.imageTripCover);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setVehicle(int transfer) {
        try {
            if (mInstance == null)
                throw new Exception(ERROR_UNBOUND);
            UiUtils.showTransfer(transfer, mActivity.imageVehicle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setTripStatus(int status) {
        try {
            if (mInstance == null)
                throw new Exception(ERROR_UNBOUND);
            if (status == IgTripStatus.PREPARED) {
                mActivity.textStatus.setText("Dự định");
                mActivity.textStatus.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.ic_style_rect_round_corners_light_blue_none));
            } else if (status == IgTripStatus.ONGOING) {
                mActivity.textStatus.setText("Đang đi");
                mActivity.textStatus.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.ic_style_rect_round_corners_light_green));
            } else if (status == IgTripStatus.FINISHED) {
                mActivity.textStatus.setText("Kết thúc");
                mActivity.textStatus.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.ic_style_rect_round_corners_light_red_none));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showTripDescription(String content) {
        try {
            if (mInstance == null)
                throw new Exception(ERROR_UNBOUND);
            UiUtils.showTextCenterInWebView(mActivity.webView, content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setTripName(String name) {
        try {
            if (mInstance == null)
                throw new Exception(ERROR_UNBOUND);
            mActivity.textTripName.setText(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
/*
    public static void setOwnerName(String name) {
        try {
            if (mInstance == null)
                throw new Exception(ERROR_UNBOUND);
            mActivity.textCreatorName.setText(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    public static void setTime(long depart, long arrive) {
        try {
            if (mInstance == null)
                throw new Exception(ERROR_UNBOUND);
            mActivity.textTimeStart.setText(DatetimeUtils.getDate(depart));
            mActivity.textTimeEnd.setText(DatetimeUtils.getDate(arrive));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void switchMode(int roleType) {
        try {
            if (mInstance == null)
                throw new Exception(ERROR_UNBOUND);
            switch (roleType) {
                case RoleType.MEMBER:
                    /*mActivity.textEdit.setVisibility(View.GONE);
                    mActivity.switchPublish.setVisibility(View.GONE);
                    mActivity.buttonAction.setBackgroundDrawable(mActivity.getResources().
                            getDrawable(R.drawable.ic_style_button_round_none_accent));
                    mActivity.buttonAction.setText("Danh sách thành viên");
                   */
                    mActivity.button.setVisibility(View.GONE);
                    break;
                case RoleType.GUEST:
                    /*mActivity.textEdit.setVisibility(View.GONE);
                    mActivity.switchPublish.setVisibility(View.GONE);
                    */
                    mActivity.button.setText("Xin tham gia");
                    mActivity.button.setVisibility(View.VISIBLE);
                    break;
                case RoleType.ADMIN: {
                    /*mActivity.textEdit.setVisibility(View.VISIBLE);
                    mActivity.switchPublish.setVisibility(View.VISIBLE);
                    mActivity.registerForContextMenu(mActivity.imageCover);
                    mActivity.buttonAction.setVisibility(View.VISIBLE);
                    mActivity.buttonAction.setBackgroundDrawable(mActivity.getResources().
                            getDrawable(R.drawable.ic_style_button_round_none_accent));
                    mActivity.buttonAction.setText("Danh sách thành viên");
                  */
                    mActivity.button.setVisibility(View.GONE);
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
                throw new Exception(ERROR_UNBOUND);
            mActivity.textFrom.setText(from);
            mActivity.textTo.setText(to);
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

                IzigoSdk.TripExecutor.changeCover(tripId, uri.toString(), new IgCallback<Void, String>() {
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
        IzigoSdk.TripExecutor.publishTrip(tripId, mIsPublic, new IgCallback<Void, String>() {
            @Override
            public void onSuccessful(Void aVoid) {
                // setPublishMode(mIsPublic);
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

    public static void isPublish(boolean published) {
        try {
            if (mInstance == null)
                throw new Exception(ERROR_UNBOUND);
            // mActivity.switchPublish.setChecked(published);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
