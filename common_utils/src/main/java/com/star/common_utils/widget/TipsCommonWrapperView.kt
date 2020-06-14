package com.star.common_utils.widget

import android.animation.Animator
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.star.common_utils.R
import com.star.common_utils.utils.AppUtil
import com.star.common_utils.utils.UIUtil

/**
 *   created by xueshanshan on 2020/6/14
 */

open class TipsCommonWrapperView(protected val mContext: Context) {
    protected val mRootView: View = LayoutInflater.from(mContext).inflate(getLayout(), null)
    protected val mTipsBgView: TipsBgView = getTipsBgView()
    private val mImgClose: View? = getImgCloseView()
    private val mTvContent: TextView? = getTextContentView()

    private var mCloseClickCallBack: (() -> Unit)? = null
    private lateinit var scaleAnimationX: ObjectAnimator
    private lateinit var scaleAnimationY: ObjectAnimator
    protected var mShowCount = -1;

    init {
        mImgClose?.setOnClickListener {
            mCloseClickCallBack?.invoke()
            dismiss()
        }
    }

    fun setCloseClickCallBack(callback: (() -> Unit)?) {
        mCloseClickCallBack = callback
    }

    @LayoutRes
    open fun getLayout(): Int {
        return R.layout.tips_guide_view
    }

    open fun getTipsBgView(): TipsBgView {
        return mRootView as TipsBgView
    }

    open fun getImgCloseView(): View? {
        return mRootView.findViewById(R.id.img_close)
    }

    open fun getTextContentView(): TextView? {
        return mRootView.findViewById(R.id.tv_tip)
    }

    fun isShowing(): Boolean {
        return mShowCount == 1
    }

    /**
     * 统一展示入口， 子类不能重写
     */
    fun show(paramsAction: TipsCommonParams.() -> Unit) {
        val params = TipsCommonParams().apply(paramsAction)
        val verifyResult = verifyParams(params)
        if (!verifyResult) {
            return
        }
        mShowCount = 0
        params.anchorView?.post {
            //可能post还未到达时调用了dismiss，那么此时不再做展示
            if (mShowCount < 0) {
                return@post
            }
            doShow(params)
        }
    }

    /**
     * 统一隐藏入口，子类不能重写
     */
    fun dismiss() {
        if (mShowCount <= 0) {
            mShowCount = -1
            return
        }
        //只有mShowCount = 1的时候即真正在展示的时候再去做动画
        doDismissAnimation()
    }

    /**
     * 验证参数合法性  子类可重写
     */
    open fun verifyParams(commonParams: TipsCommonParams): Boolean {
        if (commonParams.activity == null) {
            return false
        }
        if (commonParams.anchorView == null) {
            return false
        }
        if (mTvContent != null) {
            if (commonParams.content.isNullOrBlank()) {
                return false
            } else {
                mTvContent.text = commonParams.content
            }
        }
        return true
    }

    /**
     * 真正展示的地方 子类可重写展示逻辑
     */
    open fun doShow(commonParams: TipsCommonParams) {
        commonParams.anchorView?.run {
            mTipsBgView.mTrianglePos = commonParams.tipsPos
            var paddingSeted = false
            commonParams.paddingArray?.let {
                if (it.size == 4) {
                    paddingSeted = true
                    mTvContent?.setPadding((it[0] + mTipsBgView.mShadowRadius).toInt(),
                            (it[1] + mTipsBgView.mShadowRadius).toInt(),
                            (it[2] + mTipsBgView.mShadowRadius).toInt(),
                            (it[3] + mTipsBgView.mShadowRadius).toInt())
                }
            }
            if (!paddingSeted) {
                val padding = (AppUtil.dp2px(mContext, 10) + mTipsBgView.mShadowRadius).toInt()
                mTvContent?.setPadding(padding, padding, padding, padding)
            }
            if (commonParams.contentTextSize != 0f) {
                mTvContent?.textSize = commonParams.contentTextSize
            }

            val layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            val location = IntArray(2)
            getLocationOnScreen(location)
            val tipLayout = mRootView.findViewById<View>(R.id.tips_layout)
            when (commonParams.tipsPos) {
                TipsBgView.POS_TRIANGLE_TOP -> {
                    tipLayout.setPadding(0, mTipsBgView.mTriangleHeight.toInt(), 0, 0)
                    mRootView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
                    layoutParams.topMargin = location[1] + height + commonParams.verticalOffset
                    showTopOrBottom(location, layoutParams, commonParams)
                }
                TipsBgView.POS_TRIANGLE_BOTTOM -> {
                    tipLayout.setPadding(0, 0, 0, mTipsBgView.mTriangleHeight.toInt())
                    mRootView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
                    layoutParams.topMargin = location[1] - mRootView.measuredHeight + commonParams.verticalOffset
                    showTopOrBottom(location, layoutParams, commonParams)
                }
                TipsBgView.POS_TRIANGLE_LEFT -> {
                    tipLayout.setPadding(mTipsBgView.mTriangleHeight.toInt(), 0, 0, 0)
                    mRootView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
                    layoutParams.leftMargin = location[0] + width + commonParams.offset
                    showLeftOrRight(layoutParams, location, commonParams)
                }
                TipsBgView.POS_TRIANGLE_RIGHT -> {
                    tipLayout.setPadding(0, 0, mTipsBgView.mTriangleHeight.toInt(), 0)
                    mRootView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
                    layoutParams.leftMargin = location[0] - mRootView.measuredWidth + commonParams.offset
                    showLeftOrRight(layoutParams, location, commonParams)
                }
                else -> {
                    throw RuntimeException("tipsPos param is not correct")
                }
            }
        }
    }

