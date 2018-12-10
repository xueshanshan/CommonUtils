package com.star.common_utils.utils;

import android.content.res.Resources;
import android.util.TypedValue;

/**
 * @author xueshanshan
 * @date 2018/12/10
 */
public class UIUtil {
    /**
     * 获取attr的值
     *
     * @param theme 当前主题，在activity中可以直接使用getTheme()获取
     * @param attr  对应的attr属性，比如R.attr.colorPrimaryDark
     * @return 返回得到的值
     */
    public static int getThemeColorByAttrId(Resources.Theme theme, int attr) {
        TypedValue typedValue = new TypedValue();
        theme.resolveAttribute(attr, typedValue, true);
        return typedValue.data;
    }
}
