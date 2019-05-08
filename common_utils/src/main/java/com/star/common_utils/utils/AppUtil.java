package com.star.common_utils.utils;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Application;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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

    /**
     * 安装apk  前提是已经申请过读sd卡的权限  否则会报解析包异常
     *
     * @param context
     * @param file
     * @param providerName
     */
    public static void installApk(Context context, File file, String providerName) {
        if (!file.exists()) {
            Toast.makeText(context, "文件不存在", Toast.LENGTH_SHORT).show();
            return;
        }
        //如果不写段代码系统会弹窗提示是否允许安装未知来源的应用
        //这段代码就是自己去引导用户允许安装未知来源应用
        //可根据需求选择用系统弹窗还是自己引导用户
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !context.getPackageManager().canRequestPackageInstalls()) {
//            Uri packageUri = Uri.parse("package:" + context.getPackageName());
//            Intent settingsIntent = new Intent()
//                    .setAction(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
//                    .setData(packageUri);
//            //跳转 然后接收onActivityResult 成功后再次调用installApk
//            //startActivityForResult(settingsIntent, REQUEST_AUTHORIZATION);
//            context.startActivity(settingsIntent);
//            return;
//        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        //适配7.0 需要使用FileProvider    并且需要对目标临时授权使用FLAG_GRANT_READ_URI_PERMISSION
        //同时8.0需要在清单文件中添加权限    <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />  否则跳转无响应
        //如果不适配将崩溃  FileUriExposedException
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri apkUri = FileProvider.getUriForFile(context, providerName, file);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");

            //如果不添加该权限，系统会弹窗解析包出现问题
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            addPermission(context, intent, apkUri);
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }


    public static void addPermission(Context context, Intent intent, Uri apkUrl) {
        List resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (resInfoList == null || resInfoList.isEmpty()) {
            LogUtil.d("未找到安装程序");
            return;
        }
        Iterator resInfoIterator = resInfoList.iterator();
        while (resInfoIterator.hasNext()) {
            ResolveInfo resolveInfo = (ResolveInfo) resInfoIterator.next();
            String packageName = resolveInfo.activityInfo.packageName;
            context.grantUriPermission(packageName, apkUrl, Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
    }

    /**
     * 检查是否有悬浮窗权限
     *
     * @return
     */
    public static boolean checkDrawOverlaysPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(AppInfoUtil.sContext);
        }
        return true;
    }

    /**
     * 请求悬浮窗权限
     */
    public static void requestOverlayPermission() {
        try {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            intent.setData(Uri.parse("package:" + AppInfoUtil.sContext.getPackageName()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            AppInfoUtil.sContext.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }
}
