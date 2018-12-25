package com.star.commonutils.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.star.common_utils.widget.WheelViewPager;
import com.star.commonutils.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author xueshanshan
 * @date 2018/12/17
 */
public class BannersActivity extends BaseActivity implements WheelViewPager.WheelPagerAdapterCallback, View.OnClickListener {

    private List<Integer> mDatas;
    private WheelViewPager<Integer> mViewPager;
    private Random mRandom = new Random();
    private LayoutInflater mLayoutInflater;

    public static Intent makeIntent(Context context) {
        return new Intent(context, BannersActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banners);
        mLayoutInflater = LayoutInflater.from(this);
        mViewPager = findViewById(R.id.banner_view_pager);
        mDatas = new ArrayList<>();
        mDatas.add(getRandomColor());
        mDatas.add(getRandomColor());
        mDatas.add(getRandomColor());
        mDatas.add(getRandomColor());
        mDatas.add(getRandomColor());
        mViewPager.setAdapter(this);
        mViewPager.setData(mDatas);
        findViewById(R.id.tv_reset_equal_count).setOnClickListener(this);
        findViewById(R.id.tv_add).setOnClickListener(this);
        findViewById(R.id.tv_reduce).setOnClickListener(this);
    }

    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = mLayoutInflater.inflate(R.layout.item_banners, container, false);
        view.setBackgroundColor(mDatas.get(position));
        TextView tv = view.findViewById(R.id.item_banner_text);
        tv.setText(position + "");
        container.addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mViewPager.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mViewPager.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_reset_equal_count:
                mDatas.set(0, getRandomColor());
                mViewPager.setData(mDatas);
                Toast.makeText(this, "已经重设", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_add:
                mDatas.add(getRandomColor());
                mViewPager.setData(mDatas);
                break;
            case R.id.tv_reduce:
                if (mDatas.size() > 1) {
                    mDatas.remove(0);
                    mViewPager.setData(mDatas);
                    break;
                } else {
                    Toast.makeText(this, "不能再减少了", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private int getRandomColor() {
        int red = mRandom.nextInt(256);
        int green = mRandom.nextInt(256);
        int blue = mRandom.nextInt(256);
        return Color.argb(255, red, green, blue);
    }
}
