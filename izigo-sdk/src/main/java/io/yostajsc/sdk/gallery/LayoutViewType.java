package io.yostajsc.sdk.gallery;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by nphau on 5/25/17.
 */

@Retention(RetentionPolicy.SOURCE)
@IntDef({LayoutViewType.NORMAL, LayoutViewType.DELETE,
        LayoutViewType.MORE, LayoutViewType.SELECT})
public @interface LayoutViewType {
    int NORMAL = 1;
    int DELETE = 2;
    int MORE = 3;
    int SELECT = 4;
}
