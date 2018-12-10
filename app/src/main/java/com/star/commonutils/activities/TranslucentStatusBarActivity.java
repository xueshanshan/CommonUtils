package com.star.commonutils.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.star.commonutils.R;

/**
 * @author xueshanshan
 * @date 2018/12/10
 */
public class TranslucentStatusBarActivity extends BaseActivity {

    public static Intent getActivityIntent(Context context) {
        return new Intent(context, TranslucentStatusBarActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translucent_status_bar);
    }

    @Override
    protected boolean needSetStatusBarColor() {
        return false;
    }

    @Override
    protected boolean needTranslucentStatusBar() {
        return true;
    }
}
