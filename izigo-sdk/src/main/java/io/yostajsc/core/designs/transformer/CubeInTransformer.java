package io.yostajsc.core.designs.transformer;

import android.view.View;

/**
 * Created by nphau on 3/23/17.
 */

public class CubeInTransformer extends ViewPagerBaseTransformer {

    @Override
    protected void onTransform(View view, float position) {
        // Rotate the fragment on the left or right edge
        view.setPivotX(position > 0 ? 0 : view.getWidth());
        view.setPivotY(0);
        view.setRotationY(-90f * position);
    }

    @Override
    public boolean isPagingEnabled() {
        return true;
    }

}