package com.star.xpermission;

import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.SparseArray;

/**
 * @author xueshanshan
 * @date 2018/9/30
 */
public class OnPermissionFragment extends Fragment {
    private SparseArray<OnPermissionCallback> mCallbacks = new SparseArray<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置fragment保留，已保留的fragment（在异常情况下）不会随着activity一起销毁，相反，它会一直保留（进程不消亡的前提下）
        //比如在设备旋转的情况下，该fragment可暂时与Activity分离，等activity重新创建后，会将该fragment与activity重新绑定,fragment数据不会丢失
        //如果在恰好在分离的那段时间使用Context信息，就可能会出错，这时可以使用isAdded()判断Fragment是否绑定到Activity
        setRetainInstance(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void permissionRequest(int requestCode, String[] permissions, OnPermissionCallback callback) {
        mCallbacks.put(requestCode, callback);
        requestPermissions(permissions, requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        OnPermissionCallback callback = findCallback(requestCode);
        if (callback != null) {
            boolean granted = true;
            String deniedPermission = "";
            for (int i = 0; i < grantResults.length; i++) {
                int grantResult = grantResults[i];
                granted = grantResult == PackageManager.PERMISSION_GRANTED;
                if (!granted) {
                    deniedPermission = permissions[i];
                    break;
                }
            }
            if (granted) {
                callback.onPermissionGranted(requestCode);
            } else {
                callback.onPermissionDenied(deniedPermission, requestCode);
            }
        }
    }

    private OnPermissionCallback findCallback(int requestCode) {
        OnPermissionCallback onPermissionCallback = mCallbacks.get(requestCode);
        mCallbacks.remove(requestCode);
        return onPermissionCallback;
    }
}
