package com.star.common_utils.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;


import com.star.common_utils.R;
import com.star.common_utils.utils.LogUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xueshanshan
 * @date 2018/12/17
 * <p>
 * 思路：前面后面各多加一张图片  'c' , a , b , c , 'a'
 * viewpager两个需要注意的问题   https://blog.csdn.net/u011002668/article/details/72884893?locationNum=6&fps=1
 */
public class WheelViewPager<T> extends ViewPager {
    public static final String TAG = WheelViewPager.class.getSimpleName();

    /* 是否自动播放,如果mPlayInterval大于等于1s,会被设为true */
    private boolean mAutoPlay;
    /* 自动播放的时间间隔 */
    private int mPlayInterval;
    /* 是否循环,即是否在头尾添加2张图片，会根据实际情况赋值 */
    private boolean mPageLoop;
    /* 真实的用户设置的是否在头尾添加2张图片 */
    private boolean mLastPageLoop;
    /* 页面切换滑动的时长 */
    private int mScrollDuration;

    /* 数据源 */
    private List<T> mDatas = new ArrayList<>();
    /* 适配器 */
    private WheelPagerAdapter mWheelPagerAdapter;

    /* 是否DetachFromWindow */
    private boolean mDetached;
    /* 是否切后台 */
    private boolean mResumed = true;
    /* 是否不可见 */
    private boolean mVisiable = true;
    /* 当前的，每个数据对应的index，在onPageSelected中赋值，所以注意调用时机 */
    private int mCurIndex;
    /* 播放是否被停止 */
    private boolean playStoped = true;
    /* 轮播用到的handler */
    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (!playStoped) {
                mCurIndex++;
                if (mCurIndex == mWheelPagerAdapter.getCount()) {
                    mCurIndex = 0;
                }
                WheelViewPager.this.setCurrentItem(mCurIndex);
            }
        }
    };
    /* 为了解决 RecyclerView滚动上去，直至ViewPager看不见，再滚动下来，ViewPager下一次切换没有动画 的问题，反射获取并重新设值 */
    private Field mFirstLayout;
    /* 导航点需要的position 针对真正的数据源 在onPageScroll中获取该值 */
    private int pageScrollRealPosition;
    /* 是否是导航点跳转的边界 */
    private boolean pageEdge;
    /* 当前滑动的偏移量，为了解决当ViewPage滚动到一半的时候，RecyclerView滚动上去，再滚动下来，ViewPager会卡在一半 */
    private float mCurPositionOffset;
    /* 滑动的真实position */
    private int mCurPosition;

    public WheelViewPager(@NonNull Context context) {
        this(context, null);
    }

    public WheelViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.WheelViewPager);
            mPlayInterval = array.getInt(R.styleable.WheelViewPager_play_interval, 3000);
            mLastPageLoop = array.getBoolean(R.styleable.WheelViewPager_page_loop, true);
            mScrollDuration = array.getInt(R.styleable.WheelViewPager_scroll_duration, 1000);
            setScrollDuration();
            array.recycle();
        }
        try {
            mFirstLayout = ViewPager.class.getDeclaredField("mFirstLayout");
            mFirstLayout.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        mWheelPagerAdapter = new WheelPagerAdapter();
        setAdapter(mWheelPagerAdapter);
        addOnPageChangeListener(new WheelPagerChangeListener());
    }

    /**
     * 设置数据源
     *
     * @param datas 数据源
     */
    public void setData(List<T> datas) {
        //先停止播放
        stopPlay();

        //修改数据源，并且判断是否能够循环播放
        mDatas.clear();
        mDatas.addAll(datas);
        if (mDatas.size() <= 1) {
            //当数据源小于等于1的时候就不添加左右两边了，而且也不自动播放
            mPageLoop = false;
            mAutoPlay = false;
        } else {
            //其他情况恢复变量值
            if (mPlayInterval >= 1000) {
                mAutoPlay = true;
            }
            mPageLoop = mLastPageLoop;
        }
        mWheelPagerAdapter.notifyDataSetChanged();

        //如果是循环播放，需要从第一个item进行显示，因为第0个显示的是最后一个
        if (mPageLoop) {
            setCurrentItem(1);
        }

        //开始播放
        starPlay();
    }

    /**
     * 设置适配器，这里其实只是设回调
     *
     * @param wheelPagerAdapterCallback 对应的回调接口
     */
    public void setAdapter(WheelPagerAdapterCallback wheelPagerAdapterCallback) {
        mWheelPagerAdapter.setWheelPagerAdapterCallback(wheelPagerAdapterCallback);
    }

    public WheelPagerAdapter getWheelPagerAdapter() {
        return mWheelPagerAdapter;
    }

    public boolean isPageLoop() {
        return mPageLoop;
    }

    public int getCurIndex() {
        return mCurIndex;
    }

    public int getPageScrollRealPosition() {
        return pageScrollRealPosition;
    }

    public boolean isPageEdge() {
        return pageEdge;
    }

    private void starPlay() {
        if (mAutoPlay) {
            playStoped = false;
            if (!mHandler.hasMessages(0)) {
                mHandler.postDelayed(mRunnable, mPlayInterval);
            }
        }
    }

    //状态切换后尝试开始播放，保证多个变量满足
    private void tryStartPlay() {
        if (mAutoPlay && mResumed && mVisiable && !mDetached && playStoped) {
            LogUtil.d(TAG, "tryStartPlay");
            starPlay();
        }
    }

    private void stopPlay() {
        if (!playStoped) {
            LogUtil.d(TAG, "stopPlay");
            playStoped = true;
            mHandler.removeCallbacks(mRunnable);
        }
    }

    /**
     * 在Activity的onResume中调用
     */
    public void onResume() {
        if (!mResumed) {
            LogUtil.d(TAG, "onResume");
            mResumed = true;
            tryStartPlay();
        }
    }

    /**
     * 在Activity的onPause中调用
     */
    public void onPause() {
        if (mResumed) {
            LogUtil.d(TAG, "onPause");
            mResumed = false;
            stopPlay();
        }
    }

    private void setScrollDuration() {
        try {
            Field scrollerField = ViewPager.class.getDeclaredField("mScroller");
            scrollerField.setAccessible(true);
            scrollerField.set(this, new WheelScroller(getContext()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mDetached) {
            LogUtil.d(TAG, "onAttachedToWindow");
            mDetached = false;
            try {
                mFirstLayout.set(this, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
            tryStartPlay();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (!mDetached) {
            LogUtil.d(TAG, "onDetachedFromWindow");
            mDetached = true;
            stopPlay();

            // 处理 RecyclerView 中从对用户不可见变为可见时卡顿的问题
            if (mAutoPlay && mCurPositionOffset > 0) {
                setCurrentItem(mCurPosition);
                mCurPosition++;
                if (mCurPosition == mWheelPagerAdapter.getCount()) {
                    mCurPosition = 0;
                }
                setCurrentItem(mCurPosition);
            }
        }
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == View.VISIBLE) {
            if (!mVisiable) {
                mVisiable = true;
                tryStartPlay();
            }
        } else {
            if (mVisiable) {
                mVisiable = false;
                stopPlay();
            }
        }
    }

    //重写这个方法是为了手指放在上面不动的时候，是否停止播放
    //如果手指动的话会触发onPageScrollStateChanged的SCROLL_STATE_DRAGGING，然后会停止播放
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            /*
               如果是down事件，那么就停止滑动，
               up或者cancel事件，继续滑动
             */
            case MotionEvent.ACTION_DOWN:
                stopPlay();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                tryStartPlay();
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    private class WheelPagerAdapter extends PagerAdapter {
        private WheelPagerAdapterCallback mWheelPagerAdapterCallback;

        public void setWheelPagerAdapterCallback(WheelPagerAdapterCallback wheelPagerAdapterCallback) {
            mWheelPagerAdapterCallback = wheelPagerAdapterCallback;
        }

        @Override
        public int getCount() {
            int ret = mDatas.size();
            if (mPageLoop) {
                ret += 2;
            }
            return ret;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            if (mPageLoop) {
                if (position == 0) {
                    position = getCount() - 2;
                } else if (position == getCount() - 1) {
                    position = 1;
                }
                //算出针对于数据源真正的位置
                position = position - 1;
            }
            return mWheelPagerAdapterCallback.instantiateItem(container, position);
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            mWheelPagerAdapterCallback.destroyItem(container, position, object);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    private class WheelPagerChangeListener implements OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            mCurPosition = position;
            mCurPositionOffset = positionOffset;
            pageEdge = false;
            if (isPageLoop()) {
                if (position == 0) {
                    //从第一个往最后一个跳
                    pageScrollRealPosition = mWheelPagerAdapter.getCount() - 3;
                    pageEdge = true;
                } else if (position == mWheelPagerAdapter.getCount() - 1) {
                    //第一个往第二个跳
                    pageScrollRealPosition = 0;
                } else {
                    if (position == mWheelPagerAdapter.getCount() - 2) {
                        //从最后往第一个跳
                        pageEdge = true;
                    }
                    pageScrollRealPosition = position - 1;
                }
            } else {
                pageScrollRealPosition = position;
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (mPageLoop) {
                if (position == 0) {
                    mCurIndex = mWheelPagerAdapter.getCount() - 2;
                    return;
                } else if (position == mWheelPagerAdapter.getCount() - 1) {
                    mCurIndex = 1;
                    return;
                }
            }
            mCurIndex = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                if (mPageLoop) {
                    //悄悄将位置重置
                    setCurrentItem(mCurIndex, false);
                }
                starPlay();
            } else if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                stopPlay();
            }
        }
    }

    private class WheelScroller extends Scroller {

        public WheelScroller(Context context) {
            super(context);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy, mScrollDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, mScrollDuration);
        }
    }

    public interface WheelPagerAdapterCallback {
        Object instantiateItem(@NonNull ViewGroup container, int position);

        void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object);
    }
}
