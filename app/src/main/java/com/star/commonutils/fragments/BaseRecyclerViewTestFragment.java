package com.star.commonutils.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.star.common_utils.utils.AppUtil;
import com.star.common_utils.widget.recyclerview.BaseRecyclerView;
import com.star.common_utils.widget.recyclerview.BaseRecyclerViewHolder;
import com.star.common_utils.widget.recyclerview.ItemTouchHelperListener;
import com.star.common_utils.widget.recyclerview.OnBaseRecyclerViewListener;
import com.star.common_utils.widget.recyclerview.RecyclerBottomDividerDecoration;
import com.star.common_utils.widget.recyclerview.RecyclerGridSpaceDecoration;
import com.star.common_utils.widget.recyclerview.SimpleItemTouchHelperCallback;
import com.star.commonutils.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xueshanshan
 * @date 2019/4/17
 */
public class BaseRecyclerViewTestFragment extends BaseFragment implements OnBaseRecyclerViewListener<String>, View.OnClickListener, ItemTouchHelperListener {

    private BaseRecyclerView<String> mBaseRecyclerView;
    private List<String> mStrings;
    private boolean mGrid;
    private RecyclerView.ViewHolder dragHolder;
    private Animation shake;

    public static BaseRecyclerViewTestFragment getInstance(boolean grid) {
        BaseRecyclerViewTestFragment fragment = new BaseRecyclerViewTestFragment();
        fragment.mGrid = grid;
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBaseRecyclerView = view.findViewById(R.id.base_recycler_view);
        mStrings = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            mStrings.add("条目" + i);
        }
        //添加Header
        View header = LayoutInflater.from(getContext()).inflate(R.layout.header_base_recycler_view, (ViewGroup) view, false);
        TextView textView = header.findViewById(R.id.tv_text);
        textView.setText("这是Header");
        mBaseRecyclerView.setHeaderView(header);

        //添加Footer
        View footer = LayoutInflater.from(getContext()).inflate(R.layout.header_base_recycler_view, (ViewGroup) view, false);
        TextView textView2 = footer.findViewById(R.id.tv_text);
        textView2.setText("这是Footer");
        mBaseRecyclerView.setFooterView(footer);


        if (mGrid) {
            mBaseRecyclerView.setManagerSpanCount(3);
            mBaseRecyclerView.addItemDecoration(new RecyclerGridSpaceDecoration(AppUtil.dp2px(getContext(), 10), 3, true, true, false));
//            mBaseRecyclerView.addItemDecoration(new RecyclerGridDividerDecoration(mBaseRecyclerView, Color.GREEN, 1, true, true, 3));
        } else {
            mBaseRecyclerView.addItemDecoration(new RecyclerBottomDividerDecoration(mBaseRecyclerView, Color.GREEN, 1, true, true));
        }

        //添加拖拽更换位置
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(this);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(mBaseRecyclerView);

        //设置数据源和监听
        mBaseRecyclerView.setDatas(mStrings);
        mBaseRecyclerView.setOnBaseRecyclerViewListener(this, true);


        view.findViewById(R.id.tv_add).setOnClickListener(this);
    }

    private void addData() {
        List<String> datas = new ArrayList<>();
        int size = mStrings.size();
        for (int i = 0; i < 5; i++) {
            datas.add("条目" + (size + i));
        }
        mBaseRecyclerView.addDatas(datas);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_base_recycler_view_test;
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_base_reycler_view;
    }


    @Override
    public void onBindView(BaseRecyclerViewHolder holder, String item, int position) {
        holder.getTextView(R.id.item_tv).setText(item);
    }

    @Override
    public void onItemClick(View view, String item) {
        Toast.makeText(getContext(), "将要移除 " + item, Toast.LENGTH_SHORT).show();
        mBaseRecyclerView.removeData(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_add:
                addData();
                break;
        }
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    @Override
    public boolean onItemMove(RecyclerView.ViewHolder fromViewHolder, RecyclerView.ViewHolder toViewHolder, int fromPosition, int toPosition) {
        //这句话只是更改了UI位置 但是数据源并没有变
        mBaseRecyclerView.getBaseRecyclerAdapter().notifyItemMoved(fromPosition, toPosition);
        //数据源要做改动 将移动的移除 然后add到toPosition
        // 这里不能调用封装的mBaseRecyclerView.removeData 因为上面已经做过动画了，该方法里面也调用了动画  会导致视图错乱 只需要偷偷的改变数据源就行了
        String removeData = mBaseRecyclerView.getDatas().remove(mBaseRecyclerView.getRealDataPosition(fromPosition));
        mBaseRecyclerView.getDatas().add(mBaseRecyclerView.getRealDataPosition(toPosition), removeData);
        return false;
    }

    @Override
    public void onItemSelected(RecyclerView.ViewHolder holder, boolean selected) {
        if (selected) {
            dragHolder = holder;
            shake = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
            holder.itemView.startAnimation(shake);
        } else {
            if (dragHolder != null) {
                dragHolder.itemView.clearAnimation();
            }
        }
    }

    @Override
    public void onItemClear(RecyclerView.ViewHolder holder) {

    }


    @Override
    public void onItemDismiss(int position) {

    }
}
