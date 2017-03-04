package com.yosta.phuotngay.activities;

import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yosta.phuotngay.R;
import com.yosta.interfaces.ActivityBehavior;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebViewActivity extends ActivityBehavior {

    @BindView(R.id.web_view)
    WebView web_view;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && web_view.canGoBack()) {
            web_view.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onClose() {
        web_view.clearCache(true);
        web_view.clearHistory();
        web_view.freeMemory();
    }

    @Override
    public void onApplyComponents() {

        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);

        web_view.loadUrl("https://www.google.com");
        web_view.setBackgroundColor(Color.TRANSPARENT);
        web_view.getSettings().setJavaScriptEnabled(false);

        web_view.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getResources().getString(R.string.str_home));
        }
    }

}
