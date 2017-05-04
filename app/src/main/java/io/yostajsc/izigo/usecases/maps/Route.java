package io.yostajsc.izigo.usecases.maps;

import java.util.HashMap;
import java.util.List;

/**
 * Created by nphau on 5/4/17.
 */

public class Route {

    public Info info;
    public List<HashMap<String, String>> route;

    public Route(Info info, List<HashMap<String, String>> route) {
        this.info = info;
        this.route = route;
    }

}