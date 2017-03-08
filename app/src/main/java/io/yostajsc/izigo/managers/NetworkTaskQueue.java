package io.yostajsc.izigo.managers;

/**
 * Created by Phuc-Hau Nguyen on 3/8/2017.
 */

public interface NetworkTaskQueue<T> {

    boolean sendToServer(T t);

    T requestFromServer();

}
