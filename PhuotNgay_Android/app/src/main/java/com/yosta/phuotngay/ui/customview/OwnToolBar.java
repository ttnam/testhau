package com.yosta.phuotngay.ui.customview;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yosta.phuotngay.R;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Phuc-Hau Nguyen on 12/2/2016.
 */

public class OwnToolBar extends LinearLayout {

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

        ButterKnife.bind(this, LayoutInflater.from(context).inflate(R.layout.layout_tool_bar, this, true));
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
}
