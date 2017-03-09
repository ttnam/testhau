package io.yostajsc.utils;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import io.yostajsc.designs.decorations.SpacesItemDecoration;
import io.yostajsc.designs.listeners.RecyclerItemClickListener;
import io.yostajsc.interfaces.CallBackWith;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

/**
 * Created by Phuc-Hau Nguyen on 3/8/2017.
 */

public class UiUtils {

    public static void onApplyRecyclerView(@NonNull RecyclerView view,
                                           @NonNull RecyclerView.Adapter adapter,
                                           RecyclerView.ItemAnimator animator,
                                           final CallBackWith<Integer> itemClick) {

        view.setHasFixedSize(true);

        view.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        view.setNestedScrollingEnabled(false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(),
                LinearLayoutManager.VERTICAL, false);
        view.setLayoutManager(layoutManager);

        if (animator != null)
            view.setItemAnimator(animator);

        view.setAdapter(adapter);

        if (itemClick != null) {
            view.addOnItemTouchListener(new RecyclerItemClickListener(view.getContext(),
                    new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            itemClick.run(position);
                        }
                    }));
        }
    }

    public static void onApplyWebViewSetting(WebView webView) {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDefaultTextEncodingName("utf-8");
        settings.setDefaultFontSize(14);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setAppCacheEnabled(false);
        settings.setBlockNetworkImage(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setGeolocationEnabled(false);
        settings.setNeedInitialFocus(false);
        settings.setSaveFormData(false);
    }
}
