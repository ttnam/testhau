package io.yostajsc.izigo.usecase.view.viewholder;

import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import io.yostajsc.izigo.R;

/**
 * Created by Phuc-Hau Nguyen on 8/25/2016.
 */
public class ImageryMoreViewHolder extends ImageryViewHolder {

    @BindView(R.id.text_view)
    TextView textView;

    public ImageryMoreViewHolder(View itemView) {
        super(itemView);
    }

    public void bind(String url, int left) {
        bind(url);
        if (left > 0)
            textView.setText(String.format("+%d", left));
    }
}
