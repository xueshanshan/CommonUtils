package com.star.commonutils.activities;

import android.content.Context;
import android.content.Intent;

/**
 * @author xueshanshan
 * @date 2018/12/10
 *
 * 为了展示一个Activity嵌套多个fragment进行调度
 */
public class DispatcherActivity extends BaseActivity {

    public static Intent getActivityIntent(Context context) {
        return new Intent(context, DispatcherActivity.class);
    }
}
