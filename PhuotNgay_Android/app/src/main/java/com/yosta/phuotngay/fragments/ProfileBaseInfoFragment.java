package com.yosta.phuotngay.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yosta.materialspinner.MaterialSpinner;
import com.yosta.phuotngay.R;
import com.yosta.phuotngay.animations.YoYo;
import com.yosta.phuotngay.animations.attention.FlashAnimator;
import com.yosta.phuotngay.animations.attention.ShakeAnimator;
import com.yosta.phuotngay.helpers.app.AppUtils;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileBaseInfoFragment extends Fragment {

    @BindView(R.id.spinner_gender)
    MaterialSpinner spinnerGender;

    private Activity mActivity = null;
    private List<String> mGender = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mActivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile_base_info, container, false);
        ButterKnife.bind(this, rootView);

        onApplyComponents();
        onApplyData();

        return rootView;
    }

    private void onApplyComponents() {

    }

    private void onApplyData() {
        this.mGender = Arrays.asList(this.mActivity.getResources().getStringArray(R.array.arr_gender));
        this.spinnerGender.setItems(this.mGender);
    }
}
