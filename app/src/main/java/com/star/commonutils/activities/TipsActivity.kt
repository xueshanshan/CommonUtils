package com.star.commonutils.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
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
        text5.setOnClickListener(this)
        text6.setOnClickListener(this)
        text7.setOnClickListener(this)
        text8.setOnClickListener(this)
        text9.setOnClickListener(this)
        mTipsDachePopView = TipsDachePopView(this@TipsActivity)
        mTipsCommonView = TipsCommonWrapperView(this@TipsActivity)
    }

    override fun onClick(v: View?) {
        v?.run {
//            mTipsDachePopView.setContent("快车·优享·出租车·专车·豪华车·优享", "都叫“打车”了!")
//            mTipsDachePopView.show {
//                tipsPos = POS_TRIANGLE_TOP
//                activity = this@TipsActivity
//                anchorView = v
//            }
            var pos = TipsBgView.POS_TRIANGLE_TOP
            when (id) {
                R.id.text1, R.id.text2, R.id.text3, R.id.text5 -> {
                    pos = TipsBgView.POS_TRIANGLE_TOP
                }
                R.id.text4 -> {
                    pos = TipsBgView.POS_TRIANGLE_LEFT
                }
                R.id.text6 -> {
                    pos = TipsBgView.POS_TRIANGLE_RIGHT
                }
                R.id.text7, R.id.text8, R.id.text9 -> {
                    pos = TipsBgView.POS_TRIANGLE_BOTTOM
                }
            }
            mTipsCommonView.show {
                tipsPos = pos
                activity = this@TipsActivity
                anchorView = v
                content = "这是新手引导"
            }
        }
    }
}
