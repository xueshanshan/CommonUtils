package com.star.xpermission;

/**
 * @author xueshanshan
 * @date 2018/12/9
 */
public interface OnPermissionCallback {
    void onPermissionGranted(int reqCode);

    /**
     * 权限被拒绝
     * @param deniedPermission  被拒绝的权限
     * @param reqCode  请求码
     * @param mustShowCustomDialog 用户系统弹出点击不再提示，mustShowCustomDialog会为true 展示自定义弹窗引导用户开启权限
     */
    void onPermissionDenied(String deniedPermission, int reqCode, boolean mustShowCustomDialog);
}
