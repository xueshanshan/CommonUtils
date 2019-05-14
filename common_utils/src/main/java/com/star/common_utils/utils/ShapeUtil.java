package com.star.common_utils.utils;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

/**
 * @author xueshanshan
 * @date 2019-05-14
 */
public class ShapeUtil {

    public static GradientDrawable getRoundRectDrawable(int radius, int color, boolean isFill, int strokeWidth) {
        //左上、右上、右下、左下的圆角半径
        float[] radiuss = {radius, radius, radius, radius, radius, radius, radius, radius};
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadii(radiuss);
        drawable.setColor(isFill ? color : Color.TRANSPARENT);
        drawable.setStroke(isFill ? 0 : strokeWidth, color);
        return drawable;
    }
}
