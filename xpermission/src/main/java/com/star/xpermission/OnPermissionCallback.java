package com.star.xpermission;

/**
 * @author xueshanshan
 * @date 2018/12/9
 */
public interface OnPermissionCallback {
    void onPermissionGranted(int reqCode);

    void onPermissionDenied(String deniedPermission, int reqCode);

    void shouldShowRequestPermissionTip(String requestPermissionRationale, int reqCode);
}
