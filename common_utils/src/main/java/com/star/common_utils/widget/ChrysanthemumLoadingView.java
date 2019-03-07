package com.star.common_utils.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.star.common_utils.R;
import com.star.common_utils.utils.AppUtil;

/**
 * Created by xueshanshan on 2018/1/11.
 */

public class ChrysanthemumLoadingView extends View {
    private Context context;
    private int centerWidth;
    private int centerHeight;

    private int mSegmentWdith;
    private int mSegmentLength;
    private int mSegmentSpace;
    private int mSegmentColor;
    private int mSegmentCount;
    private int animateDuration;
    private int angle;

    private Paint paint;
    private int control;
    private int lastControl;

    private ValueAnimator valueAnimator;

    public ChrysanthemumLoadingView(Context context) {
        this(context, null);
    }

    public ChrysanthemumLoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChrysanthemumLoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ChrysanthemumLoadingView);
        mSegmentLength = typedArray.getDimensionPixelSize(R.styleable.ChrysanthemumLoadingView_seg_length, AppUtil.dp2px(context, 10));
        mSegmentSpace = typedArray.getDimensionPixelSize(R.styleable.ChrysanthemumLoadingView_seg_space, AppUtil.dp2px(context, 15));
        mSegmentWdith = typedArray.getDimensionPixelSize(R.styleable.ChrysanthemumLoadingView_seg_width, AppUtil.dp2px(context, 4));
        mSegmentColor = typedArray.getColor(R.styleable.ChrysanthemumLoadingView_default_color, Color.WHITE);
        mSegmentCount = typedArray.getInteger(R.styleable.ChrysanthemumLoadingView_seg_count, 8);
        animateDuration = typedArray.getInteger(R.styleable.ChrysanthemumLoadingView_animate_duration, 1500);
        if (animateDuration <= 500) {
            animateDuration = 500;
        }
        typedArray.recycle();

        angle = 360 / mSegmentCount;
        control = mSegmentCount;
        lastControl = mSegmentCount;

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setColor(mSegmentColor);
        paint.setStrokeWidth(mSegmentWdith);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width, height;
        if (widthMode != MeasureSpec.EXACTLY || heightMode != MeasureSpec.EXACTLY) {
            width = height = AppUtil.dp2px(context,100);
        } else {
            width = MeasureSpec.getSize(widthMeasureSpec);
            height = MeasureSpec.getSize(heightMeasureSpec);
        }
        centerWidth = width / 2;
        centerHeight = height / 2;
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 1; i <= mSegmentCount; i++) {  //每次都画mSegmentCount个条，每个条的透明度不一样
            canvas.rotate(angle, centerWidth, centerHeight);
            int a = Math.round((i + control) % mSegmentCount * (255 * 1f / mSegmentCount));
            if (a == 0) {
                a = 255;
            }
            paint.setAlpha(a);
            canvas.drawLine(centerWidth, centerHeight - mSegmentSpace, centerWidth, centerHeight - mSegmentSpace - mSegmentLength, paint);
        }
    }

    private void startAnimation() {
        if (valueAnimator != null) {
            return;
        }
        valueAnimator = ValueAnimator.ofInt(mSegmentCount, 0);
        valueAnimator.setDuration(animateDuration);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                control = (int) animation.getAnimatedValue();
                if (control != lastControl) {
                    invalidate();
                }
                lastControl = control;
            }
        });
        valueAnimator.start();
    }

    private void stopAnimation() {
        if (valueAnimator != null) {
            valueAnimator.cancel();
            valueAnimator = null;
            control = mSegmentCount;
        }
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility != View.VISIBLE) {
            stopAnimation();
        } else {
            startAnimation();
        }
    }
}
