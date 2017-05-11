package io.yostajsc.core.designs.transformer;

import android.view.View;

/**
 * Created by nphau on 3/23/17.
 */

public class AccordionTransformer extends ViewPagerBaseTransformer {

    @Override
    protected void onTransform(View view, float position) {
        view.setPivotX(position < 0 ? 0 : view.getWidth());
        view.setScaleX(position < 0 ? 1f + position : 1f - position);
    }

}