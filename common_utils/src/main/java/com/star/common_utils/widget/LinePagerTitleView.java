package com.star.common_utils.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;
import com.star.common_utils.R;
import com.star.common_utils.listener.SimpleAnimatorListener;
import com.star.common_utils.utils.AppUtil;
import com.star.common_utils.utils.image.ImageLoader;
import com.star.common_utils.utils.image.SimpleTarget;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.List;


/**
 * Created by star on 2016/12/14
 * 功能:
 */
public class LinePagerTitleView<T> extends LinearLayout {
    private CommonNavigator navigator;
    private MagicIndicator magicIndicator;
    private int normalColor;
    private int selectColor;
    private int indicatorColor;
    private float textSize;
    private float lineHeight;
    private float lineWidth;
    private int lineMode = LinePagerIndicator.MODE_EXACTLY;
    private float drawablePadding;
    public float lineRadius;
    private boolean adjustMode;
    private int titlePadding;
    private int navigatorPadding;
    private boolean bold;

    private List<T> objectList;
    private ViewPager mViewPager;
    private TopPagerTitleListener<T> mTopPagerTitleListener;
    private TopPagerListenerIcon<T> mTopPagerIconListener;
    private TopPagerClickListener<T> mTopPagerClickListener;

    private int mLastIndex;
    private int mToIndex;
    private int mTotalDiff;
    private int mCurDiff;
    private ValueAnimator mAnimator;

    public LinePagerTitleView(Context context) {
        this(context, null);
    }

