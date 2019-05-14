package com.star.commonutils.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.star.common_utils.utils.AppUtil;
import com.star.common_utils.utils.ShapeUtil;
import com.star.common_utils.widget.dialogfragment.DialogFragmentFullScreen;
import com.star.common_utils.widget.dialogfragment.DialogFragmentUnFullScreen;
import com.star.commonutils.R;

/**
 * @author xueshanshan
 * @date 2019-05-14
 */
public class MyDialogFragmentUnFullScreen extends DialogFragmentUnFullScreen {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_un_full_screen_my, container, false);
        View dialogContent = view.findViewById(R.id.dialog_content);
        dialogContent.setBackground(ShapeUtil.getRoundRectDrawable(AppUtil.dp2px(mContext, 10), Color.WHITE, true, 0));
        return view;
    }
}
