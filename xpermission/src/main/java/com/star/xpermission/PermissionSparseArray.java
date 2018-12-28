package com.star.xpermission;

import android.Manifest;
import android.os.Build;
import android.support.annotation.IntDef;
import android.util.SparseArray;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author xueshanshan
 * @date 2018/12/9
 *
 * 申请权限的集合，按android权限组进行分组。
 * - 按照权限组进行分组，某一权限组必须在清单文件中添加所有的权限。（这个很重要，如果清单文件中添加权限不够则会导致权限申请成功，但是回调失败）
 * - 如果不需要申请某一组的所有权限，则可以不使用该类定义的权限，或者动态的对该类进行扩展
 */
public class PermissionSparseArray extends SparseArray<String[]> {
    // 权限请求标识
    public static final int PERMISSION_STORAGE = 1;
    public static final int PERMISSION_CAMERA = 2;
    public static final int PERMISSION_CONTACTS = 3;
    public static final int PERMISSION_CALENDAR = 4;
    public static final int PERMISSION_LOCATION = 5;
    public static final int PERMISSION_MICROPHONE = 6;
    public static final int PERMISSION_PHONE = 7;
    public static final int PERMISSION_SENSORS = 8;
    public static final int PERMISSION_SMS = 9;


    @IntDef({PERMISSION_STORAGE, PERMISSION_CAMERA, PERMISSION_CONTACTS, PERMISSION_CALENDAR, PERMISSION_LOCATION, PERMISSION_MICROPHONE, PERMISSION_PHONE, PERMISSION_SENSORS, PERMISSION_SMS})
    @Retention(RetentionPolicy.SOURCE)
    public @interface PermissionRequestCode {

    }

    private static PermissionSparseArray sPermissionArray = new PermissionSparseArray();

    private PermissionSparseArray() {
        //Storage
        String[] storage = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE};
        this.put(PERMISSION_STORAGE, storage);

        String[] camera = {Manifest.permission.CAMERA};
        this.put(PERMISSION_CAMERA, camera);

        String[] contacts = {
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.WRITE_CONTACTS,
                Manifest.permission.GET_ACCOUNTS};
        this.put(PERMISSION_CONTACTS, contacts);

        String[] calendar = {
                Manifest.permission.READ_CALENDAR,
                Manifest.permission.WRITE_CALENDAR};
        this.put(PERMISSION_CALENDAR, calendar);


        String[] location = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};
        this.put(PERMISSION_LOCATION, location);

        String[] microphone = {Manifest.permission.RECORD_AUDIO};
        this.put(PERMISSION_MICROPHONE, microphone);


        String[] phone = {
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_CALL_LOG,
                Manifest.permission.WRITE_CALL_LOG,
                Manifest.permission.ADD_VOICEMAIL,
                Manifest.permission.USE_SIP,
                Manifest.permission.PROCESS_OUTGOING_CALLS};
        this.put(PERMISSION_PHONE, phone);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            String[] sensors = {Manifest.permission.BODY_SENSORS};
            this.put(PERMISSION_SENSORS, sensors);
        }

        String[] sms = {
                Manifest.permission.SEND_SMS,
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.READ_SMS,
                Manifest.permission.RECEIVE_WAP_PUSH,
                Manifest.permission.RECEIVE_MMS};
        this.put(PERMISSION_SMS, sms);
    }

    public static PermissionSparseArray getInstance() {
        return sPermissionArray;
    }

    public static String[] mergePermissionArrays(@PermissionRequestCode int... requestCode) {
        int totalLength = 0;
        int length = requestCode.length;
        for (int i = 0; i < length; i++) {
            totalLength += getInstance().get(requestCode[i]).length;
        }
        String[] newArray = new String[totalLength];
//        public static native void arraycopy(Object src, int srcPos, Object dest, int destPos, int length);
        int lastIndex = 0;
        for (int i = 0; i < length; i++) {
            int i1 = requestCode[i];
            String[] strings = getInstance().get(i1);
            System.arraycopy(strings, 0, newArray, lastIndex, strings.length);
            lastIndex = strings.length;
        }
        return newArray;
    }
}
