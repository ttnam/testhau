package com.yosta.phuotngay.activities;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yosta.phuotngay.R;
import com.yosta.phuotngay.activities.interfaces.ActivityBehavior;
import com.yosta.phuotngay.adapters.CommentAdapter;
import com.yosta.phuotngay.helpers.AppUtils;
import com.yosta.phuotngay.helpers.viewholders.DividerItemDecoration;
import com.yosta.phuotngay.models.Comment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SightDetailActivity extends ActivityBehavior {

    @BindView(R.id.textView)
    AppCompatTextView txtFollow;

    @BindView(R.id.textView_description)
    AppCompatTextView txtLocation;

    @BindView(R.id.textView_title)
    AppCompatTextView txtTitle;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private CommentAdapter commentAdapter = null;

    @Override
    public void onApplyComponents() {
        super.onApplyComponents();
        setContentView(R.layout.activity_sight_detail);
        ButterKnife.bind(this);

        commentAdapter = new CommentAdapter(this);

        onInitializeRecyclerView();
        onInitializeData();

    }


    private void onInitializeData() {
        for (int i = 0; i < 2; i++) {
            commentAdapter.addComment(new Comment());
        }
    }

    private void onInitializeRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(8));
        recyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(commentAdapter);
    }

    @Override
    public void onApplyFont() {
        super.onApplyFont();
        AppUtils.setFont(this, "fonts/Lato-Heavy.ttf", txtTitle);
    }

    @OnClick(R.id.img_back)
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
