package io.yostajsc.izigo.managers;

/**
 * Created by Phuc-Hau Nguyen on 3/8/2017.
 */

public class NetworkManager implements NetworkTaskQueue {

    @Override
    public boolean sendToServer(Object o) {
        return false;
    }

    @Override
    public Object requestFromServer() {
        return null;
    }
}
