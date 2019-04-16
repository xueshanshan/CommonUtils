package com.star.commonutils;

import android.app.Application;

import com.star.common_utils.utils.AppInfoUtil;
import com.star.common_utils.utils.AppUtil;
import com.star.common_utils.utils.FileUtil;
import com.star.common_utils.utils.LogUtil;
import com.star.common_utils.utils.StorageUtil;

/**
 * @author xueshanshan
 * @date 2018/12/11
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (!AppUtil.isCurProcess(this)) {
            return;
        }
        new LogUtil.Builder().debug(BuildConfig.DEBUG).globalTag("common_utils").init();
        AppInfoUtil.init(this);
        FileUtil.makeDirIfNotExist(StorageUtil.ROOT_DIR_PATH);
        FileUtil.makeDirIfNotExist(StorageUtil.ROOT_DIR_PIC);
    }
}
