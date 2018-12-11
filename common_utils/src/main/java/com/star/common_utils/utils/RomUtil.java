package com.star.common_utils.utils;

import android.content.Context;
import android.provider.Settings;

/**
 * @author xueshanshan
 * @date 2018/12/11
 */
public class RomUtil {
    private RomUtil() {
    }

    //http://lastwarmth.win/2018/09/13/phone-adapt/
    //小米手机是否开启全面屏
    public static boolean miuiNavigationGestureEnabled(Context context) {
        try {
            return Settings.Global.getInt(context.getContentResolver(), "force_fsg_nav_bar") != 0;
        } catch (Settings.SettingNotFoundException e) {
        }
        return false;
    }

    //vivo手机是否开启全面屏
    public static boolean vivoNavigationGestureEnabled(Context context) {
        try {
            return Settings.Secure.getInt(context.getContentResolver(), "navigation_gesture_on") != 0;
        } catch (Settings.SettingNotFoundException e) {
        }
        return false;
    }
}
