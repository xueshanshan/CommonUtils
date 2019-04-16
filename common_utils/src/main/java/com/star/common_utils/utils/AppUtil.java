package com.star.common_utils.utils;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.util.TypedValue;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xueshanshan
 * @date 2018/12/10
 */
public class AppUtil {
    private static final Object lockObj = new Object();
    private static Map<String, ThreadLocal<SimpleDateFormat>> sSdfMap = new HashMap<>();

    private AppUtil() {
    }


    /**
     * 是否是当前进程  一般用于Application中判断来保证某些东西只初始化一遍
     *
     * @param context
     * @return
     */
    public static boolean isCurProcess(Context context) {
        return context.getPackageName().equals(getCurProcessName(context));
    }

    /**
     * 获取当前进程名字
     *
     * @param context
     * @return
     */
    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager == null) {
            return null;
        }
        int size = activityManager.getRunningAppProcesses().size();
        for (int i = 0; i < size; i++) {
            ActivityManager.RunningAppProcessInfo runningAppProcessInfo = activityManager.getRunningAppProcesses().get(i);
            if (pid == runningAppProcessInfo.pid) {
                return runningAppProcessInfo.processName;
            }
        }
        return null;
    }

    /**
     * dp 转换为 px
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dp2px(Context context, int dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
    }

    /**
     * sp 转换为 px
     *
     * @param context
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, int spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, context.getResources().getDisplayMetrics());
    }


    /**
     * 由于SimpleDataFormat是线程不安全的，所以使用TreadLocal来保证不会报异常
     * getSdf("yyyy-MM-dd").format(new Data())
     * getSdf("yyyy-MM-dd").parse(xxx)
     *
     * @param pattern 匹配规则
     * @return
     */
    public static SimpleDateFormat getSdf(final String pattern) {
        ThreadLocal<SimpleDateFormat> tl = sSdfMap.get(pattern);
        if (tl == null) {
            synchronized (lockObj) {
                tl = sSdfMap.get(pattern);
                if (tl == null) {
                    tl = new ThreadLocal<SimpleDateFormat>() {
                        @Override
                        protected SimpleDateFormat initialValue() {
                            return new SimpleDateFormat(pattern);
                        }
                    };
                    sSdfMap.put(pattern, tl);
                }
            }
        }
        return tl.get();
    }
}
