package io.yostajsc.sdk.designs.transformer;

import android.view.View;

/**
 * Created by nphau on 3/23/17.
 */

public class CubeOutTransformer extends ViewPagerBaseTransformer {
    @Override
    protected void onTransform(View view, float position) {
        view.setPivotX(position < 0f ? view.getWidth() : 0f);
        view.setPivotY(view.getHeight() * 0.5f);
        view.setRotationY(90f * position);
    }

    @Override
    public boolean isPagingEnabled() {
        return true;
    }
}