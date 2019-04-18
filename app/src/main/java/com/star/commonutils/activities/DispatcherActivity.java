package com.star.commonutils.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.star.commonutils.R;
import com.star.commonutils.fragments.EditImageFragment;
import com.star.commonutils.fragments.LinePagerTitleFragment;
import com.star.commonutils.fragments.SwipeFirstFragment;
import com.star.commonutils.fragments.CustomViewFragment;
import com.star.commonutils.retention_defs.DispatcherType;

/**
 * @author xueshanshan
 * @date 2018/12/10
 *
 * 为了展示一个Activity嵌套多个fragment进行调度
 */
public class DispatcherActivity extends BaseActivity {
    public static final String DISPATCHER = "dispatcher";
    private String mType;

    public static Intent makeIntent(Context context, @DispatcherType String dispatcher) {
        Intent intent = new Intent(context, DispatcherActivity.class);
        intent.putExtra(DISPATCHER, dispatcher);
        return intent;
    }

    @Override
    protected boolean needSetStatusBarColor() {
        return false;
    }

    @Override
    protected boolean needTranslucentStatusBar() {
        return true;
    }

    @Override
    protected boolean needStatusBarBlackText() {
        if (DispatcherType.DISPATCH_CUSTOM_VIEW.equals(mType) || DispatcherType.DISPATCH_LINE_PAGER_TITLE.equals(mType)) {
            return true;
        }
        return super.needStatusBarBlackText();
    }

    @Override
    protected boolean enableSwipeBack() {
        if (mType.equals(DispatcherType.DISPATCH_SWIPE_DELETE)) {
            return false;
        }
        return super.enableSwipeBack();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mType = getIntent().getStringExtra(DISPATCHER);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispatcher);
        if (savedInstanceState != null) {
            finish();
            return;
        }
        dispatch();
    }

    private void dispatch() {
        switch (mType) {
            case DispatcherType.DISPATCH_EDIT_IMAGE:
                launch(EditImageFragment.getInstance(), R.id.fragment_container);
                break;
            case DispatcherType.DISPATCH_SWIPE_DELETE:
                launch(SwipeFirstFragment.getInstance(), R.id.fragment_container);
                break;
            case DispatcherType.DISPATCH_CUSTOM_VIEW:
                launch(CustomViewFragment.getInstance(), R.id.fragment_container);
                break;
            case DispatcherType.DISPATCH_LINE_PAGER_TITLE:
                launch(LinePagerTitleFragment.getInstance(), R.id.fragment_container);
                break;
        }
    }
}
