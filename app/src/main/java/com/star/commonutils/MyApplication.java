package com.star.commonutils;

import android.app.Application;

import com.star.common_utils.utils.AppInfoUtil;
import com.star.common_utils.utils.LogUtil;

/**
 * @author xueshanshan
 * @date 2018/12/11
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        new LogUtil.Builder().debug(BuildConfig.DEBUG).globalTag("common_utils").init();
        AppInfoUtil.init(this);
    }
}
