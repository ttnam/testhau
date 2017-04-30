package io.yostajsc.izigo.activities.trip;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.View;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.yostajsc.AppConfig;
import io.yostajsc.core.designs.listeners.RecyclerItemClickListener;
import io.yostajsc.core.interfaces.CoreActivity;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.adapters.ImageryOnlyAdapter;
import io.yostajsc.view.OwnToolBar;
import jp.wasabeef.recyclerview.animators.FadeInUpAnimator;

public class TripAlbumActivity extends CoreActivity {

    @BindView(R.id.own_toolbar)
    OwnToolBar ownToolBar;

    @BindView(R.id.recycler_view)
    RecyclerView rvAlbum;

    private ImageryOnlyAdapter albumAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        ButterKnife.bind(this);
        onApplyViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        onApplyData();
    }

    @Override
    public void onApplyViews() {
        super.onApplyViews();
        ownToolBar.setTitle(getString(R.string.str_photos))
                .setBinding(R.drawable.ic_vector_back_blue, R.drawable.ic_vector_add_image_theme,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onBackPressed();
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });

        this.albumAdapter = new ImageryOnlyAdapter(this);
        this.rvAlbum.setAdapter(this.albumAdapter);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(this.rvAlbum);
        this.rvAlbum.setHasFixedSize(true);
        this.rvAlbum.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        this.rvAlbum.setNestedScrollingEnabled(false);
        this.rvAlbum.setLayoutManager(new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false));
        this.rvAlbum.setItemAnimator(new FadeInUpAnimator());
        this.rvAlbum.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {


            }
        }));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onApplyData() {
        super.onApplyData();
        this.albumAdapter.replaceAll(AppConfig.igImages);
    }
}
