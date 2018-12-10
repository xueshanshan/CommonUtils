package com.star.common_utils.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 * @author xueshanshan
 * @date 2018/12/5
 */
public class StatusBarUtil {
    private StatusBarUtil() {
    }

    /**
     * 设置状态栏颜色，4.4以上起作用
     *
     * @param window    window对象
     * @param color     状态栏颜色值  这个色值可以从style中获取colorPrimaryDark的值
     * @param isAddView 对于5.0以下是否要绘制一个带颜色的状态栏，一般情况下需要，在activity是半截的情况下可能不需要
     */
    public static void setStatusBarColor(Window window, int color, boolean isAddView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.0及以上，不设置透明状态栏，设置会有半透明阴影
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(color);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (isAddView) {
                View statusBarView = new View(window.getContext());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        getStatusBarHeight(window.getContext()));
                statusBarView.setLayoutParams(params);
                statusBarView.setBackgroundColor(color);
                ViewGroup decorView = (ViewGroup) window.getDecorView();
                decorView.addView(statusBarView);
            }
            ViewGroup contentView = (ViewGroup) window.findViewById(android.R.id.content);
            View rootView = contentView.getChildAt(0);
            if (rootView instanceof ViewGroup) {
                rootView.setFitsSystemWindows(true);
            }
        }
    }

    /**
     * 透明状态栏，4.4以上起作用
     *
     * @param window window对象
     */
    public static void translucentStatusBar(Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // Translucent status bar
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 在透明状态栏的情况下，给最上面的view重新设置高度和paddingTop
     *
     * @param view 要设置的view
     */
    public static void setTranslucentHeightAndPaddingTop(final View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            view.post(new Runnable() {
                @Override
                public void run() {
                    ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                    layoutParams.height = view.getMeasuredHeight() + getStatusBarHeight(view.getContext());
                    view.setPadding(view.getPaddingLeft(),
                            view.getPaddingTop() + getStatusBarHeight(view.getContext()),
                            view.getPaddingRight(),
                            view.getPaddingBottom());
                    view.setLayoutParams(layoutParams);
                }
            });
        }
    }

    /**
     * 获取状态栏的高度
     *
     * @param context 需要上下文Context
     * @return 返回得到的状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int result = 0;
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
