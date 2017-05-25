package io.yostajsc.sdk.gallery;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import io.yostajsc.sdk.R;

/**
 * Created by nphau on 5/25/17.
 */

public class LayoutSelector {

    private LayoutSelector() {

    }

    private static class Factory {

        private static Factory mInstance = null;
        private SparseIntArray sparseIntArray = null;

        private Factory() {
            this.sparseIntArray = new SparseIntArray();
            this.sparseIntArray.append(LayoutViewType.DELETE, R.layout.item_imagery_delete);
            this.sparseIntArray.append(LayoutViewType.NORMAL, R.layout.item_imagery_normal);
            this.sparseIntArray.append(LayoutViewType.MORE, R.layout.item_imagery_normal);
            this.sparseIntArray.append(LayoutViewType.SELECT, R.layout.item_imagery_normal);
        }

        public static Factory open() {
            if (mInstance == null)
                mInstance = new Factory();
            return mInstance;
        }

        /**
         * This factory is used to get layout from define style
         */
        @LayoutRes
        int getType(@LayoutViewType int type) {
            return this.sparseIntArray.get(type);
        }
    }

    public static class Selector {

        @LayoutViewType
        private int mType;

        public Selector selectorType(@LayoutViewType int type) {
            mType = type;
            return this;
        }

        private int getWidthInPixel(@NonNull Context context) {
            return context.getResources().getDisplayMetrics().widthPixels;
        }

        private int getHeigthInPixel(@NonNull Context context) {
            return context.getResources().getDisplayMetrics().heightPixels;
        }

        private View inflater(Context context) {
            int width = (int) (getWidthInPixel(context) / 3.0f);
            View itemView = LayoutInflater.from(context).inflate(Factory.open().getType(mType), null);
            itemView.setLayoutParams(new FrameLayout.LayoutParams(width, width));
            return itemView;
        }

        private View inflater(Context context, @LayoutRes int type) {
            int width = (int) (getWidthInPixel(context) / 3.0f);
            View itemView = LayoutInflater.from(context).inflate(type, null);
            itemView.setLayoutParams(new FrameLayout.LayoutParams(width, width));
            return itemView;
        }

        public ImageNormalViewHolder build(Context context) {

            ImageNormalViewHolder holder;
            View itemView = inflater(context);

            switch (mType) {
                case LayoutViewType.DELETE:
                    holder = new ImageDeleteViewHolder(itemView);
                    break;
                case LayoutViewType.NORMAL:
                    holder = new ImageNormalViewHolder(itemView);
                    break;
                case LayoutViewType.MORE:
                    holder = new ImageNormalViewHolder(itemView);
                    break;
                case LayoutViewType.SELECT:
                    holder = new ImageNormalViewHolder(itemView);
                    break;
                default:
                    holder = new ImageNormalViewHolder(itemView);
            }
            return holder;
        }

        public ImageNormalViewHolder makeDefault(Context context) {
            return new ImageNormalViewHolder(inflater(context, R.layout.item_imagery_normal));
        }
    }

}
