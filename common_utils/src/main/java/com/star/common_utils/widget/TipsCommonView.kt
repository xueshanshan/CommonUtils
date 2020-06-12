package com.star.common_utils.widget

import android.animation.Animator
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.star.common_utils.R
import com.star.common_utils.utils.UIUtil

/**
 *   created by xueshanshan on 2020/6/6
 */
class TipsCommonView(val mContext: Context) {
    private val mRootView: TipsBgView = LayoutInflater.from(mContext).inflate(R.layout.tips_guide_view, null) as TipsBgView
    private val mTipLayout: LinearLayout = mRootView.findViewById(R.id.tips_layout)
    private val mTvTip = mRootView.findViewById<TextView>(R.id.tv_tip)
    private val mImgClose = mRootView.findViewById<ImageView>(R.id.img_close)
    private lateinit var scaleAnimationX: ObjectAnimator
    private lateinit var scaleAnimationY: ObjectAnimator
    private var mShowCount = -1;
    private var mCloseClickCallBack: (() -> Unit)? = null

    init {
        mImgClose.setOnClickListener {
            mCloseClickCallBack?.invoke()
            dismiss()
        }
    }

    fun setCloseClickCallBack(calback: (() -> Unit)?) {
        mCloseClickCallBack = calback
    }

    fun show(activity: Activity?, text: String, view: View?, @TipsBgView.TrianglePos tipsPos: Int) {
        if (activity == null) {
            return
        }
        mTvTip.text = text
        mShowCount = 0
        view?.post {
            view.run {
                //可能post还未到达时调用了dismiss，那么此时不再做展示
                if (mShowCount < 0) {
                    return@run
                }
                val layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                val location = IntArray(2)
                getLocationOnScreen(location)
                mRootView.mTrianglePos = tipsPos
                mTvTip.setPadding((mTvTip.paddingLeft + mRootView.mShadowRadius).toInt(), (mTvTip.paddingTop + mRootView.mShadowRadius).toInt(), (mTvTip.paddingRight + mRootView.mShadowRadius).toInt(), (mTvTip.paddingBottom + mRootView.mShadowRadius).toInt())
                when (tipsPos) {
                    TipsBgView.POS_TRIANGLE_TOP -> {
                        mTipLayout.setPadding(0, mRootView.mTriangleHeight.toInt(), 0, 0)
                        mRootView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
                        layoutParams.topMargin = location[1] + height
                        val beyondSize = UIUtil.getScreenAvailAbleSize(mContext).x - location[0] - mRootView.measuredWidth
                        val size = if (beyondSize > 0) 0 else beyondSize
                        layoutParams.leftMargin = location[0] + size
                        mRootView.mTriangleLeftMargin = mRootView.paddingLeft + width / 2f - size
                        addViewToParent(activity, layoutParams)
                    }
                    TipsBgView.POS_TRIANGLE_BOTTOM -> {
                        mTipLayout.setPadding(0, 0, 0, mRootView.mTriangleHeight.toInt())
                        mRootView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
                        layoutParams.topMargin = location[1] - mRootView.measuredHeight
                        val beyondSize = UIUtil.getScreenAvailAbleSize(mContext).x - location[0] - mRootView.measuredWidth
                        val size = if (beyondSize > 0) 0 else beyondSize
                        layoutParams.leftMargin = location[0] + size
                        mRootView.mTriangleLeftMargin = mRootView.paddingLeft + width / 2f - size
                        addViewToParent(activity, layoutParams)
                    }
                    TipsBgView.POS_TRIANGLE_LEFT -> {
                        mTipLayout.setPadding(mRootView.mTriangleHeight.toInt(), 0, 0, 0)
                        mRootView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
                        layoutParams.topMargin = location[1] + height / 2 - mRootView.measuredHeight / 2
                        layoutParams.leftMargin = location[0] + width
                        mRootView.mTriangleTopMargin = (mRootView.measuredHeight / 2).toFloat()
                        addViewToParent(activity, layoutParams)
                    }
                    TipsBgView.POS_TRIANGLE_RIGHT -> {
                        mTipLayout.setPadding(0, 0, mRootView.mTriangleHeight.toInt(), 0)
                        mRootView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
                        layoutParams.topMargin = location[1] + height / 2 - mRootView.measuredHeight / 2
                        layoutParams.leftMargin = location[0] - mRootView.measuredWidth
                        mRootView.mTriangleTopMargin = (mRootView.measuredHeight / 2).toFloat()
                        addViewToParent(activity, layoutParams)
                    }
                    else -> {
                        throw RuntimeException("tipsPos param is not correct")
                    }
                }

            }
        }
    }

    private fun doShowAnimation() {
        scaleAnimationX = ObjectAnimator.ofFloat(mRootView, "scaleX", 0f, 1f)
        when (mRootView.mTrianglePos) {
            TipsBgView.POS_TRIANGLE_TOP -> {
                mRootView.pivotX = mRootView.mTriangleLeftMargin + mRootView.mShadowRadius
                mRootView.pivotY = mRootView.mShadowRadius
            }
            TipsBgView.POS_TRIANGLE_BOTTOM -> {
                mRootView.pivotX = mRootView.mTriangleLeftMargin + mRootView.mShadowRadius
                mRootView.pivotY = mRootView.measuredHeight - mRootView.mShadowRadius
            }
            TipsBgView.POS_TRIANGLE_LEFT -> {
                mRootView.pivotX = mRootView.mShadowRadius
                mRootView.pivotY = (mRootView.measuredHeight / 2).toFloat()
            }
            TipsBgView.POS_TRIANGLE_RIGHT -> {
                mRootView.pivotX = mRootView.measuredWidth - mRootView.mShadowRadius
                mRootView.pivotY = (mRootView.measuredHeight / 2).toFloat()
            }
        }
        scaleAnimationX.duration = 300
        scaleAnimationY = ObjectAnimator.ofFloat(mRootView, "scaleY", 0f, 1f)
        scaleAnimationY.duration = 300
        scaleAnimationX.start()
        scaleAnimationY.start()
    }

    fun isShowing(): Boolean {
        return mShowCount == 1
    }

    fun dismiss() {
        if (mShowCount <= 0) {
            mShowCount = -1
            return
        }
        //只有mShowCount = 1的时候即真正在展示的时候再去做动画
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

    private fun addViewToParent(activity: Activity?, layoutParams: FrameLayout.LayoutParams) {
        activity?.window?.run {
            removeViewFromParent()
            val parent = decorView as FrameLayout
            parent.addView(mRootView, layoutParams)
            doShowAnimation()
            mShowCount = 1
        }
    }

    private fun removeViewFromParent() {
        mRootView.parent?.let {
            val frameLayout = it as FrameLayout
            frameLayout.removeView(mRootView)
            mShowCount = -1
        }
    }
}