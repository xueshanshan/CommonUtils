package com.star.common_utils.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Process;
import android.provider.Settings;
import android.text.TextUtils;

import com.star.common_utils.BuildConfig;

/**
 * @author xueshanshan
 * @date 2018/12/10
 */
public class AppInfoUtil {
    private static Context sContext;

    private static int sProcessId = -1;
    private static String sProcessName = "";
    private static String sDeviceId = "";

    private static String sVersionName = "";
    private static int sVersionCode;
    private static String sPackageName = "";
    private static String sAppName = "";
    private static String sApplicationLabel = "";
    private static boolean sDebuggable;

    private static int sStatusBarHeight;
    private static int sNavigationBarHeight;
    private static int sActionBarHeight;

    private static int sScreenRealWidth;
    private static int sScreenRealHeight;
    private static int sScreenAvailableWidth;
    private static int sScreenAvailableHeight;

    private AppInfoUtil() {
    }

    /**
     * 初始化方法，需要在Application中初始化
     *
     * @param context 上下文
     */
    public static void init(Context context) {
        sContext = context.getApplicationContext();
        initProcess();
        sDeviceId = Settings.Secure.getString(sContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        initPackageInfo();
        initScreenSize();
        initBars();
        LogUtil.d(intoToString());
    }

    private static void initProcess() {
        sProcessId = Process.myPid();
        sProcessName = IOUtil.readFileToString("/proc/self/cmdline").trim();
    }

    private static void initPackageInfo() {
        if (TextUtils.isEmpty(sVersionName)) {
            try {
                PackageManager packageManager = sContext.getPackageManager();
                PackageInfo packageInfo = packageManager.getPackageInfo(sContext.getPackageName(), 0);
                sVersionName = packageInfo.versionName;
                sVersionCode = packageInfo.versionCode;
                sPackageName = packageInfo.packageName;
                sAppName = packageInfo.applicationInfo.loadLabel(packageManager).toString();
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(sPackageName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
                sApplicationLabel = packageManager.getApplicationLabel(applicationInfo).toString();
                sDebuggable = BuildConfig.DEBUG;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private static void initScreenSize() {
        Point realSize = UIUtil.getScreenRealSize(sContext);
        sScreenRealWidth = Math.min(realSize.x, realSize.y);
        sScreenRealHeight = Math.max(realSize.x, realSize.y);
        Point availableSize = UIUtil.getScreenAvailAbleSize(sContext);
        sScreenAvailableWidth = Math.min(availableSize.x, availableSize.y);
        sScreenAvailableHeight = Math.max(availableSize.x, availableSize.y);
    }

    private static void initBars() {
        sStatusBarHeight = StatusBarUtil.getStatusBarHeight(sContext);
        sActionBarHeight = UIUtil.getActionBarHeight(sContext);
        sNavigationBarHeight = UIUtil.getNavigationBarHeight(sContext).y;
        //如果开启全面屏的话，手机可用高度需要加导航栏高度
        if (RomUtil.miuiNavigationGestureEnabled(sContext) || RomUtil.vivoNavigationGestureEnabled(sContext)) {
            if (sScreenAvailableHeight == sScreenRealHeight - sNavigationBarHeight) {
                sScreenAvailableHeight = sScreenRealHeight;
            }
        }
    }

    public static Context getContext() {
        return sContext;
    }

    public static int getProcessId() {
        return sProcessId;
    }

    public static String getProcessName() {
        return sProcessName;
    }

    public static String getDeviceId() {
        return sDeviceId;
    }

    public static String getVersionName() {
        return sVersionName;
    }

    public static int getVersionCode() {
        return sVersionCode;
    }

    public static String getPackageName() {
        return sPackageName;
    }

    public static String getAppName() {
        return sAppName;
    }

    public static String getApplicationLabel() {
        return sApplicationLabel;
    }

    public static boolean isDebuggable() {
        return sDebuggable;
    }

    public static int getStatusBarHeight() {
        return sStatusBarHeight;
    }

    public static int getNavigationBarHeight() {
        return sNavigationBarHeight;
    }

    public static int getActionBarHeight() {
        return sActionBarHeight;
    }

    public static int getScreenRealWidth() {
        return sScreenRealWidth;
    }

    public static int getScreenRealHeight() {
        return sScreenRealHeight;
    }

    public static int getScreenAvailableWidth() {
        return sScreenAvailableWidth;
    }

    public static int getScreenAvailableHeight() {
        return sScreenAvailableHeight;
    }

    public static String intoToString() {
        return "AppInfoUtil{" +
                "\ncontext=" + sContext +
                "\ndebuggable=" + sDebuggable +
                "\nprocessId=" + sProcessId +
                "\nprocessName=" + sProcessName +
                "\nversionName=" + sVersionName +
                "\nversionCode=" + sVersionCode +
                "\npackageName=" + sPackageName +
                "\nappName=" + sAppName +
                "\napplicationLabel=" + sApplicationLabel +
                "\ndeviceId=" + sDeviceId +
                "\nscreenRealWidth=" + sScreenRealWidth +
                "\nscreenRealHeight=" + sScreenRealHeight +
                "\nscreenAvailableWidth=" + sScreenAvailableWidth +
                "\nscreenAvailableHeight=" + sScreenAvailableHeight +
                "\nstatusBarHeight=" + sStatusBarHeight +
                "\nnavigationBarHeight=" + sNavigationBarHeight +
                "\nactionBarHeight=" + sActionBarHeight +
                "}";
    }
}
