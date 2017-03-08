package io.yostajsc.izigo.managers;

/**
 * Created by Phuc-Hau Nguyen on 3/8/2017.
 */

public class LocalManager implements LocalTaskQueue {

    @Override
    public Object readFromDisk() {
        return null;
    }

    @Override
    public boolean writeToDisk(Object o) {
        return false;
    }
}
