package com.star.common_utils.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import com.star.common_utils.R;

import java.util.ArrayList;
import java.util.List;

/**
 * viewPager的指示器
 *
 * 使用
 * mIndicatorView.setCount(5, true);
 * mViewPager.addOnPageChangeListener(new IndicatorView.ViewPagerController(mIndicatorView));
 */

public class IndicatorView extends View {
    // 粘性切换
    public static final int TRANSITION_STICK = 1;
    // 跳跃切换
    public static final int TRANSITION_JUMP = 2;
    // 过渡类型
    private int mTransitionType = TRANSITION_STICK;
    // 点的数量
    private int mCount;
    // 点的半径
    private float mRadius = 50;
    // 每个坐标点的间隔
    private float mInterval = 20;
    // 最终高度
    private int mMeasureHeight;
    // 最终宽度
    private int mMeasureWidth;
    // 导航点的画笔
    private Paint mDotPaint;
    // 导航点颜色
    private int mDotColor;
    // 存放每个导航点的信息
    private List<IndicatorDot> mDotList = new ArrayList<>();
    // 当前所在的select
    private int mSelect;
    // 过渡的百分比
    private float mPercent;
    // 过渡的目标
    private int mTargetPosition;
    // 过渡的画笔
    private Paint mTransitPaint;
    // 过渡的颜色
    private int mTransitColor;
    // 不结合viewPager时使用动画过渡
    private ValueAnimator mAnimator;
    // 过渡动画的时间
    private long mTransitDuration;
    // 拉长的时间，0~1
    private float mStretchTime;
    // 收缩的时间，0~1
    private float mContractTime;

