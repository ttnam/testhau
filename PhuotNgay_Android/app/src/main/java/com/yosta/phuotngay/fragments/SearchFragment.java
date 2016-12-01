package com.yosta.phuotngay.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yosta.materialspinner.MaterialSpinner;
import com.yosta.phuotngay.R;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchFragment extends Fragment {

    @BindView(R.id.spinner_vehicle)
    MaterialSpinner mSpinnerVehicle;

    private Activity mActivity;
    private List<String> mVehicles = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mActivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, rootView);

        onApplyData();

        return rootView;
    }

    private void onApplyData() {
        this.mVehicles = Arrays.asList(this.mActivity.getResources().getStringArray(R.array.arr_vehicle));
        this.mSpinnerVehicle.setItems(this.mVehicles);
    }
}
