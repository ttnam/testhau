package io.yostajsc.core.designs.transformer;

import android.view.View;

public class DrawerTransformer extends ViewPagerBaseTransformer {

    private static final float MIN_SCALE = 0.75f;

    @Override
    protected void onTransform(View view, float position) {
        if (position <= 0) {
            view.setTranslationX(0);
        } else if (position > 0 && position <= 1) {
            view.setTranslationX(-view.getWidth() / 2 * position);
        }
    }

}