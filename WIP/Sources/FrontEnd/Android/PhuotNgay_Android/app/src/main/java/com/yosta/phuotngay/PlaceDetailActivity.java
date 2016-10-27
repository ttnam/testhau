package com.yosta.phuotngay;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.yosta.phuotngay.R;
import com.yosta.phuotngay.adapter.NoteAdapter;
import com.yosta.phuotngay.dialog.DialogComment;
import com.yosta.phuotngay.helper.listeners.ItemClickSupport;
import com.yosta.phuotngay.interfaces.ActivityBehavior;
import com.yosta.phuotngay.adapter.ImageryAdapter;
import com.yosta.phuotngay.helper.viewholders.SpacesItemDecoration;
import com.yosta.phuotngay.model.note.Note;
import com.yosta.phuotngay.view.ImageryView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlaceDetailActivity extends ActivityBehavior {

    @BindView(R.id.textView_title)
    AppCompatTextView txtTitle;

    @BindView(R.id.web_view)
    WebView webView;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.rv_note)
    RecyclerView rvNote;

    private ImageryAdapter imageryAdapter = null;
    private NoteAdapter noteAdapter = null;

    @Override
    public void onApplyComponents() {
        super.onApplyComponents();
        setContentView(R.layout.activity_sight_detail);
        ButterKnife.bind(this);
        Window window = getWindow();
        if (window != null) {
            window.getAttributes().windowAnimations = R.style.AppTheme_AnimDialog_SlideLeftRight;
        }
        imageryAdapter = new ImageryAdapter(this);
        noteAdapter = new NoteAdapter(this);

        onInitializeWebView();
        onInitializeRecyclerView();
        onInitializeData();
    }

    private void onInitializeWebView() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(false);
        settings.setDefaultTextEncodingName("utf-8");
    }

    private void onInitializeData() {
        for (int i = 0; i < 20; i++) {
            imageryAdapter.addImage(new ImageryView("https://travel.com.vn/images/destination/Large/dg_150723_vung_tau.jpg"));
        }
        String prefix = "<html><body><p style=\"text-align: justify\">";
        String postfix = "</p></body></html>";
        String content = "Nếu bạn đã thấy sông trăng, hãy đến Nam Du vào ngày rằm để thấy biển trăng. Từ trên cao nhìn xuống, trăng tỏa sáng cả một vùng trời, đẹp và thơ mộng biết bao.";

        webView.loadData(prefix + content + postfix, "text/html; charset=utf-8", "utf-8");
        txtTitle.setText("Nam Du, thiên đường nơi cực Nam Tổ quốc");

        for (int i = 0; i < 20; i++) {
            noteAdapter.add(new Note());
        }
    }

    private void onInitializeRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new SpacesItemDecoration(2));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2,
                GridLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(imageryAdapter);

        rvNote.setHasFixedSize(true);
        rvNote.setItemAnimator(new DefaultItemAnimator());
        rvNote.addItemDecoration(new SpacesItemDecoration(2));
        rvNote.setNestedScrollingEnabled(false);
        rvNote.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        rvNote.setLayoutManager(new GridLayoutManager(this, 1,
                GridLayoutManager.HORIZONTAL, false));
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(rvNote);
        rvNote.setAdapter(noteAdapter);
        ItemClickSupport.addTo(rvNote).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        // do it
                    }
                }
        );

    }

    @OnClick(R.id.layout_comment)
    public void onShowCommentDialog() {
        DialogComment dialogComment = new DialogComment(this);
        dialogComment.show();
    }
}
