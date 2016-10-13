package com.yosta.phuotngay.models.views;

import com.yosta.phuotngay.models.base.ModelBehavior;

/**
 * Created by Phuc-Hau Nguyen on 8/25/2016.
 */
public class ImageGalleryView extends ModelBehavior {
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
