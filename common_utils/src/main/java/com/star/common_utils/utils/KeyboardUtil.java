package com.star.common_utils.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * @author xueshanshan
 * @date 2018/12/10
 */
public class KeyboardUtil {
    /**
     * 打开软键盘
     *
     * @param v 对应的editText
     */
    public static void showKeyBoard(final EditText v) {
        if (v == null) return;
        v.requestFocus();
        v.post(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });
    }

    /**
     * 关闭软键盘
     *
     * @param v 对应的view
     */
    public static void closeKeyBoard(View v) {
        if (v == null) return;
        v.clearFocus();
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    /**
     * 关闭软键盘
     *
     * @param act 对应的Activity
     */
    public static void closeKeyBoard(Activity act) {
        if (act == null || act.getCurrentFocus() == null) return;
        InputMethodManager imm = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(act.getCurrentFocus().getWindowToken(), 0);
        }
    }
}
