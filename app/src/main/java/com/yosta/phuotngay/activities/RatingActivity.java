package com.yosta.phuotngay.activities;

import android.os.Bundle;
import android.view.View;

import com.yosta.phuotngay.R;
import com.yosta.interfaces.ActivityBehavior;
import com.yosta.phuotngay.ui.OwnToolBar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RatingActivity extends ActivityBehavior {

    @BindView(R.id.own_toolbar)
    OwnToolBar mOwnToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        ButterKnife.bind(this);
        onApplyViews();
    }

    @Override
    public void onApplyViews() {

        mOwnToolbar
                .setTitle("Trip's rating")
                .setRight(R.drawable.ic_vector_close_black, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onBackPressed();
                    }
                });
    }
}
