package com.star.commonutils.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.star.commonutils.R;

/**
 * @author xueshanshan
 * @date 2018/12/29
 */
public class SwipeFirstFragment extends BaseFragment implements View.OnClickListener {

    public static SwipeFirstFragment getInstance() {
        return new SwipeFirstFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_swiper_first;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.to_next).setOnClickListener(this);
    }

    @Override
    protected boolean enableSwipeBack() {
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.to_next:
                launch(SwipeSecondFragment.getInstance(), R.id.fragment_container);
                break;
        }
    }
}
