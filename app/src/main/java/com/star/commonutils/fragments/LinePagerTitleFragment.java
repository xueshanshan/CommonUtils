package com.star.commonutils.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.star.common_utils.widget.LinePagerTitleView;
import com.star.commonutils.R;
import com.star.common_utils.adapter.CommonFragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xueshanshan
 * @date 2019/4/17
 */
public class LinePagerTitleFragment extends BaseFragment {

    public static LinePagerTitleFragment getInstance() {
        return new LinePagerTitleFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_line_pager_title;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinePagerTitleView<String> linePagerTitleView = view.findViewById(R.id.line_pager_title_view);
        final List<String> titles = new ArrayList<>();
        titles.add("RecyclerView封装测试");
        titles.add("图片裁剪");
        ViewPager pager = view.findViewById(R.id.view_pager);
        CommonFragmentPagerAdapter adapter = new CommonFragmentPagerAdapter(getChildFragmentManager()) {
            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return titles.get(position);
            }
        };
        adapter.addFragment(BaseRecyclerViewTestFragment.getInstance());
        adapter.addFragment(EditImageFragment.getInstance());
        pager.setAdapter(adapter);
        linePagerTitleView.setViewPager(pager);
        linePagerTitleView.setObjectList(titles);
    }

    @Override
    protected boolean enableSwipeBack() {
        return false;
    }
}
