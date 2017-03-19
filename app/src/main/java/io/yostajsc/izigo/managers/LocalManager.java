package io.yostajsc.izigo.managers;

import io.yostajsc.core.interfaces.CallBackWith;

/**
 * Created by Phuc-Hau Nguyen on 3/8/2017.
 */

public class LocalManager {


    private static LocalManager mInstance = null;


    public static LocalManager inject() {
        if (mInstance == null)
            mInstance = new LocalManager();
        return mInstance;
    }

    public <T> void readFromDisk(final CallBackWith<T> callBack) {

    }

    public <T> boolean writeToDisk(T t) {
        return false;
    }

}
