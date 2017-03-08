package io.yostajsc.utils;

import java.io.File;

/**
 * Created by Phuc-Hau Nguyen on 3/3/2017.
 */

public class FileUtils {

    public static void delete(String pathName) {
        try {
            File localFile = new File(pathName);
            if (localFile.exists()) {
                localFile.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
