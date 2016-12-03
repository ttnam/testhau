package com.yosta.yostatoolbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import butterknife.ButterKnife;

/**
 * Created by Phuc-Hau Nguyen on 12/2/2016.
 */

public class YosTaToolBar extends LinearLayout {


    public static final int TYPE_TITLE_CENTER = R.layout.layout_tool_bar;
    public static final int TYPE_TITLE_LEFT = R.layout.layout_tool_bar;
/*


    @BindView(R.id.text_view)
    TextView tvTitle;

    @BindView(R.id.button_left)
    AppCompatImageView btnLeft;

    @BindView(R.id.button_right)
    AppCompatImageView btnRight;
*/

    public YosTaToolBar(Context context) {
        super(context);
    }

    public YosTaToolBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.YosTaToolBar, 0, 0);
        try {
            int layout = array.getInteger(R.styleable.YosTaToolBar_type, 0);
            ButterKnife.bind(this, LayoutInflater
                    .from(context)
                    .inflate((layout == 0) ? TYPE_TITLE_CENTER : TYPE_TITLE_LEFT, this, true));
        } finally {
            array.recycle();
        }
    }

/*


    public YosTaToolBar(Context context, AttributeSet attrs, int type) {
        super(context, attrs);
        ButterKnife.bind(this, LayoutInflater.from(context).inflate(type, this, true));
    }*/

   /* public void setBinding(String title, int drawableLeft, int drawableRight,
                           OnClickListener leftListener,
                           OnClickListener rightListener) {

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
    }*/
}
