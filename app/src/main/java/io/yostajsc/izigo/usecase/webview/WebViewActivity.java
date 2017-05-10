package io.yostajsc.izigo.usecase.webview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.yostajsc.core.utils.NetworkUtils;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.activities.OwnCoreActivity;
import io.yostajsc.izigo.ui.OwnToolBar;

public class WebViewActivity extends OwnCoreActivity {

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

        mOwnToolbar.setOnlyLeft(R.drawable.ic_vector_back_white, new View.OnClickListener() {
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
    public void onInternetConnected() {
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
