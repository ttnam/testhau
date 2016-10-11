package com.yosta.phuotngay.activities;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.yosta.phuotngay.R;
import com.yosta.phuotngay.activities.dialogs.DialogComment;
import com.yosta.phuotngay.activities.interfaces.ActivityBehavior;
import com.yosta.phuotngay.adapters.CommentAdapter;
import com.yosta.phuotngay.helpers.AppUtils;
import com.yosta.phuotngay.helpers.viewholders.DividerItemDecoration;
import com.yosta.phuotngay.models.Comment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SightDetailActivity extends ActivityBehavior {
    /*
        @BindView(R.id.textView)
        AppCompatTextView txtFollow;

        @BindView(R.id.textView_description)
        AppCompatTextView txtLocation;

        */
    @BindView(R.id.textView_title)
    AppCompatTextView txtTitle;

    @BindView(R.id.web_view)
    WebView webView;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private CommentAdapter commentAdapter = null;

    @Override
    public void onApplyComponents() {
        super.onApplyComponents();
        setContentView(R.layout.activity_sight_detail);
        ButterKnife.bind(this);

        commentAdapter = new CommentAdapter(this);

        onInitializeWebView();
        onInitializeRecyclerView();
        onInitializeData();
    }

    @Override
    public void onApplyFont() {
        super.onApplyFont();
        //AppUtils.setFont(this, "fonts/Lato-Heavy.ttf", txtTitle);
    }

    private void onInitializeWebView() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(false);
        settings.setDefaultTextEncodingName("utf-8");
    }

    private void onInitializeData() {
        for (int i = 0; i < 2; i++) {
            commentAdapter.addComment(new Comment());
        }
        String prefix = "<html><body><p style=\"text-align: justify\">";
        String postfix = "</p></body></html>";
        String content = "Nếu bạn đã thấy sông trăng, hãy đến Nam Du vào ngày rằm để thấy biển trăng. Từ trên cao nhìn xuống, trăng tỏa sáng cả một vùng trời, đẹp và thơ mộng biết bao.";

        webView.loadData(prefix + content + postfix, "text/html; charset=utf-8", "utf-8");
        txtTitle.setText("Nam Du, thiên đường nơi cực Nam Tổ quốc");
    }

    private void onInitializeRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(8));
        recyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(commentAdapter);
    }

    @OnClick(R.id.layout_comment)
    public void onShowCommentDialog() {
        DialogComment dialogComment = new DialogComment(this);
        dialogComment.show();
    }
}
