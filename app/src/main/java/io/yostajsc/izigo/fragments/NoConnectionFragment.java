package io.yostajsc.izigo.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.yostajsc.designs.animations.YoYo;
import io.yostajsc.designs.animations.attention.FlashAnimator;
import io.yostajsc.designs.animations.attention.ShakeAnimator;
import io.yostajsc.izigo.R;
import io.yostajsc.utils.AppUtils;

public class NoConnectionFragment extends Fragment {

    @BindView(R.id.text_view)
    TextView textView;

    @BindView(R.id.image_view)
    ImageView image;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_no_connection, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick(R.id.image_view)
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
            Toast.makeText(getContext(), getString(R.string.error_message_no_internet), Toast.LENGTH_SHORT).show();
        }
    }
}
