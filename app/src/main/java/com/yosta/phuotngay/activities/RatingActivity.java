package com.yosta.phuotngay.activities;

import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.yosta.phuotngay.R;
import com.yosta.phuotngay.interfaces.ActivityBehavior;
import com.yosta.phuotngay.ui.OwnToolBar;
import com.yosta.phuotngay.ui.bottomsheet.BottomSheetDialog;

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
        onApplyComponents();
    }

    @Override
    public void onApplyComponents() {
        super.onApplyComponents();

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
