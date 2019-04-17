package com.star.common_utils.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.star.common_utils.R;
import com.star.common_utils.utils.AppUtil;
import com.star.common_utils.utils.LogUtil;

/**
 * @author xueshanshan
 * @date 2018/12/5
 */
public class TopBar extends RelativeLayout {
    private static final String TAG = TopBar.class.getSimpleName();

    private ImageView mImgLeftIcon;
    private ImageView mImgRightIcon;
    private ImageView mImgTitle;
    private TextView mTvTitle;

    public TopBar(Context context) {
        this(context, null);
    }

    public TopBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TopBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.top_bar, this, true);
        mImgLeftIcon = findViewById(R.id.top_bar_img_left);
        mImgRightIcon = findViewById(R.id.top_bar_img_right);
        mImgTitle = findViewById(R.id.top_bar_img_title);
        mTvTitle = findViewById(R.id.top_bar_tv_title);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TopBar);

        int leftIcon = array.getResourceId(R.styleable.TopBar_leftIcon, 0);
        if (leftIcon != 0) {
            mImgLeftIcon.setImageResource(leftIcon);
        }

        int rightIcon = array.getResourceId(R.styleable.TopBar_rightIcon, 0);
        if (rightIcon != 0) {
            mImgRightIcon.setImageResource(rightIcon);
        }

        String stringTitle = array.getString(R.styleable.TopBar_title);
        int titleImgRes = array.getResourceId(R.styleable.TopBar_title_img, 0);
        if (stringTitle != null) {
            mTvTitle.setVisibility(View.VISIBLE);
            mImgTitle.setVisibility(View.GONE);
            int titleColor = array.getColor(R.styleable.TopBar_title_color, Color.WHITE);
            float titleTextSize = array.getDimension(R.styleable.TopBar_title_text_size, AppUtil.dp2px(context, 18));
            mTvTitle.setTextColor(titleColor);
            mTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize);
            mTvTitle.setText(stringTitle);

            String titleFontAsset = array.getString(R.styleable.TopBar_title_typefaceAsset);
            if (titleFontAsset != null) {
                Typeface tf = Typeface.createFromAsset(context.getAssets(), titleFontAsset);

                if (tf != null) {
                    mTvTitle.setTypeface(tf);
                } else {
                    LogUtil.d(TAG, String.format("Could not create a font from asset: %s", titleFontAsset));
                }
            }
        } else if (titleImgRes != 0) {
            mTvTitle.setVisibility(View.GONE);
            mImgTitle.setVisibility(View.VISIBLE);
            mImgTitle.setImageResource(titleImgRes);
        }
        array.recycle();
    }

    public void setTitle(String title) {
        mTvTitle.setVisibility(View.VISIBLE);
        mImgTitle.setVisibility(View.GONE);
        mTvTitle.setText(title);
    }

    public void setTitle(int imgResource) {
        mTvTitle.setVisibility(View.GONE);
        mImgTitle.setVisibility(View.VISIBLE);
        mImgTitle.setImageResource(imgResource);
    }

    public void setLeftIcon(int imgResource) {
        mImgLeftIcon.setImageResource(imgResource);
    }

    public void setRightIcon(int imgResource) {
        mImgRightIcon.setImageResource(imgResource);
    }

    public ImageView getImgLeftIcon() {
        return mImgLeftIcon;
    }

    public ImageView getImgRightIcon() {
        return mImgRightIcon;
    }

    public ImageView getImgTitle() {
        return mImgTitle;
    }

    public TextView getTvTitle() {
        return mTvTitle;
    }
}