    private fun View.showTopOrBottom(location: IntArray, layoutParams: FrameLayout.LayoutParams, commonParams: TipsCommonParams) {
        when {
            location[0] + width / 2 == UIUtil.getScreenAvailAbleSize(mContext).x / 2 -> {
                layoutParams.leftMargin = (UIUtil.getScreenAvailAbleSize(mContext).x / 2f - mRootView.measuredWidth / 2f).toInt()
                mTipsBgView.mTriangleLeftMargin = mRootView.measuredWidth / 2f
            }
            location[0] + width / 2 < UIUtil.getScreenAvailAbleSize(mContext).x / 2 -> {
                layoutParams.leftMargin = location[0] + commonParams.offset
                mTipsBgView.mTriangleLeftMargin = width / 2f - commonParams.offset
            }
            else -> {
                layoutParams.leftMargin = location[0] - mRootView.measuredWidth + width
                mTipsBgView.mTriangleLeftMargin = mRootView.measuredWidth - width / 2f + commonParams.offset
            }
        }
        addViewToParent(commonParams.activity, layoutParams)
    }

    private fun View.showLeftOrRight(layoutParams: FrameLayout.LayoutParams, location: IntArray, commonParams: TipsCommonParams) {
        layoutParams.topMargin = location[1] + height / 2 - mRootView.measuredHeight / 2
        mTipsBgView.mTriangleTopMargin = (mRootView.measuredHeight / 2).toFloat()
        addViewToParent(commonParams.activity, layoutParams)
    }

    /**
     * 展示动画，子类可重写动画逻辑
     */
    open fun doShowAnimation() {
        scaleAnimationX = ObjectAnimator.ofFloat(mRootView, "scaleX", 0f, 1f)
        when (mTipsBgView.mTrianglePos) {
            TipsBgView.POS_TRIANGLE_TOP -> {
                mRootView.pivotX = mTipsBgView.mTriangleLeftMargin + mTipsBgView.mShadowRadius
                mRootView.pivotY = mTipsBgView.mShadowRadius
            }
            TipsBgView.POS_TRIANGLE_BOTTOM -> {
                mRootView.pivotX = mTipsBgView.mTriangleLeftMargin + mTipsBgView.mShadowRadius
                mRootView.pivotY = mTipsBgView.measuredHeight - mTipsBgView.mShadowRadius
            }
            TipsBgView.POS_TRIANGLE_LEFT -> {
                mRootView.pivotX = mTipsBgView.mShadowRadius
                mRootView.pivotY = (mRootView.measuredHeight / 2).toFloat()
            }
            TipsBgView.POS_TRIANGLE_RIGHT -> {
                mRootView.pivotX = mRootView.measuredWidth - mTipsBgView.mShadowRadius
                mRootView.pivotY = (mRootView.measuredHeight / 2).toFloat()
            }
        }
        scaleAnimationX.duration = 300
        scaleAnimationY = ObjectAnimator.ofFloat(mRootView, "scaleY", 0f, 1f)
        scaleAnimationY.duration = 300
        scaleAnimationX.start()
        scaleAnimationY.start()
    }

    /**
     * 消失动画， 子类可重写动画逻辑
     */
    open fun doDismissAnimation() {
        scaleAnimationX.reverse()
        scaleAnimationY.reverse()
        scaleAnimationX.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                removeViewFromParent()
            }

            override fun onAnimationCancel(animation: Animator?) {

            }

            override fun onAnimationStart(animation: Animator?) {

            }

        })
    }

    /**
     * 添加到decorView中
     */
    protected fun addViewToParent(activity: Activity?, layoutParams: FrameLayout.LayoutParams) {
        activity?.window?.run {
            removeViewFromParent()
            val parent = decorView as FrameLayout
            parent.addView(mRootView, layoutParams)
            doShowAnimation()
            mShowCount = 1
        }
    }

    /**
     * 从decorView中移除
     */
    protected fun removeViewFromParent() {
        mRootView.parent?.let {
            val frameLayout = it as FrameLayout
            frameLayout.removeView(mRootView)
            mShowCount = -1
        }
    }
}