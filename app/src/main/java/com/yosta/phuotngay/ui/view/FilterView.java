package com.yosta.phuotngay.ui.view;

import com.yosta.phuotngay.models.base.ViewBehavior;

/**
 * Created by Phuc-Hau Nguyen on 8/25/2016.
 */
public class FilterView extends ViewBehavior {

    private String mContent = null;

    public FilterView(String content) {
        this.mContent = content;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        this.mContent = content;
    }
}
