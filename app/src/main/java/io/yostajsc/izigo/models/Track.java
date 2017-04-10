package io.yostajsc.izigo.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nphau on 4/9/17.
 */

public class Track implements Serializable {

    public List<String> geo;

    public Track() {
        geo = new ArrayList<>();
    }

    public Track(List<String> geo) {
        this.geo = geo;
    }

    public List<String> getGeo() {
        return geo;
    }

    public void setGeo(List<String> geo) {
        this.geo = geo;
    }

    public String getLastItemGeo() {
        return geo.get(geo.size() - 1);
    }
}
