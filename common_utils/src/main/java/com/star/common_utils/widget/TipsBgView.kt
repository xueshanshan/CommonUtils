package com.star.common_utils.widget

import android.content.Context
import android.graphics.*
import android.support.annotation.IntDef
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.star.common_utils.R
import com.star.common_utils.utils.AppUtil
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.sin

/**
 * created by xueshanshan on 2020/6/5
 * 新手引导背景  包含渐变及三角位置处理，三角位置上下左右可随意指定
 *
 * 该背景的绘制不受padding的影响
 */
class TipsBgView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RelativeLayout(context, attrs, defStyleAttr) {
    @Target(AnnotationTarget.PROPERTY, AnnotationTarget.TYPE, AnnotationTarget.VALUE_PARAMETER)
    @IntDef(POS_TRIANGLE_LEFT, POS_TRIANGLE_RIGHT, POS_TRIANGLE_TOP, POS_TRIANGLE_BOTTOM)
    @Retention(AnnotationRetention.SOURCE)
    annotation class TrianglePos

    companion object {
        const val POS_TRIANGLE_LEFT = 1
        const val POS_TRIANGLE_RIGHT = 2
        const val POS_TRIANGLE_TOP = 3
        const val POS_TRIANGLE_BOTTOM = 4
    }

    private lateinit var mShader: LinearGradient
    private var mShadowPaint: Paint? = null
    private var mStrokePaint: Paint? = null
    private val mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mPath: Path = Path()
    private lateinit var mRectF: RectF
    private var mStrokeRectF: RectF? = null

    //总宽
    private var mTotalWidth = 0f

    //总高
    private var mTotalHeight = 0f

    //三角形的位置
    @TrianglePos
    var mTrianglePos = POS_TRIANGLE_TOP

    //三角中心距离左边位置（位置居上和居下生效）
    var mTriangleLeftMargin = 0f

    //三角中心距离上边位置（位置居左和居右生效）
    var mTriangleTopMargin = 0f

    //三角宽度
    var mTriangleWidth = 0f

    //三角高度
    var mTriangleHeight = 0f

    //圆角角度
    var mCornerRadius = 0f

    //渐变开始颜色
    var mStartColor = 0

    //渐变结束颜色
    var mEndColor = 0

    //三角形边上圆角
    var mTriangleSideCornerRadius = 0f

    //三角形顶部圆角
    var mTriangleTopCornerRadius = 0f

    //阴影半径
    var mShadowRadius = 0f

    //阴影x轴偏移
    var mShadowDx = 0f

    //阴影y轴偏移
    var mShadowDy = 0f

    //阴影颜色
    var mShadowColor = 0

    //描边颜色
    var mStrokeColor = 0

    //描边宽度
    var mStrokeWidth = 0f

    fun doInvalidate() {
        initRectFs()
        initShader()
        invalidate()
    }

    init {
        mPaint.style = Paint.Style.FILL

        val ta = context.obtainStyledAttributes(attrs, R.styleable.TipsBgView)
        mTriangleLeftMargin = ta.getDimension(R.styleable.TipsBgView_triangle_left_margin, AppUtil.dp2px(context, 20).toFloat())
        mTriangleTopMargin = ta.getDimension(R.styleable.TipsBgView_triangle_top_margin, AppUtil.dp2px(context, 20).toFloat())
        mTriangleWidth = ta.getDimension(R.styleable.TipsBgView_triangle_width, AppUtil.dp2px(context, 15).toFloat())
        mTriangleHeight = ta.getDimension(R.styleable.TipsBgView_triangle_height, AppUtil.dp2px(context, 8).toFloat())
        mCornerRadius = ta.getDimension(R.styleable.TipsBgView_corner_radius, AppUtil.dp2px(context, 10).toFloat())
        mStartColor = ta.getColor(R.styleable.TipsBgView_start_color, Color.parseColor("#FF9862"))
        mEndColor = ta.getColor(R.styleable.TipsBgView_end_color, Color.parseColor("#FF5B33"))
        mShadowColor = ta.getColor(R.styleable.TipsBgView_shadow_color, Color.parseColor("#30092847"))
        mTrianglePos = ta.getInt(R.styleable.TipsBgView_triangle_pos, POS_TRIANGLE_TOP)
        mTriangleSideCornerRadius = ta.getDimension(R.styleable.TipsBgView_triangle_side_corners_radius, AppUtil.dp2px(context, 3).toFloat())
        mTriangleTopCornerRadius = ta.getDimension(R.styleable.TipsBgView_triangle_top_corners_radius, AppUtil.dp2px(context, 2).toFloat())
        mShadowRadius = ta.getDimension(R.styleable.TipsBgView_shadow_radius, 0f)
        mShadowDx = ta.getDimension(R.styleable.TipsBgView_shadow_dx, 0f)
        mShadowDy = ta.getDimension(R.styleable.TipsBgView_shadow_dy, 0f)
        mStrokeColor = ta.getColor(R.styleable.TipsBgView_stroke_color, Color.TRANSPARENT)
        mStrokeWidth = ta.getDimension(R.styleable.TipsBgView_stroke_width, 0f)
        ta.recycle()
    }


