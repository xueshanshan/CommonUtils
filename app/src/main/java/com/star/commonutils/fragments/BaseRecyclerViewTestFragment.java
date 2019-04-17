package com.star.commonutils.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.star.common_utils.adapter.BaseRecyclerView;
import com.star.common_utils.adapter.BaseRecyclerViewHolder;
import com.star.common_utils.adapter.OnBaseRecyclerViewListener;
import com.star.commonutils.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xueshanshan
 * @date 2019/4/17
 */
public class BaseRecyclerViewTestFragment extends BaseFragment implements OnBaseRecyclerViewListener<String>, View.OnClickListener {

    private BaseRecyclerView<String> mBaseRecyclerView;
    private List<String> mStrings;

    public static BaseRecyclerViewTestFragment getInstance() {
        return new BaseRecyclerViewTestFragment();
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
    public void bindView(BaseRecyclerViewHolder holder, String item, int position) {
        TextView tv = (TextView) holder.getView(R.id.item_tv);
        tv.setText(item);
    }

    @Override
    public void onItemClick(View view, String item) {
        Toast.makeText(getContext(), "将要移除 " + item, Toast.LENGTH_SHORT).show();
        mBaseRecyclerView.remoteData(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_add:
                addData();
                break;
        }
    }
}
