package com.star.xpermission;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.Fragment;

/**
 * @author xueshanshan
 * @date 2018/12/9
 *
 * 暴露给外界调用的类
 */
public class XPermission {

    /**
     * 在fragment内部进行权限请求
     *
     * @param fragment    要请求权限的fragment
     * @param requestCode 对应于PermissionSparseArray中定义的requestCode
     * @param callback    OnPermissionCallback类型的请求的回调
     */
    public static void permissionRequest(Fragment fragment, @PermissionSparseArray.PermissionRequestCode int requestCode, OnPermissionCallback callback) {
        permissionRequest(fragment.getActivity(), requestCode, callback);
    }

    /**
     * 在activity内部进行权限请求
     *
     * @param activity    要请求权限的activity
     * @param requestCode 对应于PermissionSparseArray中定义的requestCode
     * @param callback    OnPermissionCallback类型的请求的回调
     */
    public static void permissionRequest(Activity activity, @PermissionSparseArray.PermissionRequestCode int requestCode, OnPermissionCallback callback) {
        PermissionManagerByFragment.permissionRequest(activity, requestCode, callback);
    }

    /**
     * 在fragment内部进行权限请求
     *
     * @param fragment    要请求权限的fragment
     * @param requestCode 请求权限的requestCode 如果要同时请求多个权限，请使用这个及下面这种方式，如果请求单种权限上面两种方法足以使用
     * @param permissions 要请求的权限 一般是多个权限组里的权限拼接
     * @param callback    OnPermissionCallback类型的请求的回调
     */
    public static void permissionRequest(Fragment fragment, int requestCode, String[] permissions, OnPermissionCallback callback) {
        permissionRequest(fragment.getActivity(), requestCode, callback);
    }

    /**
     * 在activity内部进行权限请求
     *
     * @param activity    要请求权限的activity
     * @param requestCode 请求权限的requestCode 如果要同时请求多个权限，请使用这个及下面这种方式，如果请求单种权限上面两种方法足以使用
     * @param permissions 要请求的权限 一般是多个权限组里的权限拼接
     * @param callback    OnPermissionCallback类型的请求的回调
     */
    public static void permissionRequest(Activity activity, int requestCode, String[] permissions, OnPermissionCallback callback) {
        PermissionManagerByFragment.permissionRequest(activity, requestCode, permissions, callback);
    }

    public static void showTipDialog(final Context context, String title, String message) {
        Dialog dialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("设置权限", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        toSetPermissions(context);
                    }
                })
                .setNegativeButton("取消", null)
                .create();
        dialog.show();
    }

    public static void toSetPermissions(Context context) {
        context.startActivity(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", context.getPackageName(), null)));
    }
}
