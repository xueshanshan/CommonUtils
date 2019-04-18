package com.star.commonutils.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.star.common_utils.widget.VerificationCodeView;
import com.star.commonutils.R;

/**
 * @author xueshanshan
 * @date 2019/3/6
 */
public class CustomViewFragment extends BaseFragment {

    public static CustomViewFragment getInstance() {
        return new CustomViewFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final VerificationCodeView verificationCodeView = view.findViewById(R.id.verify_code_view);
        verificationCodeView.setVerificationCodeCallBack(new VerificationCodeView.VerificationCodeCallBack() {
            @Override
            public void onInputEnd(String inputCode) {
                verificationCodeView.inputError();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_custom_view;
    }

    @Override
    protected boolean enableSwipeBack() {
        return false;
    }
}
