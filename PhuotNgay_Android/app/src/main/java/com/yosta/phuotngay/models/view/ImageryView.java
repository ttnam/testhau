package com.yosta.phuotngay.models.view;

import com.yosta.phuotngay.models.base.ViewBehavior;

/**
 * Created by Phuc-Hau Nguyen on 8/25/2016.
 */
public class ImageryView extends ViewBehavior {

    private String URL = null;

    public ImageryView(String URL) {
        this.URL = URL;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }
}
