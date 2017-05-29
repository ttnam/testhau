package io.yostajsc.izigo.usecase.webview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.yostajsc.izigo.R;
import io.yostajsc.izigo.usecase.OwnCoreActivity;
import io.yostajsc.izigo.usecase.view.OwnToolBar;

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
    protected void onStart() {
        super.onStart();
        String url = getIntent().getStringExtra("WEB_LINK");
        if (!TextUtils.isEmpty(url)) {
            webView.loadUrl(url);
            mOwnToolbar.setTitle(url);
        }
    }

    @Override
    public void onApplyViews() {
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.getSettings().setSupportZoom(false);
        webView.getSettings().setJavaScriptEnabled(false);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
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

    private void onClose() {
        webView.clearCache(true);
        webView.clearHistory();
        webView.freeMemory();
    }
}
