package com.star.commonutils.fragments;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.star.common_utils.widget.SwipeBackLayout;
import com.star.commonutils.R;

/**
 * @author xueshanshan
 * @date 2018/12/28
 */
public abstract class BaseFragment extends Fragment implements SwipeBackLayout.DismissCallback {
    protected Activity mActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        if (enableSwipeBack()) {
            view = installSwipe(view);
            view.setClickable(true);
        }
        return view;
    }

    protected abstract int getLayoutId();

    private View installSwipe(View content) {
        SwipeBackLayout swipeBackLayout = new SwipeBackLayout(getContext());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        swipeBackLayout.setLayoutParams(params);
        swipeBackLayout.setBackgroundColor(Color.TRANSPARENT);
        swipeBackLayout.attach(this, content);
        swipeBackLayout.enableGesture(enableSwipeBack());
        return swipeBackLayout;
    }

    protected boolean enableSwipeBack() {
        return true;
    }

    /**
     * fragment启动另外的fragment
     *
     * @param fragment  fragment对象
     * @param container fragment放置在的view的id
     */
    protected void launch(BaseFragment fragment, int container) {
        if (getActivity() == null) {
            return;
        }
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.common_utils_activity_open_enter, 0, 0, R.anim.common_utils_activity_close_exit)
                .add(container, fragment)
                .addToBackStack(null)  //这个虽然传的参数是null 但还是表示要添加到任务Fragment回退栈中，不调用该方法就不会添加到回退栈中
                .commit();
    }

    @Override
    public void onDismiss() {
        if (!isDetached()) {
            // 当滑动到满足滑动关闭阈值的地方，无论是否松手，此时同时又点击了物理back键，
            // 就会导致先后两次pop，back先pop，导致后一次pop即这里的getFragmentManager为null
            if (getFragmentManager() != null) {
                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    try {
                        getFragmentManager().popBackStackImmediate();
                    } catch (Throwable ignore) {
                    }
                } else {
                    onBackPressed();
                }
            }
        }
    }

    protected final void onBackPressed() {
        if (getActivity() != null) {
            getActivity().onBackPressed();
        }
    }
}
