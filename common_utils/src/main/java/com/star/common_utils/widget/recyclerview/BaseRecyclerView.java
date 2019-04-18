package com.star.common_utils.widget.recyclerview;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xueshanshan
 * @date 2019/4/17
 */
public class BaseRecyclerView<Model> extends RecyclerView {
    private Context mContext;
    private GridLayoutManager mGridLayoutManager;
    private BaseRecyclerViewAdapter<Model> mBaseRecyclerAdapter;
    private List<Model> mDatas = new ArrayList<>();
    private boolean mHasHeader;
    private boolean mHasFooter;

    public BaseRecyclerView(Context context) {
        this(context, null);
    }

    public BaseRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        mGridLayoutManager = new GridLayoutManager(context, 1);
        setLayoutManager(mGridLayoutManager);
        mBaseRecyclerAdapter = new BaseRecyclerViewAdapter(mContext, this);
        setAdapter(mBaseRecyclerAdapter);
    }

    /**
     * 设置监听
     *
     * @param onBaseRecyclerViewListener
     */
    public void setOnBaseRecyclerViewListener(OnBaseRecyclerViewListener<Model> onBaseRecyclerViewListener, boolean needItemClick) {
        mBaseRecyclerAdapter.setOnBaseRecyclerViewListener(onBaseRecyclerViewListener, needItemClick);
    }


    /**
     * 设置grid 排列个数
     *
     * @param spanCount
     */
    public void setManagerSpanCount(int spanCount) {
        mGridLayoutManager.setSpanCount(spanCount);
        mBaseRecyclerAdapter.notifyDataSetChanged();
    }

    public GridLayoutManager getGridLayoutManager() {
        return mGridLayoutManager;
    }

    public BaseRecyclerViewAdapter<Model> getBaseRecyclerAdapter() {
        return mBaseRecyclerAdapter;
    }

    public boolean isHasHeader() {
        return mHasHeader;
    }

    public boolean isHasFooter() {
        return mHasFooter;
    }

    public int getRealDataPosition(int position) {
        if (mHasHeader) {
            return position - 1;
        }
        return position;
    }

    /**
     * 设置数据源
     *
     * @param datas
     */
    public void setDatas(List<Model> datas) {
        mDatas = datas;
        mBaseRecyclerAdapter.notifyDataSetChanged();
    }

    /**
     * 获取数据源
     *
     * @return
     */
    public List<Model> getDatas() {
        return mDatas;
    }

    /**
     * 添加单条数据
     *
     * @param data
     */
    public void addData(Model data) {
        if (data != null) {
            getDatas().add(data);
            int size = getDatas().size();
            mBaseRecyclerAdapter.notifyItemInserted(mHasHeader ? size + 1 : size);
        }
    }

    /**
     * 根据位置添加数据
     *
     * @param pos  pos为数据源实际的位置
     * @param data
     */
    public void addData(int pos, Model data) {
        int size = getDatas().size();
        if (data != null && pos >= 0 && pos <= size) {
            getDatas().add(pos, data);
            mBaseRecyclerAdapter.notifyItemInserted(mHasHeader ? pos + 1 : pos);
        }
    }

    /**
     * 添加多条数据
     *
     * @param datas
     */
    public void addDatas(List<Model> datas) {
        int size = getDatas().size();
        mDatas.addAll(datas);
        mBaseRecyclerAdapter.notifyItemRangeInserted(mHasHeader ? size + 1 : size, datas.size());
    }

    /**
     * 移除单条数据
     *
     * @param data
     */
    public Model removeData(Model data) {
        int index = -1;
        if (data != null && (index = getDatas().indexOf(data)) != -1) {
            mBaseRecyclerAdapter.notifyItemRemoved(mHasHeader ? index + 1 : index);
            return getDatas().remove(index);
        }
        return null;
    }

    /**
     * 根据index移除单条数据
     *
     * @param index index为数据源实际的index
     */
    public Model removeData(int index) {
        if (index >= 0 && index < getDatas().size()) {
            mBaseRecyclerAdapter.notifyItemRemoved(mHasHeader ? index + 1 : index);
            return getDatas().remove(index);
        }
        return null;
    }

    /**
     * 设置header
     *
     * @param header
     */
    public void setHeaderView(View header) {
        mHasHeader = true;
        mBaseRecyclerAdapter.setHeaderView(header);
    }

    /**
     * 设置footer
     *
     * @param footer
     */
    public void setFooterView(View footer) {
        mHasFooter = true;
        mBaseRecyclerAdapter.setFooterView(footer);
    }
}
