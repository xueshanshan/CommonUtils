package com.star.commonutils.views

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import com.star.common_utils.utils.AppUtil
import com.star.common_utils.utils.UIUtil
import com.star.common_utils.widget.TipsBgView
import com.star.commonutils.R


/**
 *   created by xueshanshan on 2020/6/5
 */
class TipsDachePopWindow(val mContext: Context) : PopupWindow() {

    private val mTvTip1: TextView
    private val mTvTip2: TextView
    private val mImgTipClose: ImageView
    private val mTipsBgView: TipsBgView
    private var mCloseClickCallBack: (() -> Unit)? = null

    init {
        width = ViewGroup.LayoutParams.WRAP_CONTENT
        height = ViewGroup.LayoutParams.WRAP_CONTENT;
        isOutsideTouchable = false;//设置点击外部区域popWindow是否会消失
        update();

        contentView = LayoutInflater.from(mContext).inflate(R.layout.dache_tips_container, null)
        mTvTip1 = contentView.findViewById(R.id.tv_tip_1)
        mTvTip2 = contentView.findViewById(R.id.tv_tip_2)
        mImgTipClose = contentView.findViewById(R.id.img_tip_close)
        mImgTipClose.setOnClickListener {
            mCloseClickCallBack?.invoke()
            doDismissAnimation()
        }
        mTipsBgView = contentView.findViewById(R.id.tips_bg_view)
    }

    fun setCloseClickCallBack(calback: (() -> Unit)?) {
        mCloseClickCallBack = calback
    }

    fun setContent(text1: String, text2: String) {
        mTvTip1.text = text1
        mTvTip2.text = text2
    }

    override fun showAsDropDown(anchor: View?) {
        anchor?.post {
            anchor.run {
                val location = IntArray(2)
                getLocationOnScreen(location)
                var size = UIUtil.getScreenAvailAbleSize(mContext).x - location[0]
                size -= AppUtil.dp2px(context, 250)
                val leftAdd = if (size < 0) Math.abs(size).toFloat() else 0f
                if (leftAdd > 0) {
                    mTipsBgView.mTriangleLeft = leftAdd + width / 2f - mTipsBgView.getTriangleWidth() / 2f
                } else {
                    mTipsBgView.mTriangleLeft = (leftAdd + width / 2f - mTipsBgView.getTriangleWidth() / 2f - AppUtil.dp2px(context, 8))
                }
                super.showAsDropDown(anchor, 0, -AppUtil.dp2px(context, 10))
                doShowAnimation();
            }
        }
    }

    private fun doShowAnimation() {
        val scaleXAnimationContentView = ObjectAnimator.ofFloat(contentView, "scaleX", 0f, 1f)
        scaleXAnimationContentView.duration = 300
        val scaleYAnimationContentView = ObjectAnimator.ofFloat(contentView, "scaleY", 0f, 1f)
        scaleYAnimationContentView.duration = 300
        contentView.pivotX = mTipsBgView.mTriangleLeft + mTipsBgView.getTriangleWidth() / 2
        contentView.pivotY = 0f
        scaleXAnimationContentView.start()
        scaleYAnimationContentView.start()

        mTvTip1.visibility = View.INVISIBLE
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

            mImgTipClose.visibility = View.INVISIBLE
            mImgTipClose.postDelayed({
                mImgTipClose.visibility = View.VISIBLE
                val alphaAnimation = ObjectAnimator.ofFloat(mImgTipClose, "alpha", 0f, 1f)
                alphaAnimation.duration = 300
                alphaAnimation.start()
            }, 1200)
        }
    }

    fun doDismissAnimation() {
        val scaleXAnimationContentView = ObjectAnimator.ofFloat(contentView, "scaleX", 1f, 0f)
        scaleXAnimationContentView.duration = 300
        val scaleYAnimationContentView = ObjectAnimator.ofFloat(contentView, "scaleY", 1f, 0f)
        scaleYAnimationContentView.duration = 300
        contentView.pivotX = mTipsBgView.mTriangleLeft + mTipsBgView.getTriangleWidth() / 2
        contentView.pivotY = 0f
        scaleXAnimationContentView.start()
        scaleYAnimationContentView.start()
        scaleXAnimationContentView.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                dismiss()
            }

            override fun onAnimationCancel(animation: Animator?) {

            }

            override fun onAnimationStart(animation: Animator?) {

            }

        })
    }
}