package io.yostajsc.izigo.managers;

/**
 * Created by Phuc-Hau Nguyen on 3/8/2017.
 */

public interface LocalTaskQueue<T> {

    T readFromDisk();

    boolean writeToDisk(T t);

}
