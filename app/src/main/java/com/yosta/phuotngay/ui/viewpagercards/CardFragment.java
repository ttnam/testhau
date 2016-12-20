package com.yosta.phuotngay.ui.viewpagercards;

import android.app.Activity;
import android.os.Bundle;
import android.os.Debug;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yosta.phuotngay.R;
import com.yosta.phuotngay.helpers.app.AppUtils;

import java.io.Console;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CardFragment extends Fragment {

    @BindView(R.id.card_view)
    CardView mCardView;

    private Activity mActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mActivity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_adapter, container, false);
        ButterKnife.bind(this, rootView);

        onApplyCardView();

        return rootView;
    }

    private void onApplyCardView() {
        float maxElevation = mCardView.getCardElevation() * CardAdapter.MAX_ELEVATION_FACTOR;
        this.mCardView.setMaxCardElevation(maxElevation);
    }

    public CardView getCardView() {
        return mCardView;
    }

}