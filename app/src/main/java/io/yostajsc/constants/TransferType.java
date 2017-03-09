package io.yostajsc.constants;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Phuc-Hau Nguyen on 12/3/2016.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({TransferType.WALK, TransferType.BICYCLE, TransferType.BUS})
public @interface TransferType {
    int WALK = 0;
    int BICYCLE = 2;
    int BUS = 3;
}
