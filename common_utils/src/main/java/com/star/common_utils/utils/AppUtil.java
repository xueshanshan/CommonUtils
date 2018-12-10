package com.star.common_utils.utils;

import android.content.Context;
import android.util.TypedValue;

/**
 * @author xueshanshan
 * @date 2018/12/10
 */
public class AppUtil {
    public static int dp2px(int dpValue, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
    }

    public static int sp2px(int spValue, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, context.getResources().getDisplayMetrics());
    }
}
