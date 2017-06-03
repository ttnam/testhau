package io.yostajsc.izigo.customview.viewpagercards;


import android.support.v7.widget.CardView;

public interface CardAdapter {

    int MAX_ELEVATION_FACTOR = 5;

    float getBaseElevation();

    CardView getCardViewAt(int position);

    int getCount();
}
