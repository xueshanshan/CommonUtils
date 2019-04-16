package com.star.commonutils.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.star.common_utils.utils.StatusBarUtil;
import com.star.common_utils.utils.UIUtil;
import com.star.commonutils.R;
import com.star.commonutils.fragments.BaseFragment;

/**
 * @author xueshanshan
 * @date 2018/12/10
 */
public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (needSetStatusBarColor()) {
            StatusBarUtil.setStatusBarColor(getWindow(), getStatusBarColor(), isNeedAddStatusBarView(), needStatusBarBlackText());
        } else if (needTranslucentStatusBar()) {
            StatusBarUtil.translucentStatusBar(getWindow(), needStatusBarBlackText());
        }
    }

    /**
     * 暴露给子类可以重写
     *
     * @return 是否需要设置状态栏颜色
     */
    protected boolean needSetStatusBarColor() {
        return true;
    }

    /**
     * 暴露给子类可以重写
     *
     * @return 返回获取的状态栏颜色
     */
    protected int getStatusBarColor() {
        return UIUtil.getThemeAttr(this, R.attr.colorPrimaryDark);
    }

    /**
     * 暴露给子类可以重写
     *
     * @return 返回在5.0以下是否需要添加状态栏view
     */
    protected boolean isNeedAddStatusBarView() {
        return true;
    }

    /**
     * 暴露给子类可以重写
     *
     * @return 是否需要透明状态栏
     */
    protected boolean needTranslucentStatusBar() {
        return false;
    }


    /**
     * 暴露给子类可以重写
     *
     * @return 状态栏文字是否显示黑色
     */
    protected boolean needStatusBarBlackText() {
        return false;
    }


    /**
     * Activity中添加fragment
     *
     * @param fragment  fragment对象
     * @param container fragment放置在的view的id
     */
    protected void launch(BaseFragment fragment, int container) {
        getSupportFragmentManager().beginTransaction()
                .add(container, fragment)
                .commit();
    }
}
