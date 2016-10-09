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

public class ImageryActivity extends ActivityBehavior {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private ImageGalleryAdapter galleryAdapter;


    private static final String[] posters = {
            "https://cdn3.ivivu.com/2014/10/Du-lich-vung-tau-cam-nang-tu-a-den-z-iVIVU.com-1.jpg",
            "http://honviettour.vn/wp-content/uploads/2016/02/gm_151106_TC-7.jpg",
            "https://cdn3.ivivu.com/2014/10/Du-lich-vung-tau-cam-nang-tu-a-den-z-iVIVU.com-1.jpg",
            "http://honviettour.vn/wp-content/uploads/2016/02/gm_151106_TC-7.jpg",
            "https://cdn3.ivivu.com/2014/10/Du-lich-vung-tau-cam-nang-tu-a-den-z-iVIVU.com-1.jpg",
            "http://honviettour.vn/wp-content/uploads/2016/02/gm_151106_TC-7.jpg",
            "https://cdn3.ivivu.com/2014/10/Du-lich-vung-tau-cam-nang-tu-a-den-z-iVIVU.com-1.jpg",
            "http://honviettour.vn/wp-content/uploads/2016/02/gm_151106_TC-7.jpg",
            "https://cdn3.ivivu.com/2014/10/Du-lich-vung-tau-cam-nang-tu-a-den-z-iVIVU.com-1.jpg",
            "http://honviettour.vn/wp-content/uploads/2016/02/gm_151106_TC-7.jpg",
            "https://upload.wikimedia.org/wikipedia/commons/8/83/B%E1%BB%9D_bi%E1%BB%83n_V%C5%A9ng_T%C3%A0u.JPG"
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
