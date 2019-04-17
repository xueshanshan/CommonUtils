package com.star.commonutils.activities;

import android.os.Bundle;
import android.view.View;

import com.star.commonutils.R;
import com.star.commonutils.retention_defs.DispatcherType;


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
        }
    }
}
