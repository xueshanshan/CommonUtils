package com.star.commonutils.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.star.common_utils.utils.ImageUtil;
import com.star.common_utils.utils.StorageUtil;
import com.star.common_utils.widget.EditImageView;
import com.star.commonutils.R;
import com.star.xpermission.OnPermissionCallback;
import com.star.xpermission.XPermission;

import static com.star.xpermission.PermissionSparseArray.PERMISSION_STORAGE;

/**
 * @author xueshanshan
 * @date 2018/12/28
 */
public class EditImageFragment extends BaseFragment implements View.OnClickListener, OnPermissionCallback {

    private EditImageView mEditImageView;

    public static EditImageFragment getInstance() {
        return new EditImageFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_edit_image;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mEditImageView = view.findViewById(R.id.edit_img_view);
        mEditImageView.init(ImageUtil.decodeResource(getContext(), R.drawable.banner, 650, 464));
        view.findViewById(R.id.tv_rotate).setOnClickListener(this);
        view.findViewById(R.id.tv_save).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_rotate:
                mEditImageView.rotateImg();
                break;
            case R.id.tv_save:
                XPermission.permissionRequest(this, PERMISSION_STORAGE, this);
                break;
        }
    }

    @Override
    protected boolean enableSwipeBack() {
        return false;
    }

    @Override
    public void onPermissionGranted(int reqCode) {
        boolean saveBitmap = mEditImageView.saveBitmap(StorageUtil.ROOT_DIR_PIC);
        Toast.makeText(getContext(), saveBitmap ? "保存成功" : "保存失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPermissionDenied(String deniedPermission, int reqCode) {
        XPermission.showTipDialog(mActivity, "需要存储权限", "没有存储权限可能导致存储图片失败");
    }

    @Override
    public void shouldShowRequestPermissionTip(String requestPermissionRationale, int reqCode) {
        XPermission.showTipDialog(mActivity, "需要存储权限", "没有存储权限可能导致存储图片失败");
    }
}
