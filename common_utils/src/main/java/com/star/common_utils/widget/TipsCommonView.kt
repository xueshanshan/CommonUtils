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
import android.widget.TextView
import com.star.common_utils.R
import com.star.common_utils.utils.UIUtil

/**
 *   created by xueshanshan on 2020/6/6
 */
class TipsCommonView(val mContext: Context) {
    private val mRootView = LayoutInflater.from(mContext).inflate(R.layout.tips_guide_view, null)
    private val mTipLayout = mRootView.findViewById<TipsBgView>(R.id.tips_layout)
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
        //TODO 需要处理各种情况
        view?.post {
            view.run {
                val layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                layoutParams.topMargin = bottom
                mRootView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
                val beyondSize = UIUtil.getScreenAvailAbleSize(mContext).x - left - mRootView.measuredWidth
                val size = if (beyondSize > 0) 0 else beyondSize.toInt()
                layoutParams.leftMargin = left + size
                mTipLayout.mTriangleLeft = mTipLayout.paddingLeft + width / 2f - size
                activity.addContentView(mRootView, layoutParams)
                doShowAnimation()
            }
        }
    }

    private fun doShowAnimation() {
        scaleAnimationX = ObjectAnimator.ofFloat(mRootView, "scaleX", 0f, 1f)
        mRootView.pivotX = mTipLayout.mTriangleLeft
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