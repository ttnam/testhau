package io.yostajsc.izigo.activities;

import android.os.Bundle;
import android.view.View;

import butterknife.ButterKnife;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.base.ActivityBehavior;
import io.yostajsc.view.OwnToolBar;

import butterknife.BindView;

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
