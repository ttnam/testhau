package com.yosta.phuotngay;

import android.content.Intent;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Toast;

import com.yosta.phuotngay.interfaces.ActivityBehavior;
import com.yosta.phuotngay.animations.YoYo;
import com.yosta.phuotngay.animations.attention.BounceAnimator;

public class SplashActivity extends ActivityBehavior implements
        GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

   /* @BindView(R.id.image)
    AppCompatImageView imageView;

    @BindView(R.id.textView)
    TextView textView;

    @BindView(R.id.layout_linear)
    LinearLayout layoutLinear;

    private DialogLogin loginDialog = null;*/

    private GestureDetectorCompat mDetector;

    @Override
    public void onApplyComponents() {
        super.onApplyComponents();
        setContentView(R.layout.view_item_place_header);

        Window window = getWindow();
        if (window != null) {
            window.getAttributes().windowAnimations = R.style.AppTheme_AnimDialog_SlideDownUp;
        }

        // ButterKnife.bind(this);
        // loginDialog = new DialogLogin(this);
        // IsUserVerified();
        mDetector = new GestureDetectorCompat(this, this);
        mDetector.setOnDoubleTapListener(this);
    }

    @Override
    public void onApplyEvents() {
        super.onApplyEvents();
        /*YoYo.with(new BounceInRightAnimator()).duration(900)
                .interpolate(new AccelerateDecelerateInterpolator())
                .playOn(imageView);*/
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        //Toast.makeText(this, "onSingleTapConfirmed", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        //Toast.makeText(this, "onDoubleTap", Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        //Toast.makeText(this, "onDoubleTapEvent", Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        //Toast.makeText(this, "OnDown", Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        //Toast.makeText(this, "onShowPress", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        YoYo.with(new BounceAnimator(75, 50)).duration(2000)
                .interpolate(new AccelerateDecelerateInterpolator())
                .playOn(findViewById(android.R.id.content));
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        //Toast.makeText(this, "onScroll " + e1.toString() + " " + e2.toString(), Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        //Toast.makeText(this, "onLongPress", Toast.LENGTH_SHORT).show();
    }

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        // try {
            /*if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH){
                return false;
            }
            // right to left swipe
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                Toast.makeText(this, "onLeftSwipe", Toast.LENGTH_SHORT).show();
            }
            // left to right swipe
            else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                Toast.makeText(this, "onRightSwipe", Toast.LENGTH_SHORT).show();
            }
            else if (e1.getX() < -45 && angle>= -135)
            // down
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }*/
        switch (getSlope(e1.getX(), e1.getY(), e2.getX(), e2.getY())) {
            case 1:
                startActivity(new Intent(this, MainActivity.class));
                return true;
            case 2:
                Toast.makeText(this, "onLeftSwipe", Toast.LENGTH_SHORT).show();
                return true;
            case 3:
                Toast.makeText(this, "onDownSwipe", Toast.LENGTH_SHORT).show();
                return true;
            case 4:
                Toast.makeText(this, "onRightSwipe", Toast.LENGTH_SHORT).show();
                return true;
        }
        //}
        return true;
    }

    private int getSlope(float x1, float y1, float x2, float y2) {
        Double angle = Math.toDegrees(Math.atan2(y1 - y2, x2 - x1));
        if (angle > 45 && angle <= 135)
            // top
            return 1;
        if (angle >= 135 && angle < 180 || angle < -135 && angle > -180)
            // left
            return 2;
        if (angle < -45 && angle >= -135)
            // down
            return 3;
        if (angle > -45 && angle <= 45)
            // right
            return 4;
        return 0;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
    /*
    @OnClick(R.id.button_ok)
    public void Login() {
        layoutLinear.setVisibility(View.VISIBLE);
        loginDialog.show();
    }

    @OnClick(R.id.button_no)
    public void Register() {
        DialogRegister dialogRegister = new DialogRegister(this);
        dialogRegister.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        loginDialog.dismiss();
    }

    @Override
    public void onApplyFont() {
        super.onApplyFont();
        UIUtils.setFont(this, UIUtils.FONT_LATO_ITALIC, textView);
    }

    private void IsUserVerified() {
        final AppConfig appConfig = (AppConfig) getApplication();
        String token = appConfig.IsUserLogin();
        if (token != null && !TextUtils.isEmpty(token)) {
            if (AppUtils.isNetworkConnected(this)) {
                PhuotNgayApiService.getInstance(this).ApiVerify(new Token(token), new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if (response.code() == 200) {
                            boolean IsVerified = response.body();
                            if (IsVerified) {
                                onMoveToMainActivity();
                            } else {
                                appConfig.userLogout();
                                Login();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        onMoveToMainActivity();
                    }
                });
            } else {
                onMoveToMainActivity();
            }
        } else {
            Login();
        }
    }

    private void onMoveToMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        onClose();
    }*/
}
