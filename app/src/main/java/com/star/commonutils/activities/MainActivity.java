package com.star.commonutils.activities;

import android.os.Bundle;
import android.view.View;

import com.star.commonutils.R;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_translucent_status).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_translucent_status:
                startActivity(TranslucentStatusBarActivity.getActivityIntent(this));
                break;
        }
    }
}