    public LinePagerTitleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LinePagerTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Resources resources = context.getResources();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LinePagerTitleView);
        normalColor = typedArray.getColor(R.styleable.LinePagerTitleView_normal_color, resources.getColor(R.color.half_white));
        selectColor = typedArray.getColor(R.styleable.LinePagerTitleView_select_color, Color.WHITE);
        indicatorColor = typedArray.getColor(R.styleable.LinePagerTitleView_indicator_color, selectColor);
        lineHeight = typedArray.getDimension(R.styleable.LinePagerTitleView_pager_title_line_height, AppUtil.dp2px(context, 2));
        lineWidth = typedArray.getDimension(R.styleable.LinePagerTitleView_pager_title_line_width, AppUtil.dp2px(context, 13));
        titlePadding = typedArray.getDimensionPixelSize(R.styleable.LinePagerTitleView_title_padding, -1);
        navigatorPadding = typedArray.getDimensionPixelOffset(R.styleable.LinePagerTitleView_navigator_padding, -1);
        lineRadius = typedArray.getDimension(R.styleable.LinePagerTitleView_line_radius, 0);
        drawablePadding = typedArray.getDimension(R.styleable.LinePagerTitleView_drawable_padding, AppUtil.dp2px(context, 8));
        textSize = typedArray.getDimension(R.styleable.LinePagerTitleView_android_textSize, AppUtil.dp2px(context, 15));
        adjustMode = typedArray.getBoolean(R.styleable.LinePagerTitleView_adjust_mode, false);
        bold = typedArray.getBoolean(R.styleable.LinePagerTitleView_bold, false);
        typedArray.recycle();
        init(context);
    }

    private void init(Context context) {
        setOrientation(VERTICAL);
        magicIndicator = new MagicIndicator(context);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        magicIndicator.setLayoutParams(params);
        addView(magicIndicator);

        navigator = new CommonNavigator(context);
        if (navigatorPadding != -1) {
            navigator.setLeftPadding(navigatorPadding);
            navigator.setRightPadding(navigatorPadding);
        }
        navigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                int ret = 0;
                if (objectList != null) {
                    ret = objectList.size();
                }
                return ret;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                final SimplePagerTitleView simplePagerTitleView = new SimplePagerTitleView(context) {
                    @Override
                    public void onSelected(int index, int totalCount) {
                        if (bold) {
                            getPaint().setFakeBoldText(true);
                        }
                        super.onSelected(index, totalCount);
                    }

                    @Override
                    public void onDeselected(int index, int totalCount) {
                        if (bold) {
                            getPaint().setFakeBoldText(false);
                        }
                        super.onDeselected(index, totalCount);
                    }
                };
                if (titlePadding != -1) {
                    simplePagerTitleView.setPadding(titlePadding, 0, titlePadding, 0);
                }
                simplePagerTitleView.setNormalColor(normalColor);
                simplePagerTitleView.setSelectedColor(selectColor);
                simplePagerTitleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
//                simplePagerTitleView.setTypeface();
                if (mTopPagerTitleListener != null) {
                    simplePagerTitleView.setText(mTopPagerTitleListener.getTopPagerTitle(objectList.get(index)));
                } else {
                    simplePagerTitleView.setText(objectList.get(index).toString());
                }
                if (mTopPagerIconListener != null) {
                    String topPagerIconUrl = mTopPagerIconListener.getTopPagerIconUrl(objectList.get(index));
                    if (!TextUtils.isEmpty(topPagerIconUrl)) {
                        ImageLoader.getDefaultImageLoader().load(topPagerIconUrl).into(new SimpleTarget() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
                                bitmapDrawable.setBounds(0, 0, bitmapDrawable.getMinimumWidth(), bitmapDrawable.getMinimumHeight());
                                simplePagerTitleView.setCompoundDrawables(bitmapDrawable, null, null, null);
                                simplePagerTitleView.setCompoundDrawablePadding((int) drawablePadding);
                            }
                        });
                    }
                }
                simplePagerTitleView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mViewPager != null) {
                            mViewPager.setCurrentItem(index);
                        } else {
                            onTabClickAnimator(index);
                        }
                        if (mTopPagerClickListener != null) {
                            mTopPagerClickListener.onTabClick(index, objectList.get(index));
                        }
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
                linePagerIndicator.setLineHeight(lineHeight);
                linePagerIndicator.setLineWidth(lineWidth);
                linePagerIndicator.setMode(lineMode);
                linePagerIndicator.setColors(indicatorColor);
                linePagerIndicator.setRoundRadius(lineRadius);
                return linePagerIndicator;
            }
        });
        navigator.setAdjustMode(adjustMode);
        magicIndicator.setNavigator(navigator);
    }


    public void setObjectList(List<T> objectList) {
        this.objectList = objectList;
        navigator.notifyDataSetChanged();
    }

    public void onPageSelected(int index) {
        if (mViewPager != null) {
            mViewPager.setCurrentItem(index);
        } else {
            mLastIndex = index;
            magicIndicator.onPageSelected(index);
        }
    }

    public void setViewPager(ViewPager viewPager) {
        this.mViewPager = viewPager;
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                magicIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                magicIndicator.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                magicIndicator.onPageScrollStateChanged(state);
            }
        });
    }

    public void setAdjustMode(boolean adjustMode) {
        this.adjustMode = adjustMode;
        navigator.setAdjustMode(adjustMode);
        navigator.onAttachToMagicIndicator();
    }


    public void setTopPagerTitleListener(TopPagerTitleListener<T> topPagerTitleListener) {
        this.mTopPagerTitleListener = topPagerTitleListener;
    }

    public void setTopPagerIconListener(TopPagerListenerIcon<T> topPagerIconListener) {
        this.mTopPagerIconListener = topPagerIconListener;
    }

    public void setTopPagerClickListener(TopPagerClickListener<T> topPagerClickListener) {
        mTopPagerClickListener = topPagerClickListener;
    }

    /**
     * 如果该导航没跟viewPager绑定，那么点击的时候就需要自己做动画
     *
     * @param toIndex 被选中的index
     */
    public void onTabClickAnimator(int toIndex) {
        int diff = Math.abs(toIndex - mLastIndex);
        if (diff > 0) {
            mTotalDiff = diff;
            mToIndex = toIndex;
            mCurDiff = 1;
            initAnimator();
            mAnimator.start();
        }
    }

    public void initAnimator() {
        if (mAnimator == null) {
            mAnimator = ValueAnimator.ofFloat(0.0F, 1.0F);
            mAnimator.setDuration(200);
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    Float animatedValue = (Float) animation.getAnimatedValue();
                    float i = 1f / mTotalDiff;
                    if (animatedValue > mCurDiff * i) {
                        mCurDiff++;
                        if (mLastIndex < mToIndex) {
                            mLastIndex++;
                        } else {
                            mLastIndex--;
                        }
                    }
                    animatedValue -= (mCurDiff - 1) * i;

                    if (mLastIndex < mToIndex) {
                        magicIndicator.onPageScrolled(mLastIndex, animatedValue * mTotalDiff, 0);
                    } else if (mLastIndex > mToIndex) {
                        magicIndicator.onPageScrolled(mLastIndex - 1, 1 - animatedValue * mTotalDiff, 0);
                    }
                }
            });
            mAnimator.addListener(new SimpleAnimatorListener() {
                @Override
                public void onAnimationEnd(Animator animation, boolean isReverse) {
                    magicIndicator.onPageSelected(mToIndex);
                    mLastIndex = mToIndex;
                }
            });
        }
    }

    public interface TopPagerTitleListener<T> {
        String getTopPagerTitle(T t);
    }

    public interface TopPagerListenerIcon<T> {
        String getTopPagerIconUrl(T t);
    }

    public interface TopPagerClickListener<T> {
        void onTabClick(int index, T t);
    }
}
