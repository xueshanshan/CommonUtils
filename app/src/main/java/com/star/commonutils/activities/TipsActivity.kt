package com.star.commonutils.activities

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import com.star.common_utils.utils.AppUtil
import com.star.common_utils.widget.TipsBgView
import com.star.common_utils.widget.TipsCommonWrapperView
import com.star.commonutils.R
import com.star.commonutils.views.TipsDachePopView

class TipsActivity : BaseActivity(), View.OnClickListener {

    private lateinit var mTipsDachePopView: TipsDachePopView
    private lateinit var mTipsCommonView: TipsCommonWrapperView

    companion object {
        fun showActivity(context: Context) {
            val intent = Intent(context, TipsActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tips)
        findViewById<TextView>(R.id.text1).setOnClickListener(this)
        findViewById<TextView>(R.id.text2).setOnClickListener(this)
        findViewById<TextView>(R.id.text3).setOnClickListener(this)
        findViewById<TextView>(R.id.text4).setOnClickListener(this)
        findViewById<TextView>(R.id.text6).setOnClickListener(this)
        findViewById<TextView>(R.id.text7).setOnClickListener(this)
        findViewById<TextView>(R.id.text8).setOnClickListener(this)
        findViewById<TextView>(R.id.text9).setOnClickListener(this)
        findViewById<TextView>(R.id.text10).setOnClickListener(this)
        findViewById<TextView>(R.id.text11).setOnClickListener(this)
        val tipBgView = findViewById<TipsBgView>(R.id.tips_bg_view)
        tipBgView.mStrokeColor = Color.CYAN
        tipBgView.mStrokeWidth = AppUtil.dp2px(this,2).toFloat()
        tipBgView.mStartColor = Color.RED
        tipBgView.mEndColor = Color.BLUE
//        tipBgView.mShadowRadius = 0f
        tipBgView.doInvalidate()
    }

    override fun onClick(v: View?) {
        v?.run {
//            mTipsDachePopView = TipsDachePopView(this@TipsActivity)
//            mTipsDachePopView.setContent("快车·优享·出租车·专车·豪华车·优享", "都叫“打车”了!")
//            mTipsDachePopView.show {
//                tipsPos = TipsBgView.POS_TRIANGLE_TOP
//                activity = this@TipsActivity
//                anchorView = v
//            }
            mTipsCommonView = TipsCommonWrapperView(this@TipsActivity)
            mTipsCommonView.setDismissCallback { closeClick ->
                Log.d("xueshanshan", "closeClick = $closeClick")
            }
            when (id) {
                R.id.text1, R.id.text2, R.id.text3 -> {
                    mTipsCommonView.show {
                        tipsPos = TipsBgView.POS_TRIANGLE_TOP
                        verticalOffset = AppUtil.dp2px(this@TipsActivity, 10)
                        activity = this@TipsActivity
                        anchorView = v
                        content = "三角在上，整体往下偏移10dp"
                        touchOutsideClose = true
                        closeVisiable = false
                        val dp = AppUtil.dp2px(this@TipsActivity, 5)
                        paddingArray = intArrayOf(dp, dp, dp, dp)
                        cornerRadius = dp.toFloat()
                        shadowRadius = AppUtil.dp2px(this@TipsActivity, 2).toFloat()
                        triangleWidth = AppUtil.dp2px(this@TipsActivity, 13).toFloat()
                        triangleHeight = AppUtil.dp2px(this@TipsActivity, 8).toFloat()
                    }
                }
                R.id.text4 -> {
                    mTipsCommonView.show {
                        tipsPos = TipsBgView.POS_TRIANGLE_LEFT
                        horizontalOffset = AppUtil.dp2px(this@TipsActivity, 10)
                        activity = this@TipsActivity
                        anchorView = v
                        content = "三角在左，整体往右偏移10dp"
                    }
                }
                R.id.text6 -> {
                    mTipsCommonView.show {
                        tipsPos = TipsBgView.POS_TRIANGLE_RIGHT
                        horizontalOffset = AppUtil.dp2px(this@TipsActivity, -10)
                        activity = this@TipsActivity
                        anchorView = v
                        content = "三角在右，整体往左偏移10dp"
                    }
                }
                R.id.text7, R.id.text9 -> {
                    mTipsCommonView.show {
                        tipsPos = TipsBgView.POS_TRIANGLE_BOTTOM
                        verticalOffset = AppUtil.dp2px(this@TipsActivity, -10)
                        activity = this@TipsActivity
                        anchorView = v
                        content = "三角在下，整体往上偏移10dp"
                    }
                }
                R.id.text8 -> {
                    mTipsCommonView.show {
                        tipsPos = TipsBgView.POS_TRIANGLE_BOTTOM
                        verticalOffset = AppUtil.dp2px(this@TipsActivity, -10)
                        horizontalOffset = AppUtil.dp2px(this@TipsActivity, -10)
                        activity = this@TipsActivity
                        anchorView = v
                        content = "三角在下，整体往上偏移10dp, 往左偏移10dp"
                    }
                }
                R.id.text10 -> {
                    mTipsCommonView.show {
                        tipsPos = TipsBgView.POS_TRIANGLE_TOP
                        horizontalOffset = AppUtil.dp2px(this@TipsActivity, -40)
                        activity = this@TipsActivity
                        anchorView = v
                        content = "三角在上，整体往左偏移40dp"
                    }
                }
                R.id.text11 -> {
                    mTipsCommonView.show {
                        tipsPos = TipsBgView.POS_TRIANGLE_TOP
                        horizontalOffset = AppUtil.dp2px(this@TipsActivity, 40)
                        activity = this@TipsActivity
                        anchorView = v
                        content = "三角在上，整体往右偏移40dp"
                    }
                }
            }
        }
    }
}
