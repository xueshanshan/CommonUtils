package com.star.common_utils.widget.dialogfragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.Window;

import com.star.common_utils.R;

/**
 * @author xueshanshan
 * @date 2018/9/7
 */
public class DialogFragmentFullScreen extends DialogFragment implements DialogInterface.OnKeyListener {
    protected Context mContext;
    protected Resources mResources;
    protected DialogFragmentDismissListener mDialogFragmentDismissListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        mResources = getResources();
        setStyle(Window.FEATURE_NO_TITLE, R.style.DialogFragmentFullScreen);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().setOnKeyListener(this);
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        // 如果需要动画或处理返回键操作需要重写dismiss方法
        if (isCancelable() && keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            dismissAllowingStateLoss();
            return true;
        }
        return false;
    }


    public void show(FragmentManager fragmentManager) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(this, this.getClass().getSimpleName());
        transaction.commitAllowingStateLoss();
    }

    public void setDialogFragmentDismissListener(DialogFragmentDismissListener dialogFragmentDismissListener) {
        mDialogFragmentDismissListener = dialogFragmentDismissListener;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mDialogFragmentDismissListener != null) {
            mDialogFragmentDismissListener.onDialogFragmentDismiss();
        }
    }
}