    public IndicatorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
        initPaint();
    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.IndicatorView);
        mDotColor = a.getColor(R.styleable.IndicatorView_dotColor, Color.BLACK);
        mTransitColor = a.getColor(R.styleable.IndicatorView_transitColor, Color.BLUE);
        mCount = a.getInteger(R.styleable.IndicatorView_count, 0);
        mRadius = a.getDimension(R.styleable.IndicatorView_radius, 50);
        mInterval = a.getDimension(R.styleable.IndicatorView_interval, 20);
        mTransitDuration = (long) a.getInteger(R.styleable.IndicatorView_transitDuration, 300);
        mStretchTime = a.getFraction(R.styleable.IndicatorView_stretchTime, 1, 1, 0.7f);
        mContractTime = a.getFraction(R.styleable.IndicatorView_contractTime, 1, 1, 0.3f);
        a.recycle();
    }

    private void initPaint() {
        // 画导航点
        mDotPaint = new Paint();
        mDotPaint.setColor(mDotColor);
        mDotPaint.setStyle(Paint.Style.FILL);
        mDotPaint.setAntiAlias(true);
        // 画过渡点
        mTransitPaint = new Paint();
        mTransitPaint.setColor(mTransitColor);
        mTransitPaint.setStyle(Paint.Style.FILL);
        mTransitPaint.setAntiAlias(true);
    }

    public void initAnimator() {
        mAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.setDuration(mTransitDuration);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mPercent = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                mSelect = mTargetPosition;
                mPercent = 0.0f;
                invalidate();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mSelect = mTargetPosition;
                mPercent = 0.0f;
                invalidate();
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 获取最终宽度
        int width = 0;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.UNSPECIFIED) {
            width = (int) getDotDrawWidth();
        } else {
            width = MeasureSpec.getSize(widthMeasureSpec);
        }
        // 获取最终高度
        int height = 0;
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.UNSPECIFIED) {
            height = (int) (mRadius * 2);
        } else {
            height = MeasureSpec.getSize(heightMeasureSpec);
        }
        setMeasuredDimension(width, height);
        // 保存测量的宽高
        mMeasureHeight = getMeasuredHeight();
        mMeasureWidth = getMeasuredWidth();
        // 计算每一个点的位置
        measureDotLocation();
    }

    /**
     * 测量每一个点的位置
     */
    private void measureDotLocation() {
        mDotList.clear();
        float cy = mMeasureHeight / 2.0f;
        float startX = (mMeasureWidth - getDotDrawWidth()) / 2.0f;
        for (int i = 0; i < mCount; i++) {
            float cx = startX + mRadius + (mRadius * 2.0f + mInterval) * i;
            IndicatorDot dot = new IndicatorDot();
            dot.setCx(cx);
            dot.setCy(cy);
            dot.setRadius(mRadius);
            mDotList.add(dot);
        }
    }

    /**
     * 圆点画出来所占的宽度
     */
    private float getDotDrawWidth() {
        return mRadius * 2.0f * mCount + mInterval * (mCount - 1);
    }

    /**
     * 设置目标点，会以动画的形式跳转
     */
    public void setPosition(int position, int type) {
        if (mAnimator != null && mAnimator.isRunning()) {
            mAnimator.cancel();
        }
        mTargetPosition = position;
        if (mTargetPosition >= mCount) {
            mTargetPosition = 0;
        }
        mTransitionType = type;
        startAnim();
    }

    /**
     * 根据百分比设置过渡效果中的某一帧
     */
    public void setScene(int position, float percent, int type) {
        mTargetPosition = position + 1;
        if (mTargetPosition >= mCount) {
            mTargetPosition = 0;
        }
        mSelect = position;
        this.mPercent = percent;
        mTransitionType = type;
        invalidate();
    }

    public void setScene(int position, float percent) {
        setScene(position, percent, TRANSITION_STICK);
    }

    public void startAnim() {
        initAnimator();
        mAnimator.start();
    }

    public void setPosition(int position) {
        setPosition(position, TRANSITION_STICK);
    }

    public int getPosition() {
        return mTargetPosition;
    }

    public int getCount() {
        return mCount;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 画点
        drawDot(canvas);
        // 绘制过渡
        drawTransition(canvas);
    }

    private void drawTransition(Canvas canvas) {
        if (mDotList.isEmpty()) {
            return;
        }
        IndicatorDot selectDot = mDotList.get(mSelect);
        float startX = selectDot.getCx();
        float cy = selectDot.getCy();
        float endX = mDotList.get(mTargetPosition).getCx();
        Path path;
        if (mTransitionType == TRANSITION_STICK) {
            float spaceX = endX - startX;
            float targetX = startX + evaluatorEndX(spaceX);
            startX += evaluatorStartX(spaceX);
            if (startX > targetX) {
                path = getTransitionPath(targetX, startX, cy, selectDot.getRadius());
            } else {
                path = getTransitionPath(startX, targetX, cy, selectDot.getRadius());
            }
        } else {
            float targetX = (endX - startX) * mPercent + startX;
            // 绘制的时候不关注起始点，只绘制目标点
            path = getTransitionPath(targetX, targetX, cy, selectDot.getRadius());
        }
        canvas.drawPath(path, mTransitPaint);
    }

    /**
     * 计算目标点坐标
     */
    private float evaluatorEndX(float space) {
        float v = space / mStretchTime * mPercent;
        if (Math.abs(v) > Math.abs(space)) {
            return space;
        }
        return v;
    }

    /**
     * 计算开始点坐标
     */
    private float evaluatorStartX(float space) {
        if (mPercent < 1.0f - mContractTime) {
            return 0.0f;
        }
        return space / mContractTime * (mPercent - (1.0f - mContractTime));
    }

    /**
     * 获取过渡动画的path
     */
    private Path getTransitionPath(float startX, float endX, float cy, float radius) {
        Path path = new Path();
        path.moveTo(startX, cy + radius);
        RectF leftRect = new RectF(startX - radius, cy - radius, startX + radius, cy + radius);
        path.arcTo(leftRect, 90, 180, false);
        path.lineTo(endX, cy - radius);
        RectF rightRect = new RectF(endX - radius, cy - radius, endX + radius, cy + radius);
        path.arcTo(rightRect, -90, 180, false);
        path.lineTo(startX, cy + radius);
        return path;
    }

    /**
     * 绘制导航点
     */
    private void drawDot(Canvas canvas) {
        int size = mDotList.size();
        for (int i = 0; i < size; i++) {
            IndicatorDot dot = mDotList.get(i);
            canvas.drawCircle(dot.getCx(), dot.getCy(), dot.getRadius(), mDotPaint);
        }
    }

    public void setCount(int count, boolean isReset) {
        this.mCount = count;
        if (isReset) {
            reset();
        }
    }

    public void reset() {
        if (mAnimator != null && mAnimator.isRunning()) {
            mAnimator.cancel();
        }
        mTargetPosition = 0;
        mSelect = 0;
        mPercent = 0.0f;
        invalidate();
    }

    public static class IndicatorDot {
        private float cx;
        private float cy;
        private float radius;

        private float getCx() {
            return cx;
        }

        private void setCx(float cx) {
            this.cx = cx;
        }

        private float getCy() {
            return cy;
        }

        private void setCy(float cy) {
            this.cy = cy;
        }

        private float getRadius() {
            return radius;
        }

        private void setRadius(float radius) {
            this.radius = radius;
        }

        @Override
        public String toString() {
            return "IndicatorDot{" +
                    "cx=" + cx +
                    ", cy=" + cy +
                    ", mRadius=" + radius +
                    '}';
        }
    }

    public static class ViewPagerController implements ViewPager.OnPageChangeListener {

        private IndicatorView mIndicatorView;

        public ViewPagerController(IndicatorView indicatorView) {
            mIndicatorView = indicatorView;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int maxCount = mIndicatorView.getCount();
            if (maxCount > 0) {
                mIndicatorView.setScene(position % maxCount, positionOffset, position % maxCount == maxCount - 1 ? TRANSITION_JUMP : TRANSITION_STICK);
            }
        }

        @Override
        public void onPageSelected(int position) {}

        @Override
        public void onPageScrollStateChanged(int state) {}
    }
}
