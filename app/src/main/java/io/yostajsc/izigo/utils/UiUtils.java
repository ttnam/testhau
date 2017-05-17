package io.yostajsc.izigo.utils;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import io.yostajsc.izigo.constants.TransferType;
import io.yostajsc.core.designs.listeners.RecyclerItemClickListener;
import io.yostajsc.core.interfaces.CallBackWith;
import io.yostajsc.izigo.R;

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

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                view.getContext(),
                LinearLayoutManager.VERTICAL,
                false);

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
        settings.setDefaultTextEncodingName("utf-8");
        settings.setDefaultFontSize(13);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setAppCacheEnabled(false);
        settings.setBlockNetworkImage(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setGeolocationEnabled(false);
        settings.setNeedInitialFocus(false);
        settings.setSaveFormData(false);
    }

    public static void showTransfer(int type, AppCompatImageView imageView) {
        switch (type) {
            case TransferType.BUS:
                imageView.setImageResource(R.drawable.ic_vector_bus);
                break;
            case TransferType.MOTORBIKE:
                imageView.setImageResource(R.drawable.ic_vector_motor_bike);
                break;
            case TransferType.WALK:
                imageView.setImageResource(R.drawable.ic_vector_walk);
                break;
        }
    }

    public static void showTextCenterInWebView(WebView webView, String text) {
        String prefix = "<html><body><p style=\"text-align: justify\">";
        String postfix = "</p></body></html>";
        if (!TextUtils.isEmpty(text)) {
            webView.loadData(prefix + text + postfix, "text/html; charset=utf-8", "utf-8");
            webView.setVisibility(View.VISIBLE);
        } else {
            webView.setVisibility(View.GONE);
        }
    }

}
