package com.yosta.phuotngay.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yosta.phuotngay.R;
import com.yosta.phuotngay.animations.YoYo;
import com.yosta.phuotngay.animations.attention.BounceAnimator;
import com.yosta.phuotngay.animations.attention.FlashAnimator;
import com.yosta.phuotngay.animations.attention.ShakeAnimator;
import com.yosta.phuotngay.helpers.globalapp.AppUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NoConnectionFragment extends Fragment {

    @BindView(R.id.textView)
    TextView textView;

    @BindView(R.id.image)
    ImageView image;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.view_fragment_no_connection, container, false);
        ButterKnife.bind(this, rootView);
        onApplyAnimation();
        return rootView;
    }

    @OnClick(R.id.image)
    public void onClick() {
        if (!AppUtils.isNetworkConnected(getContext())) {
            YoYo.with(new ShakeAnimator())
                    .duration(600)
                    .interpolate(new AccelerateDecelerateInterpolator())
                    .playOn(image);
            YoYo.with(new FlashAnimator())
                    .duration(600)
                    .interpolate(new AccelerateDecelerateInterpolator())
                    .playOn(textView);
            Toast.makeText(getContext(), getString(R.string.message_no_internet), Toast.LENGTH_SHORT).show();
        }
    }

    void onApplyAnimation() {
        YoYo.with(new BounceAnimator())
                .duration(5000)
                .interpolate(new AccelerateDecelerateInterpolator())
                .playOn(image);
    }
}
