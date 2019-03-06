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
    private static boolean inited;
    public static Context sContext;

    public static int sProcessId = -1;
    public static String sProcessName = "";
    public static String sDeviceId = "";

    public static PackageInfo packageInfo;
    public static String sVersionName = "";
    public static int sVersionCode;
    public static String sPackageName = "";
    public static String sAppName = "";
    public static String sApplicationLabel = "";
    public static boolean sDebuggable;

    //从meta-data中获取  应用需要配置对应的meta-data
    public static String sVersionType = "";
    public static String sAppBuildTime = "";
    public static String sUMengChannel = "";
    public static String sPackageFrom = "";
    public static String sAppBranch = "";
    public static String sAppCommitId = "";

    public static int sStatusBarHeight;
    public static int sNavigationBarHeight;
    public static int sActionBarHeight;

    public static int sScreenRealWidth;
    public static int sScreenRealHeight;
    public static int sScreenAvailableWidth;
    public static int sScreenAvailableHeight;

    private AppInfoUtil() {
    }

    /**
     * 初始化方法，需要在Application中初始化
     *
     * @param context 上下文
     */
    public static void init(Context context) {
        if (inited) {
            return;
        }
        inited = true;
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
                packageInfo = packageManager.getPackageInfo(sContext.getPackageName(), 0);

                sVersionName = packageInfo.versionName;
                sVersionCode = packageInfo.versionCode;
                sPackageName = packageInfo.packageName;
                sAppName = packageInfo.applicationInfo.loadLabel(packageManager).toString();
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(sPackageName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
                sApplicationLabel = packageManager.getApplicationLabel(applicationInfo).toString();
                sDebuggable = BuildConfig.DEBUG;

                ApplicationInfo appInfo = packageManager.getApplicationInfo(sContext.getPackageName(), PackageManager.GET_META_DATA);
                sUMengChannel = getMetaDataField(appInfo, "UMENG_CHANNEL");
                sVersionType = getMetaDataField(appInfo, "VERSION_TYPE");
                sAppCommitId = getMetaDataField(appInfo, "COMMIT_ID");
                sPackageFrom = getMetaDataField(appInfo, "PACKAGE_FROM");
                sAppBranch = getMetaDataField(appInfo, "BRANCH");
                sAppBuildTime = getMetaDataField(appInfo, "BUILD_TIME");
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getMetaDataField(ApplicationInfo appInfo, String key) {
        if (appInfo.metaData.containsKey(key)) {
            return String.valueOf(appInfo.metaData.get(key));
        }
        return "";
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
                "\nsVersionType=" + sVersionType +
                "\nsUMengChannel=" + sUMengChannel +
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
