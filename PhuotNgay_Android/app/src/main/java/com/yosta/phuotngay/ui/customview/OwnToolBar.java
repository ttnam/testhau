package com.yosta.phuotngay.ui.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yosta.phuotngay.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Phuc-Hau Nguyen on 12/2/2016.
 */

public class OwnToolBar extends LinearLayout {

    public static final int TYPE_TITLE_CENTER = R.layout.layout_tool_bar;
    public static final int TYPE_TITLE_LEFT = R.layout.layout_tool_bar_lr;

    @BindView(R.id.text_view)
    TextView tvTitle;

    @BindView(R.id.button_left)
    AppCompatImageView btnLeft;

    @BindView(R.id.button_right)
    AppCompatImageView btnRight;

    public OwnToolBar(Context context) {
        super(context);
    }

    public OwnToolBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.OwnToolBar, 0, 0);

        try {
            int layout = array.getInteger(R.styleable.OwnToolBar_layoutType, 0);
            ButterKnife.bind(this, LayoutInflater
                    .from(context)
                    .inflate((layout == 0) ? TYPE_TITLE_CENTER : TYPE_TITLE_LEFT, this, true));

        } finally {
            array.recycle();
        }
    }

    public OwnToolBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setBinding(String title, int drawableLeft, int drawableRight,
                           View.OnClickListener leftListener,
                           View.OnClickListener rightListener) {

        this.tvTitle.setText(title);
        this.btnLeft.setOnClickListener(leftListener);
        this.btnRight.setOnClickListener(rightListener);

        if (drawableLeft == Integer.MIN_VALUE) {
            this.btnLeft.setVisibility(GONE);
        } else {
            this.btnLeft.setVisibility(VISIBLE);
            this.btnLeft.setImageResource(drawableLeft);
        }

        if (drawableRight == Integer.MIN_VALUE) {
            this.btnRight.setVisibility(GONE);
        } else {
            this.btnRight.setVisibility(VISIBLE);
            this.btnRight.setImageResource(drawableRight);
        }
    }

    public AppCompatImageView getLeftButton() {
        return this.btnLeft;
    }
    public AppCompatImageView getRightButton() {
        return this.btnRight;
    }
}
