package com.star.common_utils.utils;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xueshanshan
 * @date 2018/12/10
 */
public class AppUtil {
    private static Map<String, ThreadLocal<SimpleDateFormat>> sSdfMap = new HashMap<>();

    private AppUtil() {
    }

    public static int dp2px(Context context, int dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
    }

    public static int sp2px(Context context, int spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, context.getResources().getDisplayMetrics());
    }

    public static SimpleDateFormat getSdf(final String pattern) {
        ThreadLocal<SimpleDateFormat> tl = sSdfMap.get(pattern);
        if (tl == null) {
            tl = new ThreadLocal<SimpleDateFormat>() {
                @Override
                protected SimpleDateFormat initialValue() {
                    return new SimpleDateFormat(pattern);
                }
            };
            sSdfMap.put(pattern, tl);
        }
        return tl.get();
    }
}
