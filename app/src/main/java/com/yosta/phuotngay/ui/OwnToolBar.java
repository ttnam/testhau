package com.yosta.phuotngay.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yosta.phuotngay.R;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Phuc-Hau Nguyen on 12/2/2016.
 */

public class OwnToolBar extends RelativeLayout {

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
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.OwnToolBar, 0, 0);
        try {
            int layout = array.getInteger(R.styleable.OwnToolBar_layoutType, 0);
            ButterKnife.bind(this, LayoutInflater.from(context).inflate((layout == 0) ? TYPE_TITLE_CENTER : TYPE_TITLE_LEFT, this, true));

            getRootView().setBackgroundColor(array.getColor(
                    R.styleable.OwnToolBar_layoutBackground,
                    getResources().getColor(android.R.color.white)));

            tvTitle.setTextColor(array.getColor(
                    R.styleable.OwnToolBar_layoutTitleColor,
                    getResources().getColor(android.R.color.white)));

        } finally {
            array.recycle();
        }
    }

    public OwnToolBar setTitle(String title) {
        this.tvTitle.setText(title);
        setBinding(Integer.MIN_VALUE, Integer.MIN_VALUE, null, null);
        return this;
    }

    public OwnToolBar setTitleColor(@NonNull @ColorRes int color) {
        tvTitle.setTextColor(getResources().getColor(color));
        return this;
    }

    private void setOnLeftClickListener(View.OnClickListener listener) {
        this.btnLeft.setOnClickListener(listener);
    }

    private void setOnRightClickListener(View.OnClickListener listener) {
        this.btnRight.setOnClickListener(listener);
    }

    private void setDrawableLeft(int icon) {
        if (icon == Integer.MIN_VALUE) {
            this.btnLeft.setVisibility(GONE);
        } else {
            this.btnLeft.setVisibility(VISIBLE);
            this.btnLeft.setImageResource(icon);
        }
    }

    private void setDrawableRight(int icon) {
        if (icon == Integer.MIN_VALUE) {
            this.btnRight.setVisibility(GONE);
        } else {
            this.btnRight.setVisibility(VISIBLE);
            this.btnRight.setImageResource(icon);
        }
    }

    public OwnToolBar setBinding(String title, int drawableLeft, int drawableRight) {
        this.tvTitle.setText(title);
        setBinding(drawableLeft, drawableRight, null, null);
        return this;
    }

    public OwnToolBar setOwnBackgroud(int id) {
        getRootView().setBackgroundColor(getResources().getColor(id));
        return this;
    }

    public void setBinding(String title, int drawableLeft, int drawableRight,
                           View.OnClickListener leftListener, View.OnClickListener rightListener) {

        this.tvTitle.setText(title);
        setBinding(drawableLeft, drawableRight, leftListener, rightListener);
    }

    public void setBinding(int drawableLeft, int drawableRight,
                           View.OnClickListener leftListener, View.OnClickListener rightListener) {

        setOnLeftClickListener(leftListener);
        setOnRightClickListener(rightListener);
        setDrawableLeft(drawableLeft);
        setDrawableRight(drawableRight);
    }

    public OwnToolBar setLeft(int icon, @NotNull View.OnClickListener listener) {
        setDrawableLeft(icon);
        setOnLeftClickListener(listener);

        setDrawableRight(Integer.MIN_VALUE);
        setOnRightClickListener(null);
        return this;
    }

    public OwnToolBar setRight(int icon, @NotNull View.OnClickListener listener) {
        setDrawableRight(icon);
        setOnRightClickListener(listener);

        setDrawableLeft(Integer.MIN_VALUE);
        setOnLeftClickListener(null);
        return this;
    }
}
