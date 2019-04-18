package com.star.common_utils.widget.recyclerview;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author xueshanshan
 * @date 2019/4/19
 */
public class RecyclerGridDividerDecoration extends RecyclerBottomDividerDecoration {
    private int mSpanCount;

    public RecyclerGridDividerDecoration(RecyclerView list, int dividerColor, int dividerHeight, int spanCount) {
        this(list, dividerColor, dividerHeight, false, false, spanCount);
    }

    public RecyclerGridDividerDecoration(RecyclerView list, int dividerColor, int dividerHeight, boolean hasHeader, boolean hasFooter, int spanCount) {
        super(list, dividerColor, dividerHeight, hasHeader, hasFooter);
        this.mSpanCount = spanCount;
    }


    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        drawVertical(c, parent);
        drawHorizontal(c, parent);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int childCount = parent.getChildCount();
        // 四个方向的偏移值
        int right = mDividerHeight;
        int bottom = mDividerHeight;

        if (isHeaderLine(position)) {
            //header下面不划线
            bottom = 0;
        }
        if (isFooterLine(position, childCount) || isLastLine(position, childCount)) {
            //最后一条不划线
            bottom = 0;
        }
        if (isHeaderLine(position) || isFooterLine(position, childCount) || isLastColumn(position, childCount)) {
            //最右边不划线
            right = 0;
        }
        outRect.set(0, 0, right, bottom);
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
            if (isFooterLine(i, childCount) || isLastLine(i, childCount)) {
                //最后一条不划线
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

    private void drawVertical(Canvas canvas, RecyclerView parent) {
        //绘制垂直间隔线 统一画view右侧
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            if (isHeaderLine(i) || isFooterLine(i, childCount) || isLastColumn(i, childCount)) {
                //最右边不划线
                continue;
            }
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int left = child.getRight() + params.rightMargin;
            int right = left + mDividerHeight;
            int top = child.getTop() - params.topMargin;
            int bottom = child.getBottom() + params.bottomMargin;

            if (mDivider != null) {
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(canvas);
            } else {
                canvas.drawRect(left, top, right, bottom, mDividerPaint);
            }
        }

    }

    /**
     * 是第一列
     *
     * @param position
     * @param totalCount
     * @return
     */
    private boolean isFirstColumn(int position, int totalCount) {
        if (mHasHeader) {
            position--;
        }
        return position % mSpanCount == 0;
    }

    /**
     * 是最后一列
     *
     * @param position
     * @param totalCount
     * @return
     */
    private boolean isLastColumn(int position, int totalCount) {
        if (mHasHeader) {
            position--;
        }
        return (position + 1) % mSpanCount == 0;
    }


    /**
     * 是否是数据是第一行
     *
     * @param position
     * @return
     */
    private boolean isFirstLine(int position, int totalCount) {
        if (mHasHeader) {
            position--;
        }
        return position < mSpanCount;
    }

    /**
     * 是否是数据最后一行
     *
     * @param position
     * @param totalCount
     * @return
     */
    private boolean isLastLine(int position, int totalCount) {
        if (mHasHeader) {
            totalCount--;
            position--;
        }
        if (mHasFooter) {
            totalCount--;
        }
        if (totalCount % mSpanCount != 0) {
            return position >= (totalCount / mSpanCount * mSpanCount);
        } else {
            return position >= ((totalCount / mSpanCount - 1) * mSpanCount);
        }
    }
}
