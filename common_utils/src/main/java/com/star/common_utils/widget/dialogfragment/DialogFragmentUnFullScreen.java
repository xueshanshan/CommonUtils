package com.star.common_utils.widget.dialogfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;

import com.star.common_utils.R;
import com.star.common_utils.utils.AppUtil;

/**
 * @author xueshanshan
 * @date 2018/9/7
 */
public class DialogFragmentUnFullScreen extends DialogFragmentFullScreen {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(Window.FEATURE_NO_TITLE, R.style.DialogFragmentUnFullScreen);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setLayout(getWidth(), WindowManager.LayoutParams.WRAP_CONTENT);  //必须手动指定宽高，布局写死100dp也是不起作用的
        }
    }

    protected int getWidth() {
        return AppUtil.dp2px(mContext, 280);
    }
}
