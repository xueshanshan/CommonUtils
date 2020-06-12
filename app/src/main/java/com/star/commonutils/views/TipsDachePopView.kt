package com.star.commonutils.views

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.text.TextUtils
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.star.common_utils.utils.AppUtil
import com.star.common_utils.utils.UIUtil
import com.star.common_utils.widget.TipsBgView
import com.star.common_utils.widget.TipsCommonWrapperView
import com.star.common_utils.widget.TipsCommonParams
import com.star.commonutils.R


/**
 *   created by xueshanshan on 2020/6/5
 */
class TipsDachePopView(context: Context) : TipsCommonWrapperView(context) {
    private val mTvTip1: TextView = mRootView.findViewById(R.id.tv_tip_1)
    private val mTvTip2: TextView = mRootView.findViewById(R.id.tv_tip_2)

    override fun getLayout(): Int {
        return R.layout.dache_tips_container
    }

    override fun getImgCloseView(): View? {
        return mRootView.findViewById(R.id.img_tip_close)
    }

    override fun getTipsBgView(): TipsBgView {
        return mRootView.findViewById(R.id.tips_bg_view)
    }

    fun setContent(text1: String, text2: String) {
        mTvTip1.text = text1
        mTvTip2.text = text2
        mTvTip2.setTextSize(TypedValue.COMPLEX_UNIT_PX, (if (text1.isEmpty()) AppUtil.dp2px(mContext, 15) else AppUtil.dp2px(mContext, 18)).toFloat())
    }

    override fun doShow(commonParams: TipsCommonParams) {
        commonParams.anchorView?.run {
            //可能post还未到达时调用了dismiss，那么此时不再做展示
            if (mShowCount < 0) {
                return@run
            }
            val location = IntArray(2)
            getLocationOnScreen(location)
            if (location[1] == 0) {
                return@run
            }
            val layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            layoutParams.topMargin = location[1] + height + AppUtil.dp2px(mContext, -10)
            val measureText = mTvTip1.paint.measureText(mTvTip1.text.toString())
            val size = measureText - AppUtil.dp2px(mContext, 176)
            if (size > 0) {
                mTipsBgView.layoutParams.width = (AppUtil.dp2px(mContext, 250) + size).toInt()
            }
            mRootView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            if (location[0] + width / 2 <= UIUtil.getScreenAvailAbleSize(mContext).x / 2 + AppUtil.dp2px(mContext, 5)) {
                layoutParams.leftMargin = 0
            } else {
                layoutParams.leftMargin = UIUtil.getScreenAvailAbleSize(mContext).x - AppUtil.dp2px(mContext, 4) - mRootView.measuredWidth
            }
            mTipsBgView.mTriangleLeftMargin = (location[0] + width / 2 - layoutParams.leftMargin - AppUtil.dp2px(mContext, 8)).toFloat()
            addViewToParent(commonParams.activity, layoutParams)
        }
    }

    override fun doShowAnimation() {
        val scaleXAnimationContentView = ObjectAnimator.ofFloat(mRootView, "scaleX", 0f, 1f)
        scaleXAnimationContentView.duration = 300
        val scaleYAnimationContentView = ObjectAnimator.ofFloat(mRootView, "scaleY", 0f, 1f)
        scaleYAnimationContentView.duration = 300
        mRootView.pivotX = mTipsBgView.mTriangleLeftMargin + AppUtil.dp2px(mContext, 8)
        mRootView.pivotY = 0f
        scaleXAnimationContentView.start()
        scaleYAnimationContentView.start()

        mTvTip1.visibility = View.INVISIBLE
        mTvTip2.translationY = 0f
        if (!TextUtils.isEmpty(mTvTip1.text.toString()) && !TextUtils.isEmpty(mTvTip2.text.toString())) {
            mTvTip2.visibility = View.INVISIBLE
            mTvTip1.postDelayed({
                mTvTip1.visibility = View.VISIBLE
                val alphaAnimationTip1 = ObjectAnimator.ofFloat(mTvTip1, "alpha", 0f, 1f)
                alphaAnimationTip1.duration = 600
                val translateAnimationTip1 = ObjectAnimator.ofFloat(mTvTip1, "translationY", AppUtil.dp2px(mContext, 30).toFloat(), 0f)
                translateAnimationTip1.duration = 600
                val scaleXAnimationTip1 = ObjectAnimator.ofFloat(mTvTip1, "scaleX", 1f, 1.2f)
                scaleXAnimationTip1.duration = 600
                val scaleYAnimationTip1 = ObjectAnimator.ofFloat(mTvTip1, "scaleY", 1f, 1.2f)
                scaleYAnimationTip1.duration = 600
                alphaAnimationTip1.start()
                translateAnimationTip1.start()
                scaleXAnimationTip1.start()
                scaleYAnimationTip1.start()
            }, 200)

            mTvTip2.postDelayed({
                mTvTip2.visibility = View.VISIBLE
                val alphaAnimationTip2 = ObjectAnimator.ofFloat(mTvTip2, "alpha", 0f, 1f)
                alphaAnimationTip2.duration = 400
                val translateAnimationTip2 = ObjectAnimator.ofFloat(mTvTip2, "translationY", AppUtil.dp2px(mContext, 30).toFloat(), AppUtil.dp2px(mContext, 10).toFloat())
                translateAnimationTip2.duration = 400
                alphaAnimationTip2.start()
                translateAnimationTip2.start()

                val translateAnimation1Tip1 = ObjectAnimator.ofFloat(mTvTip1, "translationY", 0f, -AppUtil.dp2px(mContext, 10).toFloat())
                val scaleXAnimationTip1 = ObjectAnimator.ofFloat(mTvTip1, "scaleX", 1.2f, 1f)
                val scaleYAnimationTip1 = ObjectAnimator.ofFloat(mTvTip1, "scaleY", 1.2f, 1f)
                translateAnimation1Tip1.duration = 400
                scaleXAnimationTip1.duration = 400
                scaleYAnimationTip1.duration = 400
                translateAnimation1Tip1.start()
                scaleXAnimationTip1.start()
                scaleYAnimationTip1.start()
            }, 1000)

            getImgCloseView()?.run {
                visibility = View.INVISIBLE
                postDelayed({
                    getImgCloseView()?.visibility = View.VISIBLE
                    val alphaAnimation = ObjectAnimator.ofFloat(getImgCloseView(), "alpha", 0f, 1f)
                    alphaAnimation.duration = 300
                    alphaAnimation.start()
                }, 1200)
            }
        }
    }

    override fun doDismissAnimation() {
        val scaleXAnimationContentView = ObjectAnimator.ofFloat(mRootView, "scaleX", 1f, 0f)
        scaleXAnimationContentView.duration = 300
        val scaleYAnimationContentView = ObjectAnimator.ofFloat(mRootView, "scaleY", 1f, 0f)
        scaleYAnimationContentView.duration = 300
        mRootView.pivotX = mTipsBgView.mTriangleLeftMargin + AppUtil.dp2px(mContext, 8)
        mRootView.pivotY = 0f
        scaleXAnimationContentView.start()
        scaleYAnimationContentView.start()
        scaleXAnimationContentView.addListener(object : Animator.AnimatorListener {
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
}