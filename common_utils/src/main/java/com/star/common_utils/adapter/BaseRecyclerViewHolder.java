package com.star.common_utils.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xueshanshan
 * @date 2019/4/18
 */
public class BaseRecyclerViewHolder extends RecyclerView.ViewHolder {

    private Map<Integer, View> mViewMap;

    BaseRecyclerViewHolder(View itemView) {
        super(itemView);
        mViewMap = new HashMap<>();
    }

    /**
     * 根据id获取布局上的view
     */
    public <T extends View> T getView(int id) {
        View view = mViewMap.get(id);
        if (view == null) {
            view = itemView.findViewById(id);
            mViewMap.put(id, view);
        }
        return (T) view;
    }

    public TextView getTextView(int id) {
        return getView(id);
    }

    public TextView getImageView(int id) {
        return getView(id);
    }
}
