package com.star.common_utils.utils;

import android.os.Environment;

import java.io.File;

/**
 * @author xueshanshan
 * @date 2018/12/28
 */
public class StorageUtil {
    private static final String APP_DIR_ROOT = "common_util/";
    private static final String APP_DIR_PIC = "common_util/pic/";
    public static final String ROOT_DIR_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + APP_DIR_ROOT;
    public static final String ROOT_DIR_PIC = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + APP_DIR_PIC;

    private StorageUtil() {
    }
}