    override fun dispatchDraw(canvas: Canvas?) {
        drawShadow(canvas)
        mPath.apply {
            reset()
            addRoundRect(mRectF, mCornerRadius, mCornerRadius, Path.Direction.CW)
            val left = mTriangleLeftMargin
            val top = mTriangleTopMargin
            if (mTriangleWidth > 0 && mTriangleHeight > 0) {
                when (mTrianglePos) {
                    POS_TRIANGLE_TOP -> {
                        moveTo(left - mTriangleWidth / 2 - mTriangleSideCornerRadius, mRectF.top)
                        val arcTan = atan((mTriangleHeight / (mTriangleWidth / 2)).toDouble())
                        val x = cos(arcTan) * mTriangleSideCornerRadius
                        val y = sin(arcTan) * mTriangleSideCornerRadius
                        quadTo(left - mTriangleWidth / 2, mRectF.top, (left - mTriangleWidth / 2 + x).toFloat(), (mRectF.top - y).toFloat())
                        val xTop = cos(arcTan) * mTriangleTopCornerRadius
                        val yTop = sin(arcTan) * mTriangleTopCornerRadius
                        lineTo((left - xTop).toFloat(), (mRectF.top - (mTriangleHeight - yTop)).toFloat())
                        quadTo(left, mRectF.top - mTriangleHeight, (left + xTop).toFloat(), (mRectF.top - (mTriangleHeight - yTop)).toFloat())
                        lineTo((left + mTriangleWidth / 2 - x).toFloat(), (mRectF.top - y).toFloat())
                        quadTo(left + mTriangleWidth / 2, mRectF.top, left + mTriangleWidth / 2 + mTriangleSideCornerRadius, mRectF.top)
                    }
                    POS_TRIANGLE_BOTTOM -> {
                        moveTo(left - mTriangleWidth / 2 - mTriangleSideCornerRadius, mRectF.bottom)
                        val arcTan = atan((mTriangleHeight / (mTriangleWidth / 2)).toDouble())
                        val x = cos(arcTan) * mTriangleSideCornerRadius
                        val y = sin(arcTan) * mTriangleSideCornerRadius
                        quadTo(left - mTriangleWidth / 2, mRectF.bottom, (left - mTriangleWidth / 2 + x).toFloat(), (mRectF.bottom + y).toFloat())
                        val xTop = cos(arcTan) * mTriangleTopCornerRadius
                        val yTop = sin(arcTan) * mTriangleTopCornerRadius
                        lineTo((left - xTop).toFloat(), (mRectF.bottom + mTriangleHeight - yTop).toFloat())
                        quadTo(left, mRectF.bottom + mTriangleHeight, (left + xTop).toFloat(), (mRectF.bottom + mTriangleHeight - yTop).toFloat())
                        lineTo((left + mTriangleWidth / 2 - x).toFloat(), (mRectF.bottom + y).toFloat())
                        quadTo(left + mTriangleWidth / 2, mRectF.bottom, left + mTriangleWidth / 2 + mTriangleSideCornerRadius, mRectF.bottom)
                    }
                    POS_TRIANGLE_LEFT -> {
                        moveTo(mRectF.left, top + mTriangleWidth / 2 + mTriangleSideCornerRadius)
                        val arcTan = atan((mTriangleHeight / (mTriangleWidth / 2)).toDouble())
                        val x = cos(arcTan) * mTriangleSideCornerRadius
                        val y = sin(arcTan) * mTriangleSideCornerRadius
                        quadTo(mRectF.left, top + mTriangleWidth / 2, (mRectF.left - y).toFloat(), (top + mTriangleWidth / 2 - x).toFloat())
                        val xTop = cos(arcTan) * mTriangleTopCornerRadius
                        val yTop = sin(arcTan) * mTriangleTopCornerRadius
                        lineTo((mRectF.left - mTriangleHeight + yTop).toFloat(), (top + xTop).toFloat())
                        quadTo(mRectF.left - mTriangleHeight, top, (mRectF.left - mTriangleHeight + yTop).toFloat(), (top - xTop).toFloat())
                        lineTo((mRectF.left - y).toFloat(), (top - mTriangleWidth / 2 + x).toFloat())
                        quadTo(mRectF.left, top - mTriangleWidth / 2, mRectF.left, top - mTriangleWidth / 2 - mTriangleSideCornerRadius)
                    }
                    POS_TRIANGLE_RIGHT -> {
                        moveTo(mRectF.right, top - mTriangleWidth / 2 - mTriangleSideCornerRadius)
                        val arcTan = atan((mTriangleHeight / (mTriangleWidth / 2)).toDouble())
                        val x = cos(arcTan) * mTriangleSideCornerRadius
                        val y = sin(arcTan) * mTriangleSideCornerRadius
                        quadTo(mRectF.right, top - mTriangleWidth / 2, (mRectF.right + y).toFloat(), (top - mTriangleWidth / 2 + x).toFloat())
                        val xTop = cos(arcTan) * mTriangleTopCornerRadius
                        val yTop = sin(arcTan) * mTriangleTopCornerRadius
                        lineTo((mRectF.right + mTriangleHeight - yTop).toFloat(), (top - xTop).toFloat())
                        quadTo(mRectF.right + mTriangleHeight, top, (mRectF.right + mTriangleHeight - yTop).toFloat(), (top + xTop).toFloat())
                        lineTo((mRectF.right + y).toFloat(), (top + mTriangleWidth / 2 - x).toFloat())
                        quadTo(mRectF.right, top + mTriangleWidth / 2, mRectF.right, top + mTriangleWidth / 2 + mTriangleSideCornerRadius)
                    }
                }
            }
            canvas?.drawPath(mPath, mPaint)
        }
        drawStroke(canvas)
        super.dispatchDraw(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val measureWidth = measuredWidth.toFloat()
        val measureHeight = measuredHeight.toFloat()
        if (mTotalWidth != measureWidth || mTotalHeight != measureHeight) {
            mTotalWidth = measureWidth
            mTotalHeight = measureHeight
            initRectFs()
            initShader()
        }
    }

    private fun initRectFs() {
        if (mTotalWidth <= 0 || mTotalHeight <= 0) {
            return
        }
        var left = 0f
        var top = 0f
        var right = 0f
        var bottom = 0f
        when (mTrianglePos) {
            POS_TRIANGLE_TOP -> {
                left = mShadowRadius
                top = mTriangleHeight + mShadowRadius
                right = mTotalWidth - mShadowRadius
                bottom = mTotalHeight - mShadowRadius
            }
            POS_TRIANGLE_BOTTOM -> {
                left = mShadowRadius
                top = mShadowRadius
                right = mTotalWidth - mShadowRadius
                bottom = mTotalHeight - mTriangleHeight - mShadowRadius
            }
            POS_TRIANGLE_LEFT -> {
                left = mTriangleHeight + mShadowRadius
                top = mShadowRadius
                right = mTotalWidth - mShadowRadius
                bottom = mTotalHeight - mShadowRadius
            }
            POS_TRIANGLE_RIGHT -> {
                left = mShadowRadius
                top = mShadowRadius
                right = mTotalWidth - mTriangleHeight - mShadowRadius
                bottom = mTotalHeight - mShadowRadius
            }
        }
        mRectF = RectF(left, top, right, bottom)
    }

    private fun initShader() {
        if (mTotalWidth <= 0 || mTotalHeight <= 0) {
            return
        }
        mShader = LinearGradient(0f, 0f, mTotalWidth, 0f, mStartColor, mEndColor, Shader.TileMode.CLAMP)
        mPaint.shader = mShader
    }

    private fun drawShadow(canvas: Canvas?) {
        if (mShadowRadius > 0) {
            if (mShadowPaint == null) {
                mShadowPaint = Paint()
            }
            mShadowPaint!!.setShadowLayer(mShadowRadius, mShadowDx, mShadowDy, mShadowColor)
            mShadowPaint!!.color = Color.TRANSPARENT
            //绘制阴影要关闭硬件加速
            setLayerType(View.LAYER_TYPE_SOFTWARE, null)
            canvas?.drawRoundRect(mRectF, mCornerRadius, mCornerRadius, mShadowPaint!!)
        }
    }

    private fun drawStroke(canvas: Canvas?) {
        if (mStrokeWidth > 0) {
            if (mStrokePaint == null) {
                mStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG)
                mStrokePaint!!.style = Paint.Style.STROKE
            }
            if (mStrokeRectF == null) {
                mStrokeRectF = RectF()
            }
            mStrokePaint!!.strokeWidth = mStrokeWidth
            mStrokePaint!!.color = mStrokeColor
            val offset = mStrokeWidth / 3f
            mStrokeRectF?.let {
                it.left = mRectF.left + offset
                it.top = mRectF.top + offset
                it.right = mRectF.right - offset
                it.bottom = mRectF.bottom - offset
                canvas?.drawRoundRect(it, mCornerRadius, mCornerRadius, mStrokePaint!!)
            }
        }
    }
}