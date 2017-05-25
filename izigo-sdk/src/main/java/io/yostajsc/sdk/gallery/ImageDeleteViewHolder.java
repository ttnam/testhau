package io.yostajsc.sdk.gallery;

import android.support.v7.widget.AppCompatImageView;
import android.view.View;

import io.yostajsc.sdk.R;

/**
 * Created by nphau on 5/25/17.
 */

public class ImageDeleteViewHolder extends ImageNormalViewHolder {

    private AppCompatImageView imageButton;

    public ImageDeleteViewHolder(View itemView) {
        super(itemView);
        imageButton = (AppCompatImageView) itemView.findViewById(R.id.button);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickListener != null)
                    mOnClickListener.onClick();
            }
        });
    }

    @Override
    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

}
