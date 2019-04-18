package com.star.common_utils.widget.recyclerview;

import android.graphics.Canvas;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.star.common_utils.utils.LogUtil;

/**
 * Created by xueshanshan on 2017/10/30.
 */

public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {
    public static final float ALPHA_FULL = 1.0f;

    private final ItemTouchHelperListener mItemTouchListener;

    public SimpleItemTouchHelperCallback(ItemTouchHelperListener itemTouchListener) {
        mItemTouchListener = itemTouchListener;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return mItemTouchListener.isLongPressDragEnabled();
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return mItemTouchListener.isItemViewSwipeEnabled();
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager && ((GridLayoutManager) manager).getSpanCount() > 1) {
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            final int swipeFlags = 0;
            return makeMovementFlags(dragFlags, swipeFlags);
        } else if (manager instanceof LinearLayoutManager) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            boolean scrollHorizontally = layoutManager.canScrollHorizontally();
            if (scrollHorizontally) {
                final int dragFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                final int swipeFlags = 0;
                return makeMovementFlags(dragFlags, swipeFlags);
            } else {
                final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags);
            }
        } else {
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            return makeMovementFlags(dragFlags, swipeFlags);
        }
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        if (source.getItemViewType() != target.getItemViewType()) {
            return false;
        }

        // Notify the adapter of the move
        LogUtil.d("touchHelper", "onItemMove");
        mItemTouchListener.onItemMove(source, target, source.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        // Notify the adapter of the dismissal
        mItemTouchListener.onItemDismiss(viewHolder.getAdapterPosition());
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            // Fade out the view as it is swiped out of the parent's bounds
            final float alpha = ALPHA_FULL - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
            viewHolder.itemView.setAlpha(alpha);
            viewHolder.itemView.setTranslationX(dX);
        } else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        // We only want the active item to change
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            // Let the view holder know that this item is being moved or dragged
            LogUtil.d("touchHelper", "onItemSelected(true)");
            mItemTouchListener.onItemSelected(viewHolder, true);
        } else {
            mItemTouchListener.onItemSelected(viewHolder, false);
            LogUtil.d("touchHelper", "onItemSelected(false)");
        }

        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        viewHolder.itemView.setAlpha(ALPHA_FULL);

        // Tell the view holder it's time to restore the idle state
        mItemTouchListener.onItemClear(viewHolder);
    }
}
