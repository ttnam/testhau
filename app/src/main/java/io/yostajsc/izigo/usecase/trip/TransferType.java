package io.yostajsc.izigo.usecase.trip;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Phuc-Hau Nguyen on 12/3/2016.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({TransferType.WALK, TransferType.MOTORBIKE, TransferType.BUS})
public @interface TransferType {
    int WALK = 0;
    int MOTORBIKE = 2;
    int BUS = 3;
}
