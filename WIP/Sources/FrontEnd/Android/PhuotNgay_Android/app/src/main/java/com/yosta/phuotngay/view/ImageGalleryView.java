package com.yosta.phuotngay.view;

import com.yosta.phuotngay.view.base.ViewBehavior;

/**
 * Created by Phuc-Hau Nguyen on 8/25/2016.
 */
public class ImageGalleryView extends ViewBehavior {
    private String URL = null;

    public ImageGalleryView(String URL) {
        this.URL = URL;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }
}
