package com.star.common_utils.widget

import android.app.Activity
import android.view.View

/**
 *   created by xueshanshan on 2020/6/12
 */
class TipsCommonParams {
    @TipsBgView.TrianglePos
    var tipsPos: Int = TipsBgView.POS_TRIANGLE_TOP

    //activity
    var activity: Activity? = null

    //锚点View
    var anchorView: View? = null

    //偏移量  anchorView位置在屏幕左边居左对齐，在屏幕中间居中对齐(忽略偏移量)，在屏幕右面居右对齐
    var horizontalOffset = 0 //小于0往左偏移，大于0往右偏移

    //竖直方向上的偏移量， 只有在箭头居上或居下的时候起作用
    var verticalOffset = 0 //小于0往上偏移，大于0往下偏移

    //文案内容
    var content: String? = null

    //文字大小
    var contentTextSize = 0f

    //文字四边padding
    var paddingArray: IntArray? = null

    //如果还有圆角，阴影大小等一些熟悉需要调整，请直接重写getLayout，然后编写对应布局
}