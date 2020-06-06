package com.star.xpermission;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;

/**
 * @author xueshanshan
 * @date 2018/9/30
 */
public class PermissionManagerByFragment {
    private static String TAG = PermissionManagerByFragment.class.getSimpleName();

    private static OnPermissionFragment getOnPermissionFragment(Activity activity) {
        OnPermissionFragment onPermissionFragment = findOnPermissionFragment(activity);
        if (onPermissionFragment == null) {
            onPermissionFragment = new OnPermissionFragment();
            FragmentManager fragmentManager = activity.getFragmentManager();
            fragmentManager.beginTransaction().add(onPermissionFragment, TAG).commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }
        return onPermissionFragment;
    }

    private static OnPermissionFragment findOnPermissionFragment(Activity activity) {
        return (OnPermissionFragment) activity.getFragmentManager().findFragmentByTag(TAG);
    }

    private static boolean checkPermission(Activity activity, String[] permissions) {
        for (String permission : permissions) {
            int permissionCheck = ContextCompat.checkSelfPermission(activity, permission);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    protected static void permissionRequest(Activity activity, @PermissionSparseArray.PermissionRequestCode int requestCode, OnPermissionCallback callback) {
        String[] permissions = PermissionSparseArray.getInstance().get(requestCode);
        permissionRequest(activity, requestCode, permissions, callback);
    }

    protected static void permissionRequest(Activity activity, int requestCode, String[] permissions, OnPermissionCallback callback) {
        if (activity == null) {
            return;
        }
        if (checkPermission(activity, permissions) || Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            callback.onPermissionGranted(requestCode);
        } else {
            getOnPermissionFragment(activity).permissionRequest(requestCode, permissions, callback);
        }
    }
}
