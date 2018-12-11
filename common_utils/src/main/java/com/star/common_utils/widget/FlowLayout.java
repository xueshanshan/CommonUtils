package com.star.common_utils.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.star.common_utils.R;
import com.star.common_utils.utils.AppUtil;

/**
 * @author xueshanshan
 * @date 2018/9/5
 */
public class FlowLayout extends ViewGroup {
    private float mWidthMargin;
    private float mHeightMargin;
    private Context mContext;

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout);
        mWidthMargin = array.getDimension(R.styleable.FlowLayout_width_margin, AppUtil.dp2px(context, 10));
        mHeightMargin = array.getDimension(R.styleable.FlowLayout_height_margin, AppUtil.dp2px(context, 10));
        array.recycle();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int horizontalWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        int availableWidth = horizontalWidth;
        int childTop = getPaddingTop();
        int childLeft = getPaddingLeft();
        int maxLineHeight = 0;  //每一行最大高度

        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (childAt != null && childAt.getVisibility() != View.GONE) {
                int childWidth = childAt.getMeasuredWidth();
                int childHeight = childAt.getMeasuredHeight();

                //不够放了
                if (availableWidth < childWidth) {
                    availableWidth = horizontalWidth;
                    childTop += maxLineHeight + mHeightMargin;
                    //重置每行的最大高度
                    maxLineHeight = 0;
                    //重置childLeft
                    childLeft = getPaddingLeft();
                }
                childAt.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
                childLeft += childWidth + mWidthMargin;
                availableWidth -= childWidth + mWidthMargin;
                maxLineHeight = Math.max(maxLineHeight, childHeight);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode != MeasureSpec.EXACTLY) {
            width = AppUtil.dp2px(mContext,200);
        }
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
        }
        if (heightMode != MeasureSpec.EXACTLY) {
            setMeasuredDimension(width, getDesiredHeight(width));
        } else {
            setMeasuredDimension(width, height);
        }
    }

    private int getDesiredHeight(int width) {
        int horizontalWidth = width - getPaddingLeft() - getPaddingRight();
        int availableWidth = horizontalWidth;
        int totalHeight = getPaddingTop() + getPaddingBottom();
        int maxLineHeight = 0;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (childAt != null && childAt.getVisibility() != View.GONE) {
                int childWidth = childAt.getMeasuredWidth();
                int childHeight = childAt.getMeasuredHeight();
                if (availableWidth < childWidth) {
                    availableWidth = horizontalWidth;
                    totalHeight += maxLineHeight + mHeightMargin;
                    maxLineHeight = 0;
                }
                availableWidth -= childWidth + mWidthMargin;
                maxLineHeight = Math.max(maxLineHeight, childHeight);
                if (i == childCount - 1) {
                    totalHeight += maxLineHeight;
                }
            }
        }
        return totalHeight;
    }
}
