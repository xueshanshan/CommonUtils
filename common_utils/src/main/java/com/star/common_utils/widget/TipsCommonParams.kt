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
    var activity: Activity? = null  //必传，不然展示不出来

    //锚点View
    var anchorView: View? = null  //必传，不然展示不出来

    //文案内容
    var content: String? = null  //必传，不然展示不出来

    //------------------------------以下为可选参数----------------------------------------

    //对齐基础上再 整体偏移量  anchorView位置在屏幕左边居左对齐，在屏幕中间居中对齐，在屏幕右面居右对齐
    var horizontalOffset = 0 //小于0往左偏移，大于0往右偏移

    //竖直方向上的偏移量， 只有在箭头居上或居下的时候起作用
    var verticalOffset = 0 //小于0往上偏移，大于0往下偏移

    //文字大小
    var contentTextSize = 0f

    //文字四边padding
    var paddingArray: IntArray? = null

    //三角宽度
    var triangleWidth = 0f

    //三角高度
    var triangleHeight = 0f

    //圆角大小
    var cornerRadius = 0f

    //渐变起始颜色
    var startColor = 0

    //渐变结束颜色
    var endColor = 0

    //阴影半径
    var shadowRadius = 0f

    //阴影颜色
    var shadowColor = 0

}