package com.yosta.phuotngay.model.photo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phuc-Hau Nguyen on 10/12/2016.
 */

public class Photos implements Serializable {

    private List<Photo> photos = new ArrayList<>();

    public Photos() {
    }

    public Photos(List<Photo> photos) {
        this.photos = photos;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }
}
