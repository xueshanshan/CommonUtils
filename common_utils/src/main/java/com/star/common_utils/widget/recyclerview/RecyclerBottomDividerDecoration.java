package com.star.common_utils.widget.recyclerview;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author xueshanshan
 * @date 2018/11/6
 *
 * 针对垂直滚动的LinearLayoutManager
 *
 * 实现的结果是只在item之间划线
 */
public class RecyclerBottomDividerDecoration extends RecyclerView.ItemDecoration {
    protected RecyclerView mList;
    protected Drawable mDivider;
    protected int mDividerHeight;
    protected Paint mDividerPaint;
    protected boolean mHasHeader;
    protected boolean mHasFooter;

    public RecyclerBottomDividerDecoration(RecyclerView list, int dividerColor, int dividerHeight) {
        this(list, dividerColor, dividerHeight, false, false);
    }

    public RecyclerBottomDividerDecoration(RecyclerView list, int dividerColor, int dividerHeight, boolean hasHeader, boolean hasFooter) {
        mList = list;
        setDividerColorAndHeight(dividerColor, dividerHeight);
        mHasHeader = hasHeader;
        mHasFooter = hasFooter;
    }

    @Override
    public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        drawHorizontal(canvas, parent);
    }

    private void drawHorizontal(Canvas canvas, RecyclerView parent) {
        // 绘制水平间隔线
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            if (isHeaderLine(i)) {
                //header下面不划线
                continue;
            }
            if (isFooterLine(i, childCount)) {
                //最后一条不划线
                continue;
            }
            if (mHasFooter && i == childCount - 2) {
                //footer上面不划线
                continue;
            }
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int left = child.getLeft() - params.leftMargin;
            int right = child.getRight() + params.rightMargin;
            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDividerHeight;

            if (mDivider != null) {
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(canvas);
            } else {
                canvas.drawRect(left, top, right, bottom, mDividerPaint);
            }
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int childAdapterPosition = parent.getChildAdapterPosition(view);
        int childCount = parent.getChildCount();
        if (isHeaderLine(childAdapterPosition)) {
            //header下面不划线
            outRect.bottom = 0;
        } else if (isFooterLine(childAdapterPosition, childCount)) {
            //最后一条不划线
            outRect.bottom = 0;
        } else if (mHasFooter && childAdapterPosition == childCount - 2) {
            //footer上面不划线
            outRect.bottom = 0;
        } else {
            outRect.bottom = mDividerHeight;
        }
    }

    public void setDivider(Drawable divider) {
        if (divider != null) {
            mDividerHeight = divider.getIntrinsicHeight();
        } else {
            mDividerHeight = 0;
        }
        mDivider = divider;
        mList.invalidateItemDecorations();
    }

    public void setDividerColorAndHeight(int color, int height) {
        if (mDividerPaint == null) {
            mDividerPaint = new Paint();
        }
        mDividerPaint.setColor(color);
        mDividerHeight = height;
        mDividerPaint.setStrokeWidth(height);
        mList.invalidateItemDecorations();
    }

    protected boolean isHeaderLine(int position) {
        return mHasHeader && position == 0;
    }

    protected boolean isFooterLine(int position, int totalCount) {
        return mHasFooter && position == totalCount - 1;
    }
}
