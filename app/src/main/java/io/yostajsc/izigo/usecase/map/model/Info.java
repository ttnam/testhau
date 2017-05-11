package io.yostajsc.izigo.usecase.map.model;

/**
 * Created by nphau on 5/4/17.
 */

public class Info {

    public Long distance;
    public String strDistance;
    public Long duration;
    public String strDuration;

    public Info(Long distance, String strDistance, Long duration, String strDuration) {
        this.distance = distance;
        this.strDistance = strDistance;
        this.duration = duration;
        this.strDuration = strDuration;
    }
}
