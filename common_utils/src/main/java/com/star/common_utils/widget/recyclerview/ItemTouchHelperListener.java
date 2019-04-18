package com.star.common_utils.widget.recyclerview;

import android.support.v7.widget.RecyclerView;

/**
 * Created by xueshanshan on 2017/10/30.
 */

public interface ItemTouchHelperListener {

    boolean isLongPressDragEnabled();

    boolean isItemViewSwipeEnabled();

    void onItemSelected(RecyclerView.ViewHolder holder, boolean selected);

    boolean onItemMove(RecyclerView.ViewHolder fromViewHolder, RecyclerView.ViewHolder toViewHolder, int fromPosition, int toPosition);

    void onItemDismiss(int position);

    void onItemClear(RecyclerView.ViewHolder holder);
}
