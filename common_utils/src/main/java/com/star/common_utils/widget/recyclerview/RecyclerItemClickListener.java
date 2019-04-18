package com.star.common_utils.widget.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author xueshanshan
 * @date 2018/11/9
 *
 * 使用方式：
 *    mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener() {
 *             @Override
 *             public void onItemClick(View view, int position) {
 *
 *             }
 *         });
 */
public abstract class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
    private GestureDetector mGestureDetector;

    public RecyclerItemClickListener(Context context) {
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View childView = rv.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && mGestureDetector.onTouchEvent(e)) {
            int position = rv.getChildAdapterPosition(childView);
            if (position != RecyclerView.NO_POSITION) {
                onItemClick(childView, position);
            }
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    public abstract void onItemClick(View view, int position);
}