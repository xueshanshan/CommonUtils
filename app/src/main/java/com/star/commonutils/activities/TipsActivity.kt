package com.star.commonutils.activities

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.star.commonutils.R
import com.star.commonutils.views.TipsDachePopWindow
import kotlinx.android.synthetic.main.activity_tips.*

class TipsActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mTipsDachePopWindow: TipsDachePopWindow

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
        mTipsDachePopWindow = TipsDachePopWindow(this)
    }

    override fun onClick(v: View?) {
        v?.run {
            mTipsDachePopWindow.showAsDropDown(v)
        }
    }
}
