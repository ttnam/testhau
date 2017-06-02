package io.yostajsc.izigo.usecase.trip;

import android.view.View;

import io.yostajsc.izigo.constants.RoleType;
import io.yostajsc.sdk.utils.DatetimeUtils;
import io.yostajsc.izigo.R;
import io.yostajsc.sdk.api.model.trip.IgTripStatus;
import io.yostajsc.izigo.ui.UiUtils;
import io.yostajsc.sdk.utils.GlideUtils;

/**
 * Created by nphau on 4/7/17.
 */

public class TripActivityHelper {

    private static final String ERROR_UNBOUND = "You must bind first!";

    private static TripActivity mActivity = null;
    private static TripActivityHelper mInstance = null;

    private TripActivityHelper(TripActivity activity) {
        mActivity = activity;
    }

    public static synchronized TripActivityHelper bind(TripActivity activity) {
        mInstance = new TripActivityHelper(activity);
        return mInstance;
    }

    static void setOwnerAvatar(String url) {
        try {
            if (mInstance == null)
                throw new Exception(ERROR_UNBOUND);
            GlideUtils.showAvatar(url, mActivity.imageCreatorAvatar);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void setTripCover(String url) {
        try {
            if (mInstance == null)
                throw new Exception(ERROR_UNBOUND);
            GlideUtils.showImage(url, mActivity.imageTripCover);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void setVehicle(int transfer) {
        try {
            if (mInstance == null)
                throw new Exception(ERROR_UNBOUND);
            UiUtils.showTransfer(transfer, mActivity.imageVehicle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void setTripStatus(int status) {
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

    static void showTripDescription(String content) {
        try {
            if (mInstance == null)
                throw new Exception(ERROR_UNBOUND);
            UiUtils.showTextCenterInWebView(mActivity.webView, content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void setTripName(String name) {
        try {
            if (mInstance == null)
                throw new Exception(ERROR_UNBOUND);
            mActivity.textTripName.setText(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void setTime(long depart, long arrive) {
        try {
            if (mInstance == null)
                throw new Exception(ERROR_UNBOUND);
            mActivity.textTimeStart.setText(DatetimeUtils.getDate(depart));
            mActivity.textTimeEnd.setText(DatetimeUtils.getDate(arrive));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void switchMode(int roleType) {
        try {
            if (mInstance == null)
                throw new Exception(ERROR_UNBOUND);
            switch (roleType) {
                case RoleType.MEMBER:
                    mActivity.buttonPublish.setVisibility(View.GONE);
                    mActivity.button.setVisibility(View.GONE);
                    break;
                case RoleType.GUEST:
                    mActivity.button.setText("Xin tham gia");
                    mActivity.buttonPublish.setVisibility(View.GONE);
                    mActivity.button.setVisibility(View.VISIBLE);
                    break;
                case RoleType.ADMIN: {
                    mActivity.buttonPublish.setVisibility(View.VISIBLE);
                    mActivity.button.setVisibility(View.GONE);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void setFromTo(String from, String to) {
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
        mInstance = null;
    }

    static void isPublish(boolean published) {
        try {
            if (mInstance == null)
                throw new Exception(ERROR_UNBOUND);
            mActivity.buttonPublish.setChecked(published);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
