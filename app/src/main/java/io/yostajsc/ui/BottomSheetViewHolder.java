package io.yostajsc.ui;

import android.support.annotation.DrawableRes;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.yostajsc.izigo.R;

/**
 * Created by Phuc-Hau Nguyen on 12/14/2016.
 */
public class BottomSheetViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.image_view)
    AppCompatImageView imageView;

    public BottomSheetViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(@DrawableRes int drawable) {
        imageView.setImageResource(drawable);
    }
}
