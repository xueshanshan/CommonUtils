package com.star.common_utils.adapter;

import android.view.View;

/**
 * @author xueshanshan
 * @date 2019/4/17
 */
public interface OnBaseRecyclerViewListener<Model> {
    int getItemLayoutId();

    void bindView(BaseRecyclerViewHolder holder, Model item, int position);

    void onItemClick(View view, Model item);
}
