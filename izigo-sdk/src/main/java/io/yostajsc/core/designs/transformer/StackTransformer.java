package io.yostajsc.core.designs.transformer;

import android.view.View;

/**
 * Created by nphau on 3/23/17.
 */

public class StackTransformer extends ViewPagerBaseTransformer {

    @Override
    protected void onTransform(View view, float position) {
        view.setTranslationX(position < 0 ? 0f : -view.getWidth() * position);
    }

}