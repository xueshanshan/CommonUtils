package com.star.common_utils.widget.recyclerview;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by su on 16-8-2.
 *
 * 针对垂直滚动的GridLayoutManager
 */
public class RecyclerGridSpaceDecoration extends RecyclerView.ItemDecoration {
    private int mSpacing; //需要的间距大小
    private int mSpanCount;  //column的个数
    private boolean mSpaceEqual; //左中右是否要相同的间距  如果为true则会均分，但是如果还想有拖拽就不要设置该参数为true,不然拖拽的时候会错乱，解决方法是该参数传false,然后给recyclerview设置相应的左右padding
    private boolean mHasHeader;  //如果有header那么将不在header两边留space，否则会留
    private boolean mHasFooter; //如果有footer那么将不在footer两边留space，否则会留

    public RecyclerGridSpaceDecoration(int spacing, int spanCount, boolean spaceEqual) {
        this(spacing, spanCount, false, false, spaceEqual);
    }

    public RecyclerGridSpaceDecoration(int spacing, int spanCount, boolean hasHeader, boolean hasFooter, boolean spaceEqual) {
        mSpacing = spacing / 2;
        mSpanCount = spanCount;
        mHasHeader = hasHeader;
        mHasFooter = hasFooter;
        this.mSpaceEqual = spaceEqual;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildLayoutPosition(view);
        int count = parent.getAdapter().getItemCount();
        if (isHeaderLine(position)) {
            outRect.set(0, 0, 0, mSpacing);
        } else if (isFooterLine(position, count)) {
            outRect.set(0, mSpacing, 0, 0);
        } else {
            if (mSpaceEqual && isFirstColumn(position, count)) {
                outRect.set(mSpacing * 2, mSpacing, mSpacing, mSpacing);
            } else if (mSpaceEqual && isLastColumn(position, count)) {
                outRect.set(mSpacing, mSpacing, mSpacing * 2, mSpacing);
            } else {
                outRect.set(mSpacing, mSpacing, mSpacing, mSpacing);
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

    private boolean isHeaderLine(int position) {
        return mHasHeader && position == 0;
    }

    private boolean isFooterLine(int position, int totalCount) {
        return mHasFooter && position == totalCount - 1;
    }
}
