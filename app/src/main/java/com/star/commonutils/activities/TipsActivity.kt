package com.star.commonutils.activities

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.star.common_utils.utils.AppUtil
import com.star.common_utils.widget.TipsBgView
import com.star.common_utils.widget.TipsCommonWrapperView
import com.star.commonutils.R
import com.star.commonutils.views.TipsDachePopView
import kotlinx.android.synthetic.main.activity_tips.*

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
        text1.setOnClickListener(this)
        text2.setOnClickListener(this)
        text3.setOnClickListener(this)
        text4.setOnClickListener(this)
        text6.setOnClickListener(this)
        text7.setOnClickListener(this)
        text8.setOnClickListener(this)
        text9.setOnClickListener(this)
        text10.setOnClickListener(this)
        text11.setOnClickListener(this)
        mTipsDachePopView = TipsDachePopView(this@TipsActivity)
        mTipsDachePopView.setContent("快车·优享·出租车·专车·豪华车·优享", "都叫“打车”了!")
        mTipsCommonView = TipsCommonWrapperView(this@TipsActivity)
    }

    override fun onClick(v: View?) {
        v?.run {
//            mTipsDachePopView.show {
//                tipsPos = TipsBgView.POS_TRIANGLE_TOP
//                activity = this@TipsActivity
//                anchorView = v
//            }
            when (id) {
                R.id.text1, R.id.text2, R.id.text3 -> {
                    mTipsCommonView.show {
                        tipsPos = TipsBgView.POS_TRIANGLE_TOP
                        verticalOffset = AppUtil.dp2px(this@TipsActivity, 10)
                        activity = this@TipsActivity
                        anchorView = v
                        content = "三角在上，整体往下偏移10dp"
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
