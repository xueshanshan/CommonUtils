package com.star.common_utils.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;

import com.star.common_utils.R;


public class AnimZoomLinearLayout extends LinearLayout implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {
    private ValueAnimator mAnimatorDown;
    private ValueAnimator mAnimatorUp;
    private AnimatorZoomListener mOnAnimatorZoomListener;
    private boolean mUp;

    public AnimZoomLinearLayout(Context context) {
        this(context, null);
    }

    public AnimZoomLinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimZoomLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AnimZoomLinearLayout);
        float zoomScale = typedArray.getFloat(R.styleable.AnimZoomLinearLayout_zoom_scale, 0.96f);
        int downDuration = typedArray.getInt(R.styleable.AnimZoomLinearLayout_duration_down, 200);
        int upDuration = typedArray.getInt(R.styleable.AnimZoomLinearLayout_duration_up, 100);
        typedArray.recycle();

        mAnimatorDown = ValueAnimator.ofFloat(1f, zoomScale);
        mAnimatorDown.addUpdateListener(this);
        mAnimatorDown.setDuration(downDuration);
        mAnimatorDown.setInterpolator(new AccelerateInterpolator());
        mAnimatorDown.addListener(this);

        mAnimatorUp = ValueAnimator.ofFloat(zoomScale, 1);
        mAnimatorUp.addUpdateListener(this);
        mAnimatorUp.setDuration(upDuration);
        mAnimatorUp.setInterpolator(new AccelerateInterpolator());
        mAnimatorUp.addListener(this);
    }

    public void setOnAnimatorZoomListener(AnimatorZoomListener onAnimatorZoomListener) {
        mOnAnimatorZoomListener = onAnimatorZoomListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mUp = false;
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mAnimatorDown.start();
            //返回true 如果子view没有处理事件 则该view进行处理 为了能够接收到后续事件
            return true;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            mUp = true;
            if (!mAnimatorDown.isRunning()) {
                mAnimatorUp.start();
            }
        } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
            mAnimatorUp.start();
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        float value = (float) animation.getAnimatedValue();
        setScaleX(value);
        setScaleY(value);
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        if (animation == mAnimatorDown && mUp) {
            mAnimatorUp.start();
        }
        if (mOnAnimatorZoomListener != null && animation == mAnimatorUp && mUp) {
            mOnAnimatorZoomListener.onUp(this);
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}
