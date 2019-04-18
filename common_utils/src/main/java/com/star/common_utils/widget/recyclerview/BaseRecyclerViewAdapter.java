package com.star.common_utils.widget.recyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.star.common_utils.R;
import com.star.common_utils.utils.UIUtil;

/**
 * @author xueshanshan
 * @date 2019/4/18
 */
public class BaseRecyclerViewAdapter<Model> extends RecyclerView.Adapter<BaseRecyclerViewHolder> implements View.OnClickListener {

    // item 的三种类型
    public static final int ITEM_TYPE_NORMAL = 1; // 正常的item类型
    protected static final int ITEM_TYPE_HEADER = 2; // header
    protected static final int ITEM_TYPE_FOOTER = 3; // footer

    private Context mContext;
    private BaseRecyclerView<Model> mBaseRecyclerView;
    private OnBaseRecyclerViewListener<Model> mOnBaseRecyclerViewListener;
    private boolean mNeedItemClick;

    private View mHeaderView;
    private View mFooterView;

    BaseRecyclerViewAdapter(Context context, BaseRecyclerView<Model> baseRecyclerView) {
        mContext = context;
        mBaseRecyclerView = baseRecyclerView;
    }


    void setOnBaseRecyclerViewListener(OnBaseRecyclerViewListener<Model> onBaseRecyclerViewListener, boolean needItemClick) {
        mOnBaseRecyclerViewListener = onBaseRecyclerViewListener;
        mNeedItemClick = needItemClick;
    }

    void setHeaderView(View header) {
        mHeaderView = header;
        notifyDataSetChanged();
    }

    void setFooterView(View footer) {
        mFooterView = footer;
        notifyDataSetChanged();
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_HEADER) {
            UIUtil.removeViewFromParent(mHeaderView);
            return new BaseRecyclerViewHolder(mHeaderView);
        }
        if (viewType == ITEM_TYPE_FOOTER) {
            UIUtil.removeViewFromParent(mFooterView);
            return new BaseRecyclerViewHolder(mFooterView);
        }

        BaseRecyclerViewHolder holder = null;
        if (mOnBaseRecyclerViewListener != null) {
            View itemView = LayoutInflater.from(mContext).inflate(mOnBaseRecyclerViewListener.getItemLayoutId(), parent, false);
            holder = new BaseRecyclerViewHolder(itemView);
//            mOnBaseRecyclerViewListener.onViewHolderCreated(holder);

            if (mNeedItemClick) {
                holder.itemView.setOnClickListener(this);
            }

        }
        return holder;
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {
        int type = getItemViewType(position);
        if (type == ITEM_TYPE_HEADER || type == ITEM_TYPE_FOOTER) {
            // 不做处理
        } else {
            position -= mHeaderView != null ? 1 : 0;
            Model item = mBaseRecyclerView.getDatas().get(position);
            holder.itemView.setTag(R.id.recycler_item_tag, item);
            if (mOnBaseRecyclerViewListener != null) {
                mOnBaseRecyclerViewListener.onBindView(holder, item, position);
            }
        }
    }

    @Override
    public int getItemCount() {
        int size = mBaseRecyclerView.getDatas().size();
        if (mHeaderView != null) {
            size++;
        }
        if (mFooterView != null) {
            size++;
        }
        return size;
    }

    @Override
    public int getItemViewType(int position) {
        // 根据索引获取当前View的类型，以达到复用的目的
        // 根据位置的索引，获取当前position的类型
        if (mHeaderView != null && position == 0) {
            return ITEM_TYPE_HEADER;
        }
        if (mHeaderView != null && mFooterView != null && position == mBaseRecyclerView.getDatas().size() + 1) {
            // 有头部和底部时，最后底部的应该等于size+!
            return ITEM_TYPE_FOOTER;
        } else if (mHeaderView == null && mFooterView != null && position == mBaseRecyclerView.getDatas().size()) {
            // 没有头部，有底部，底部索引为size
            return ITEM_TYPE_FOOTER;
        }
        return ITEM_TYPE_NORMAL;
    }

    @Override
    public void onClick(View v) {
        if (mOnBaseRecyclerViewListener != null) {
            mOnBaseRecyclerViewListener.onItemClick(v, ((Model) v.getTag(R.id.recycler_item_tag)));
        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull BaseRecyclerViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        int position = holder.getLayoutPosition();
        int itemViewType = getItemViewType(position);
        if (holder.itemView.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams &&
                (itemViewType == ITEM_TYPE_HEADER || itemViewType == ITEM_TYPE_FOOTER)) {
            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) holder
                    .itemView.getLayoutParams();
            params.setFullSpan(true);
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    //占用的单元格数 header 和 Footer占用多个单元格
                    int itemViewType = getItemViewType(position);
                    if (itemViewType == ITEM_TYPE_NORMAL) {
                        return 1;
                    } else {
                        return gridManager.getSpanCount();
                    }
                }
            });
        }
    }
}
