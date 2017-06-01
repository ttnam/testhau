package io.yostajsc.izigo.usecase.map;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;

import io.yostajsc.izigo.R;
import io.yostajsc.izigo.usecase.map.utils.Info;
import io.yostajsc.izigo.usecase.map.utils.MapUtils;
import io.yostajsc.izigo.usecase.map.utils.RouteParserTask;

/**
 * Created by nphau on 5/28/17.
 */

public class SuggestionView extends CardView {

    public static final String TAG = SuggestionView.class.getSimpleName();

    private AppCompatImageView imageCover;
    private TextView
            textSuggestType,
            textSuggestName,
            textSuggestDistance,
            textSuggestDescription;

    private String mLink = "";

    public SuggestionView(Context context) {
        super(context);
        init(context);
    }

    public SuggestionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SuggestionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.item_map_suggestion, this);

        textSuggestName = (TextView) findViewById(R.id.text_suggest_name);
        textSuggestType = (TextView) findViewById(R.id.text_suggest_type);
        textSuggestDistance = (TextView) findViewById(R.id.text_suggest_distance);
        textSuggestDescription = (TextView) findViewById(R.id.text_suggest_description);

        imageCover = (AppCompatImageView) findViewById(R.id.image_suggest_cover);
    }

    public void bind(String name, String cover, String description,
                     String type, String link,
                     LatLng from, LatLng to) {
        try {

            mLink = link;

            Glide.with(getContext())
                    .load(cover)
                    .apply(new RequestOptions()
                            .dontTransform()
                            .priority(Priority.IMMEDIATE)
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                            .error(R.drawable.ic_profile_holder))
                    .into(imageCover);

            if (!TextUtils.isEmpty(name)) {
                textSuggestName.setText(name);
            }
            if (!TextUtils.isEmpty(type)) {
                textSuggestType.setText(type);
            }
            MapUtils.Map.direction(from, to, new RouteParserTask.OnDirectionCallBack() {
                @Override
                public void onSuccess(Info info, Polyline polyline) {
                    if (!TextUtils.isEmpty(info.strDistance)) {
                        textSuggestDistance.setText(info.strDistance);
                    }
                }
            });
            if (!TextUtils.isEmpty(description)) {
                textSuggestDescription.setText(description);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getLink() {
        return mLink;
    }
}
