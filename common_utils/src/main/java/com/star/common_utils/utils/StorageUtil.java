package com.star.common_utils.utils;

import android.os.Environment;

import java.io.File;

/**
 * @author xueshanshan
 * @date 2018/12/28
 */
public class StorageUtil {
    private static final String APP_ROUTE = Environment.getExternalStorageDirectory().getAbsolutePath();
    private static final String APP_DIR_ROOT = "common_util/";
    private static final String APP_DIR_PIC = "common_util/pic/";
    public static final String ROOT_DIR_PATH = APP_ROUTE + File.separator + APP_DIR_ROOT;
    public static final String ROOT_DIR_PIC = APP_ROUTE + File.separator + APP_DIR_PIC;

    private StorageUtil() {
    }
}
