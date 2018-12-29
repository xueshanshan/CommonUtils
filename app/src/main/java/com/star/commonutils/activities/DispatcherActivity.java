package com.star.commonutils.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.star.commonutils.R;
import com.star.commonutils.fragments.EditImageFragment;
import com.star.commonutils.fragments.SwipeFirstFragment;

/**
 * @author xueshanshan
 * @date 2018/12/10
 *
 * 为了展示一个Activity嵌套多个fragment进行调度
 */
public class DispatcherActivity extends BaseActivity {
    public static final String DISPATCHER = "dispatcher";
    public static final String DISPATCH_EDIT_IMAGE = "edit_img";
    public static final String DISPATCH_SWIPE_DELETE = "swipe_delete";
    private String mType;

    public static Intent makeIntent(Context context, String dispatcher) {
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispatcher);
        if (savedInstanceState != null) {
            finish();
            return;
        }
        mType = getIntent().getStringExtra(DISPATCHER);
        dispatch();
    }

    private void dispatch() {
        switch (mType) {
            case DISPATCH_EDIT_IMAGE:
                launch(EditImageFragment.getInstance(), R.id.fragment_container);
                break;
            case DISPATCH_SWIPE_DELETE:
                launch(SwipeFirstFragment.getInstance(), R.id.fragment_container);
                break;
        }
    }
}
