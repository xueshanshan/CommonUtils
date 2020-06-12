package com.star.common_utils.widget

import android.content.Context
import android.graphics.*
import android.support.annotation.IntDef
import android.util.AttributeSet
import android.widget.RelativeLayout
import com.star.common_utils.R
import com.star.common_utils.utils.AppUtil
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.sin

/**
 * created by xueshanshan on 2020/6/5
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

    private val mContext: Context = context
    private lateinit var mShader: LinearGradient
    private var mPaint: Paint
    private var mPath: Path
    private lateinit var mRectF: RectF
    private var mRectInited = false

    //加padding的宽
    private var mTotalWidth = 0f

    //加padding的高
    private var mTotalHeight = 0f

    //三角形的位置
    @TrianglePos
    var mTrianglePos = POS_TRIANGLE_TOP
        set(value) {
            field = value
            initRectFs()
            invalidate()
        }

    //三角中心距离左边位置（位置居上和居下生效）
    var mTriangleLeftMargin = 0f
        set(value) {
            field = value
            invalidate()
        }

    //三角中心距离上边位置（位置居左和居右生效）
    var mTriangleTopMargin = 0f
        set(value) {
            field = value
            invalidate()
        }

    //三角宽度
    var mTriangleWidth = 0f
        set(value) {
            field = value
            initRectFs()
            invalidate()
        }

    //三角高度
    var mTriangleHeight = 0f
        set(value) {
            field = value
            initRectFs()
            invalidate()
        }

    //圆角角度
    var mCornerRadius = 0f
        set(value) {
            field = value
            invalidate()
        }

    //渐变开始颜色
    var mStartColor = 0
        set(value) {
            field = value
            initShader()
            invalidate()
        }

    //渐变结束颜色
    private var mEndColor = 0
        set(value) {
            field = value
            initShader()
            invalidate()
        }

    //三角形边上圆角
    private var mTriangleSideCornerRadius = 0f

    //三角形顶部圆角
    private var mTriangleTopCornerRadius = 0f

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.TipsBgView)
        mTriangleLeftMargin = ta.getDimension(R.styleable.TipsBgView_triangle_left_margin, AppUtil.dp2px(context, 20).toFloat())
        mTriangleTopMargin = ta.getDimension(R.styleable.TipsBgView_triangle_top_margin, AppUtil.dp2px(context, 20).toFloat())
        mTriangleWidth = ta.getDimension(R.styleable.TipsBgView_triangle_width, AppUtil.dp2px(context, 15).toFloat())
        mTriangleHeight = ta.getDimension(R.styleable.TipsBgView_triangle_height, AppUtil.dp2px(context, 8).toFloat())
        mCornerRadius = ta.getDimension(R.styleable.TipsBgView_corner_radius, AppUtil.dp2px(context, 10).toFloat())
        mStartColor = ta.getColor(R.styleable.TipsBgView_start_color, Color.parseColor("#FF9862"))
        mEndColor = ta.getColor(R.styleable.TipsBgView_end_color, Color.parseColor("#FF5B33"))
        mTrianglePos = ta.getInt(R.styleable.TipsBgView_triangle_pos, POS_TRIANGLE_TOP)
        mTriangleSideCornerRadius = ta.getDimension(R.styleable.TipsBgView_triangle_height, AppUtil.dp2px(context, 3).toFloat())
        mTriangleTopCornerRadius = ta.getDimension(R.styleable.TipsBgView_triangle_height, AppUtil.dp2px(context, 3).toFloat())
        ta.recycle()

        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint.style = Paint.Style.FILL
        mPath = Path()
    }


    override fun dispatchDraw(canvas: Canvas?) {
        mPath.apply {
            reset()
            addRoundRect(mRectF, mCornerRadius, mCornerRadius, Path.Direction.CW)
            val left = mRectF.left + mTriangleLeftMargin
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
                    moveTo(mRectF.left, mRectF.top + mTriangleTopMargin - mTriangleWidth / 2)
                    lineTo(mRectF.left - mTriangleHeight, mRectF.top + mTriangleTopMargin)
                    lineTo(mRectF.left, mRectF.top + mTriangleTopMargin + mTriangleWidth / 2)
                }
                POS_TRIANGLE_RIGHT -> {
                    moveTo(mRectF.right, mRectF.top + mTriangleTopMargin - mTriangleWidth / 2)
                    lineTo(mRectF.right + mTriangleHeight, mRectF.top + mTriangleTopMargin)
                    lineTo(mRectF.right, mRectF.top + mTriangleTopMargin + mTriangleWidth / 2)
                }
            }
            canvas?.drawPath(mPath, mPaint)
        }
        super.dispatchDraw(canvas)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mTotalWidth = measuredWidth.toFloat()
        mTotalHeight = measuredHeight.toFloat()
        //为了防止多次测量，导致对象多次重建的问题
        if (!mRectInited) {
            initRectFs()
            initShader()
        }
    }

    private fun initRectFs() {
        if (mTotalWidth <= 0 || mTotalHeight <= 0) {
            return
        }
        mRectInited = true
        var left = 0f
        var top = 0f
        var right = 0f
        var bottom = 0f
        when (mTrianglePos) {
            POS_TRIANGLE_TOP -> {
                left = paddingLeft.toFloat()
                top = (paddingTop + mTriangleHeight)
                right = (mTotalWidth - paddingRight)
                bottom = (mTotalHeight - paddingBottom)
            }
            POS_TRIANGLE_BOTTOM -> {
                left = paddingLeft.toFloat()
                top = paddingTop.toFloat()
                right = (mTotalWidth - paddingRight)
                bottom = (mTotalHeight - paddingBottom - mTriangleHeight)
            }
            POS_TRIANGLE_LEFT -> {
                left = (paddingLeft + mTriangleHeight)
                top = paddingTop.toFloat()
                right = (mTotalWidth - paddingRight)
                bottom = (mTotalHeight - paddingBottom)
            }
            POS_TRIANGLE_RIGHT -> {
                left = paddingLeft.toFloat()
                top = paddingTop.toFloat()
                right = (mTotalWidth - paddingRight - mTriangleHeight)
                bottom = (mTotalHeight - paddingBottom)
            }
        }
        mRectF = RectF(left, top, right, bottom)
    }

    private fun initShader() {
        if (mTotalWidth <= 0 || mTotalHeight <= 0) {
            return
        }
        mShader = LinearGradient(paddingLeft.toFloat(), 0f, mTotalWidth, 0f, mStartColor, mEndColor, Shader.TileMode.CLAMP)
        mPaint.shader = mShader
    }
}