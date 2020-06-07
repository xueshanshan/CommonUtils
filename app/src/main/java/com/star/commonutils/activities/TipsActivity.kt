package com.star.commonutils.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.star.common_utils.widget.TipsBgView
import com.star.common_utils.widget.TipsCommonView
import com.star.commonutils.R
import com.star.commonutils.views.TipsDachePopView
import kotlinx.android.synthetic.main.activity_tips.*

class TipsActivity : BaseActivity(), View.OnClickListener {

    private lateinit var mTipsDachePopView: TipsDachePopView
    private lateinit var mTipsCommonView: TipsCommonView

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
    }

    override fun onClick(v: View?) {
        v?.run {
            mTipsDachePopView = TipsDachePopView(this@TipsActivity)
            mTipsDachePopView.show(this@TipsActivity, v)
//            mTipsCommonView = TipsCommonView(this@TipsActivity)
//            mTipsCommonView.show(this@TipsActivity, "这是很长的文案很长的文案很长的文案很长的文案", v, TipsBgView.POS_TRIANGLE_TOP)
        }
    }
}
