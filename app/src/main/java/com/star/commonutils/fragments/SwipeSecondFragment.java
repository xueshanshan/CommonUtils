package com.star.commonutils.fragments;

import com.star.commonutils.R;

/**
 * @author xueshanshan
 * @date 2018/12/29
 */
public class SwipeSecondFragment extends BaseFragment {

    public static SwipeSecondFragment getInstance() {
        return new SwipeSecondFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_swiper_second;
    }
}
