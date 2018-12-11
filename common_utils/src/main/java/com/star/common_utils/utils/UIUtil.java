package com.star.common_utils.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

/**
 * @author xueshanshan
 * @date 2018/12/10
 */
public class UIUtil {
    private UIUtil() {
    }

    /**
     * 获取attr的值
     *
     * @param context 上下文
     * @param attr    对应的attr属性，比如R.attr.colorPrimaryDark
     * @return 返回得到的值
     */
    public static int getThemeAttr(Context context, int attr) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(attr, typedValue, true);
        return typedValue.data;
    }

    /**
     * 获取手机真实的屏幕尺寸
     *
     * @param context 上下文
     * @return 返回真实屏幕尺寸
     */
    public static Point getScreenRealSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        //屏幕实际大小
        Point realSize = new Point();
        display.getRealSize(realSize);
        return realSize;
    }

    /**
     * 获取手机可用的屏幕尺寸
     *
     * @param context 上下文
     * @return 返回可用屏幕尺寸
     */
    public static Point getScreenAvailAbleSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        //屏幕可用大小
        Point availableSize = new Point();
        display.getSize(availableSize);
        return availableSize;
    }

    /**
     * 获取ActionBar的高度
     *
     * @param context 上下文
     * @return 返回ActionBar的高度
     */
    public static int getActionBarHeight(Context context) {
        TypedValue tv = new TypedValue();
        int actionBarHeight = 0;
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                actionBarHeight += AppUtil.dp2px(context, 8);
            }
        }
        return actionBarHeight;
    }

    /**
     * 获取底部导航栏的高度
     * https://stackoverflow.com/questions/20264268/how-to-get-height-and-width-of-navigation-bar-programmatically
     *
     * @param context 上下文
     * @return 返回导航栏的Point对象，包含宽高
     */
    public static Point getNavigationBarHeight(Context context) {
        Point realSize = getScreenRealSize(context);
        Point availAbleSize = getScreenAvailAbleSize(context);
        if (availAbleSize.x < realSize.x) {
            return new Point(realSize.x - availAbleSize.x, availAbleSize.y);
        }
        if (availAbleSize.y < realSize.y) {
            return new Point(availAbleSize.x, realSize.y - availAbleSize.y);
        }
        return new Point();
    }
}
