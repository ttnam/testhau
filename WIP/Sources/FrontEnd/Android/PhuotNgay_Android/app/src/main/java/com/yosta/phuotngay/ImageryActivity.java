package com.yosta.phuotngay;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yosta.fresco.ImageViewer;
import com.yosta.phuotngay.R;
import com.yosta.phuotngay.interfaces.ActivityBehavior;
import com.yosta.phuotngay.adapter.ImageryAdapter;
import com.yosta.phuotngay.helper.listeners.RecyclerItemClickListener;
import com.yosta.phuotngay.view.ImageryView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageryActivity extends ActivityBehavior {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private ImageryAdapter galleryAdapter;


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
        setContentView(R.layout.activity_imagery);
        ButterKnife.bind(this);
        galleryAdapter = new ImageryAdapter(this);


        // RecyclerView
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        // TODO
        // recyclerView.addItemDecoration(new DividerItemDecoration(3));
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
            galleryAdapter.addImage(new ImageryView(poster));
        }
    }

    private void showPicker(int startPosition) {
        new ImageViewer.Builder(this, posters)
                .setStartPosition(startPosition)
                .show();
    }
}
