package com.star.common_utils.utils;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * @author xueshanshan
 * @date 2019/4/17
 *
 * 屏幕适配
 * https://mp.weixin.qq.com/s?__biz=MzI1MzYzMjE0MQ==&mid=2247484502&idx=2&sn=a60ea223de4171dd2022bc2c71e09351&scene=21#wechat_redirect
 * https://juejin.im/post/5b7fafb351882542af1c75ad
 */
public class ScreenUtil {

    private static boolean sCustomDensityAppInited;
    private static float sTargetDensity;
    private static float sTargetScaleDensity;
    private static int sTargetDensityDpi;

    private ScreenUtil() {
    }

    /**
     * Activity全屏
     *
     * @param activity
     */
    public static void setFullScreen(Activity activity) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * Activity取消全屏
     *
     * @param activity
     */
    public static void setNonFullScreen(Activity activity) {
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 屏幕适配 在Application中调用
     *
     * @param app
     */
    public static void initCustomDensityApp(Application app) {
        if (sCustomDensityAppInited) {
            return;
        }
        sCustomDensityAppInited = true;

        DisplayMetrics systemDm = Resources.getSystem().getDisplayMetrics();
        final DisplayMetrics displayMetrics = app.getResources().getDisplayMetrics();

        //监听设置字体大小
        app.registerComponentCallbacks(new ComponentCallbacks() {
            @Override
            public void onConfigurationChanged(Configuration newConfig) {
                if (newConfig != null && newConfig.fontScale > 0) {
                    DisplayMetrics systemDm = Resources.getSystem().getDisplayMetrics();
                    sTargetScaleDensity = sTargetDensity * (systemDm.scaledDensity / systemDm.density);

                    displayMetrics.density = sTargetDensity;
                    displayMetrics.scaledDensity = sTargetScaleDensity;
                    displayMetrics.densityDpi = sTargetDensityDpi;
                }
            }

            @Override
            public void onLowMemory() {

            }
        });

        sTargetDensity = systemDm.widthPixels / 360f;
        //字体的缩放
        sTargetScaleDensity = sTargetDensity * (systemDm.scaledDensity / systemDm.density);
        sTargetDensityDpi = (int) (sTargetDensity * 160);


        displayMetrics.density = sTargetDensity;
        displayMetrics.scaledDensity = sTargetScaleDensity;
        displayMetrics.densityDpi = sTargetDensityDpi;

    }

    /**
     * 屏幕适配 在Activity中调用
     *
     * @param activity
     */
    public static void initCustomDensityActivity(Activity activity) {
        if (!sCustomDensityAppInited) {
            return;
        }
        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();

        displayMetrics.density = sTargetDensity;
        displayMetrics.scaledDensity = sTargetScaleDensity;
        displayMetrics.densityDpi = sTargetDensityDpi;
    }

    /**
     * 取消自定义的屏幕适配  主要用于webview或者系统dialog适配导致的问题
     *
     * @param application
     * @param activity
     */
    public static void cancelCustomDensity(Application application, Activity activity) {
        final DisplayMetrics systemDm = Resources.getSystem().getDisplayMetrics();
        final DisplayMetrics appDm = application.getResources().getDisplayMetrics();
        final DisplayMetrics activityDm = activity.getResources().getDisplayMetrics();

        appDm.density = systemDm.density;
        appDm.scaledDensity = systemDm.scaledDensity;
        appDm.densityDpi = systemDm.densityDpi;

        activityDm.density = systemDm.density;
        activityDm.scaledDensity = systemDm.scaledDensity;
        activityDm.densityDpi = systemDm.densityDpi;
    }

    /**
     * 重新恢复自定义屏幕适配 主要用于webview或者系统dialog适配导致的问题
     *
     * @param application
     * @param activity
     */
    public static void restoreCustomDensity(Application application, Activity activity) {
        final DisplayMetrics appDm = application.getResources().getDisplayMetrics();
        final DisplayMetrics activityDm = activity.getResources().getDisplayMetrics();

        appDm.density = sTargetDensity;
        appDm.scaledDensity = sTargetScaleDensity;
        appDm.densityDpi = sTargetDensityDpi;

        activityDm.density = sTargetDensity;
        activityDm.scaledDensity = sTargetScaleDensity;
        activityDm.densityDpi = sTargetDensityDpi;
    }

}
