package com.star.common_utils.widget.recyclerview;

import android.view.View;

/**
 * @author xueshanshan
 * @date 2019/4/17
 */
public interface OnBaseRecyclerViewListener<Model> {
    int getItemLayoutId();

//    void onViewHolderCreated(BaseRecyclerViewHolder holder);

    void onBindView(BaseRecyclerViewHolder holder, Model item, int position);

    void onItemClick(View view, Model item);
}
