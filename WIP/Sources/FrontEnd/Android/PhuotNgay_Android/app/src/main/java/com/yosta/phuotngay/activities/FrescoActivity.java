package com.yosta.phuotngay.activities;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yosta.fresco.ImageViewer;
import com.yosta.phuotngay.R;
import com.yosta.phuotngay.activities.interfaces.ActivityBehavior;
import com.yosta.phuotngay.adapters.ImageGalleryAdapter;
import com.yosta.phuotngay.helpers.listeners.RecyclerItemClickListener;
import com.yosta.phuotngay.helpers.viewholders.DividerItemDecoration;
import com.yosta.phuotngay.models.views.ImageGalleryView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FrescoActivity extends ActivityBehavior {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private ImageGalleryAdapter galleryAdapter;


    private static final String[] posters = {
            "https://pp.vk.me/c630619/v630619423/4637a/vAOodrqPzQM.jpg",
            "https://pp.vk.me/c630619/v630619423/46395/71QKIPW6BWM.jpg",
            "https://pp.vk.me/c630619/v630619423/46383/GOTf1IvHKoc.jpg",
            "https://pp.vk.me/c630619/v630619423/4638c/i1URx2fWj20.jpg",
            "https://pp.vk.me/c630619/v630619423/4639e/BPoHv4xEikA.jpg",
            "https://pp.vk.me/c630619/v630619423/463a7/9EjA0oqA_yQ.jpg",
            "https://pp.vk.me/c630619/v630619423/463b0/VLPAZQJ0kuI.jpg",
            "https://pp.vk.me/c630619/v630619423/463b9/O3-hk8kIvdY.jpg",
            "https://pp.vk.me/c630619/v630619423/463c2/WgtvE0FQwVY.jpg"
    };

    @Override
    public void onApplyComponents() {
        super.onApplyComponents();
        setContentView(R.layout.activity_fresco);
        ButterKnife.bind(this);
        galleryAdapter = new ImageGalleryAdapter(this);

        // RecyclerView
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(3));
        recyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(galleryAdapter);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        showPicker(position);
                    }
                })
        );

    }

    @Override
    public void onApplyData() {
        super.onApplyData();
        for (String poster : posters) {
            galleryAdapter.addView(new ImageGalleryView(poster));
        }
    }

    private void showPicker(int startPosition) {
        new ImageViewer.Builder(this, posters)
                .setStartPosition(startPosition)
                .show();
    }
}
