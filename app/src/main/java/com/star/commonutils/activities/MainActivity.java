package com.star.commonutils.activities;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.star.common_utils.utils.AppUtil;
import com.star.common_utils.utils.LogUtil;
import com.star.commonutils.R;
import com.star.commonutils.retention_defs.DispatcherType;
import com.star.xpermission.OnPermissionCallback;
import com.star.xpermission.PermissionSparseArray;
import com.star.xpermission.XPermission;

import java.io.File;


public class MainActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_translucent_status).setOnClickListener(this);
        findViewById(R.id.btn_wheel_banner).setOnClickListener(this);
        findViewById(R.id.edit_img).setOnClickListener(this);
        findViewById(R.id.swipe_delete).setOnClickListener(this);
        findViewById(R.id.custom_view).setOnClickListener(this);
        findViewById(R.id.line_pager_title).setOnClickListener(this);
        findViewById(R.id.app_install).setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_translucent_status:
                startActivity(TranslucentStatusBarActivity.makeIntent(this));
                break;
            case R.id.btn_wheel_banner:
                startActivity(BannersActivity.makeIntent(this));
                break;
            case R.id.edit_img:
                startActivity(DispatcherActivity.makeIntent(this, DispatcherType.DISPATCH_EDIT_IMAGE));
                break;
            case R.id.swipe_delete:
                startActivity(DispatcherActivity.makeIntent(this, DispatcherType.DISPATCH_SWIPE_DELETE));
                break;
            case R.id.custom_view:
                startActivity(DispatcherActivity.makeIntent(this, DispatcherType.DISPATCH_CUSTOM_VIEW));
                break;
            case R.id.line_pager_title:
                startActivity(DispatcherActivity.makeIntent(this, DispatcherType.DISPATCH_LINE_PAGER_TITLE));
                break;
            case R.id.app_install:
                XPermission.permissionRequest(this, PermissionSparseArray.PERMISSION_STORAGE, new OnPermissionCallback() {
                    @Override
                    public void onPermissionGranted(int reqCode) {
                        AppUtil.installApk(MainActivity.this, new File(Environment.getExternalStorageDirectory(), "test.apk"), "com.star.commonutils.fileprovider");
                    }

                    @Override
                    public void onPermissionDenied(String deniedPermission, int reqCode) {
                        XPermission.showTipDialog(MainActivity.this, "请授予权限", "该权限读取sd卡内容");
                    }

                    @Override
                    public void shouldShowRequestPermissionTip(String requestPermissionRationale, int reqCode) {
                        XPermission.showTipDialog(MainActivity.this, "请授予权限", "该权限读取sd卡内容");
                    }
                });
                break;
        }
    }

    @Override
    protected boolean enableSwipeBack() {
        return false;
    }
}
