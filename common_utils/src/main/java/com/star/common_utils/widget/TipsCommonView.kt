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
    private lateinit var mActivity: Activity

    init {
        mImgClose.setOnClickListener {
            dismiss()
        }
    }

    fun show(activity: Activity, text: String, view: View?, @TipsBgView.TrianglePos tipsPos: Int) {
        if (mRootView.parent != null) {
            removeViewFromParent()
        }
        mTvTip.text = text
        mActivity = activity
        view?.post {
            view.run {
                val layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                val location = IntArray(2)
                getLocationOnScreen(location)
                mRootView.mTrianglePos = tipsPos
                when (tipsPos) {
                    TipsBgView.POS_TRIANGLE_TOP -> {
                        mTipLayout.setPadding(0, mRootView.mTriangleHeight.toInt(), 0, 0)
                        mRootView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
                        layoutParams.topMargin = location[1] + height
                        val beyondSize = UIUtil.getScreenAvailAbleSize(mContext).x - location[0] - mRootView.measuredWidth
                        val size = if (beyondSize > 0) 0 else beyondSize
                        layoutParams.leftMargin = location[0] + size
                        mRootView.mTriangleLeftMargin = mRootView.paddingLeft + width / 2f - size
                        val parent = activity.window.decorView as FrameLayout
                        parent.addView(mRootView, layoutParams)
                        doShowAnimation()
                    }
                    TipsBgView.POS_TRIANGLE_BOTTOM -> {
                        mTipLayout.setPadding(0, 0, 0, mRootView.mTriangleHeight.toInt())
                        mRootView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
                        layoutParams.topMargin = location[1] - mRootView.measuredHeight
                        val beyondSize = UIUtil.getScreenAvailAbleSize(mContext).x - location[0] - mRootView.measuredWidth
                        val size = if (beyondSize > 0) 0 else beyondSize
                        layoutParams.leftMargin = location[0] + size
                        mRootView.mTriangleLeftMargin = mRootView.paddingLeft + width / 2f - size
                        val parent = activity.window.decorView as FrameLayout
                        parent.addView(mRootView, layoutParams)
                        doShowAnimation()
                    }
                    TipsBgView.POS_TRIANGLE_LEFT -> {
                        mTipLayout.setPadding(mRootView.mTriangleHeight.toInt(), 0, 0, 0)
                        mRootView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
                        layoutParams.topMargin = location[1] + height / 2 - mRootView.measuredHeight / 2
                        layoutParams.leftMargin = location[0] + width
                        mRootView.mTriangleTopMargin = (mRootView.measuredHeight / 2).toFloat()
                        val parent = activity.window.decorView as FrameLayout
                        parent.addView(mRootView, layoutParams)
                        doShowAnimation()
                    }
                    TipsBgView.POS_TRIANGLE_RIGHT -> {
                        mTipLayout.setPadding(0, 0, mRootView.mTriangleHeight.toInt(), 0)
                        mRootView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
                        layoutParams.topMargin = location[1] + height / 2 - mRootView.measuredHeight / 2
                        layoutParams.leftMargin = location[0] - mRootView.measuredWidth
                        mRootView.mTriangleTopMargin = (mRootView.measuredHeight / 2).toFloat()
                        val parent = activity.window.decorView as FrameLayout
                        parent.addView(mRootView, layoutParams)
                        doShowAnimation()
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
                mRootView.pivotX = mRootView.mTriangleLeftMargin
                mRootView.pivotY = 0f
            }
            TipsBgView.POS_TRIANGLE_BOTTOM -> {
                mRootView.pivotX = mRootView.mTriangleLeftMargin
                mRootView.pivotY = mRootView.measuredHeight.toFloat()
            }
            TipsBgView.POS_TRIANGLE_LEFT -> {
                mRootView.pivotX = 0f
                mRootView.pivotY = (mRootView.measuredHeight / 2).toFloat()
            }
            TipsBgView.POS_TRIANGLE_RIGHT -> {
                mRootView.pivotX = mRootView.measuredWidth.toFloat()
                mRootView.pivotY = (mRootView.measuredHeight / 2).toFloat()
            }
        }
        scaleAnimationX.duration = 300
        scaleAnimationY = ObjectAnimator.ofFloat(mRootView, "scaleY", 0f, 1f)
        scaleAnimationY.duration = 300
        scaleAnimationX.start()
        scaleAnimationY.start()
    }

    fun dismiss() {
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

    protected fun removeViewFromParent() {
        val frameLayout = mRootView.parent as FrameLayout
        frameLayout.removeView(mRootView)
    }
}