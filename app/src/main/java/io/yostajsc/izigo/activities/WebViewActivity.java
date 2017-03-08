package io.yostajsc.izigo.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.base.ActivityBehavior;
import io.yostajsc.utils.NetworkUtils;
import io.yostajsc.view.OwnToolBar;

public class WebViewActivity extends ActivityBehavior {

    @BindView(R.id.web_view)
    WebView webView;

    @BindView(R.id.layout)
    OwnToolBar mOwnToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);
        onApplyViews();
    }

    @Override
    public void onApplyViews() {
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });

        mOwnToolbar.setLeft(R.drawable.ic_vector_back_white, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        onClose();
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (NetworkUtils.isNetworkConnected(this))
            loadWebsite();
    }

    @Override
    protected void onInternetConnected() {
        super.onInternetConnected();
        loadWebsite();
    }

    private void onClose() {
        webView.clearCache(true);
        webView.clearHistory();
        webView.freeMemory();
    }

    private void loadWebsite() {
        webView.loadUrl("https://izigovn.firebaseapp.com");
    }
}
