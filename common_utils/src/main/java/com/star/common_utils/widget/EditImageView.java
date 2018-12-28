package com.star.common_utils.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import com.star.common_utils.R;
import com.star.common_utils.utils.AppUtil;
import com.star.common_utils.utils.ImageUtil;
import com.star.common_utils.utils.MatrixUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author xueshanshan
 * @date 2018/12/25
 */
public class EditImageView extends View {

    public static final int SCALE_NO_LIMIT = 0; //无限制，根据图片尺寸进行展示
    public static final int SCALE_FIT_LONG = 1; //适应长边，即长边两端顶到边框，短边两端居中
    public static final int SCALE_FIT_SHORT = 2; //适应短边，即短边两端顶到边框，那么长边两端就会超出边框区域

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({SCALE_NO_LIMIT, SCALE_FIT_LONG, SCALE_FIT_SHORT})
    public @interface EditImageScaleLimit {}

    private static final int NONE = 0;// 初始状态
    private static final int DRAG = 1;// 拖动
    private static final int ZOOM = 2;// 缩放

    private int lineColor;
    private int lineWidth;
    private int maskColor;
    private boolean isRound;

    private @EditImageScaleLimit
    int mImageScaleLimit = SCALE_FIT_LONG;
    private int mCurMode;
    private int stdWidth = 200;
    private int stdHeight = 200;
    private int borderWidth;  //线框的宽
    private int borderHeight; //线框的高  宽高跟跟stdWidth stdHeight同比例的
    private int viewWidth;
    private int viewHeight;

    private PointF srcTopLeft;
    private PointF srcTopRight;
    private PointF srcBottomLeft;
    private PointF srcBottomRight;
    private PointF[] srcPoints;
    private boolean isCutHeight;
    private boolean isWidthLonger;

    private Rect centerRect; //中间图片显示区域
    private Point centerPoint;
    private int roundRadius;

    private Paint transparentPaint;
    private Paint bmpPaint;
    private Paint linePaint;

    private int rotateCount = 0;

    private Bitmap srcBitmap;
    private Matrix curMatrix; //图片当前matrix
    private Matrix savedMatrix = new Matrix(); //变动前暂存的matrix

    private PointF prev = new PointF();
    private PointF mid = new PointF();
    private float dist = 1f;

    public EditImageView(Context context) {
        this(context, null);
    }

    public EditImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EditImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.EditImageView);
        mImageScaleLimit = typedArray.getInt(R.styleable.EditImageView_fit_mode, SCALE_FIT_LONG);
        lineColor = typedArray.getColor(R.styleable.EditImageView_line_color, Color.WHITE);
        lineWidth = typedArray.getInt(R.styleable.EditImageView_line_width, AppUtil.dp2px(context, 2));
        borderWidth = borderHeight = (int) typedArray.getDimension(R.styleable.EditImageView_border_width, 0);
        maskColor = typedArray.getColor(R.styleable.EditImageView_mask_color, getResources().getColor(R.color.photo_edit_cover));
        isRound = typedArray.getBoolean(R.styleable.EditImageView_is_round, true);
        typedArray.recycle();

        initPaint();
    }

    private void initPaint() {
        transparentPaint = new Paint();
        transparentPaint.setColor(Color.TRANSPARENT);
        transparentPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        bmpPaint = new Paint();
        bmpPaint.setAntiAlias(true);

        linePaint = new Paint();
        linePaint.setStrokeWidth(lineWidth);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(lineColor);
        linePaint.setAntiAlias(true);
    }

    public void init(Bitmap bitmap) {
        init(bitmap, stdWidth, stdHeight);
    }

    /**
     * 初始化方法
     *
     * @param bitmap 要被裁剪的bitmap
     * @param width  要裁剪的图片宽
     * @param height 要裁剪的图片高
     */
    public void init(Bitmap bitmap, int width, int height) {
        srcBitmap = bitmap;
        stdWidth = width;
        stdHeight = height;
        measureRect();
        srcTopLeft = new PointF(0f, 0f);
        srcTopRight = new PointF(bitmap.getWidth(), 0);
        srcBottomLeft = new PointF(0, bitmap.getHeight());
        srcBottomRight = new PointF(bitmap.getWidth(), bitmap.getHeight());
        srcPoints = new PointF[]{srcTopLeft, srcTopRight, srcBottomLeft, srcBottomRight};
    }

    public void rotateImg() {
        float centerX = centerRect.centerX();
        float centerY = centerRect.centerY();
        curMatrix.postRotate(90, centerX, centerY);
        rotateCount++;
        isWidthLonger = !isWidthLonger;
        checkView();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = getMeasuredWidth();
        viewHeight = getMeasuredHeight();
        measureRect();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            //主点按下
            case MotionEvent.ACTION_DOWN:
                savedMatrix.set(curMatrix);
                prev.set(event.getX(), event.getY());
                mCurMode = DRAG;
                break;
            //副点按下
            case MotionEvent.ACTION_POINTER_DOWN:
                dist = spacing(event);
                if (dist > 10f) {
                    savedMatrix.set(curMatrix);
                    midPoint(mid, event);
                    mCurMode = ZOOM;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mCurMode == DRAG) {
                    float dx = event.getX() - prev.x;
                    float dy = event.getY() - prev.y;
                    float[] after = adjustDxDy(dx, dy, savedMatrix);
                    curMatrix.set(savedMatrix);
                    curMatrix.postTranslate(after[0], after[1]);
                } else if (mCurMode == ZOOM) {
                    float newDist = spacing(event);
                    if (newDist > 10f) {
                        curMatrix.set(savedMatrix);
                        float tScale = newDist / dist;
                        curMatrix.postScale(tScale, tScale, mid.x, mid.y);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mCurMode = NONE;
                checkView();
                break;
        }
        invalidate();
        return true;
    }

    //两点的距离
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    //两点的中点
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    public float[] adjustDxDy(float dx, float dy, Matrix nowMatrix) {
        float afterDx = 0;
        float afterDy = 0;

        RectF loc = getBmpLocation(nowMatrix);

        //如果图片大小大于显示区域的大小，才可移动
        if (loc.width() > centerRect.width()) {
            afterDx = dx; //移动距离等于手指拖动的距离
            if (loc.left + dx >= centerRect.left) { //与左右边界比较，不能超出边界
                afterDx = centerRect.left - loc.left;
            } else if (loc.right + dx <= centerRect.right) {
                afterDx = centerRect.right - loc.right;
            }
        }

        //与x方向同理
        if (loc.height() > centerRect.height()) {
            afterDy = dy;
            if (loc.top + dy > centerRect.top) {
                afterDy = centerRect.top - loc.top;
            } else if (loc.bottom + dy < centerRect.bottom) {
                afterDy = centerRect.bottom - loc.bottom;
            }
        }
        return new float[]{afterDx, afterDy};
    }

    private RectF getBmpLocation(Matrix matrix) {
        PointF[] nowPoints = MatrixUtil.getPointFs(srcPoints, matrix);
        return MatrixUtil.getMaxRectF(nowPoints);
    }

    private void checkView() {
        checkScale();
        checkTrans();
        invalidate();
    }

    private void checkScale() {
        if (mImageScaleLimit == SCALE_NO_LIMIT) return;
        RectF loc = this.getBmpLocation(curMatrix);

        //如果最小是长边fit，即短边可以小于区域，但长边不能小于区域
        if (mImageScaleLimit == SCALE_FIT_LONG) {
            if (isWidthLonger && loc.width() < centerRect.width()) {
                curMatrix.postScale(getTargetScale(loc), getTargetScale(loc), mid.x, mid.y);
            } else if (!isWidthLonger && loc.height() < centerRect.height()) {
                curMatrix.postScale(getTargetScale(loc), getTargetScale(loc), mid.x, mid.y);
            }
            return;
        }

        //如果最小是短边fit，即短边不能小于区域，当然长边更不能小于区域
        if (mImageScaleLimit == SCALE_FIT_SHORT) {
            if (isWidthLonger && loc.height() < centerRect.height()) { //如果宽是长边，则较验短边高
                curMatrix.postScale(getTargetScale(loc), getTargetScale(loc), mid.x, mid.y);
            } else if (!isWidthLonger && loc.width() < centerRect.width()) {
                curMatrix.postScale(getTargetScale(loc), getTargetScale(loc), mid.x, mid.y);
            }
        }
    }

    private float getTargetScale(RectF loc) {
        float scaleX;
        float scaleY;
        int size;
        if (rotateCount % 2 == 0) {
            size = borderWidth;
        } else {
            size = borderHeight;
        }
        scaleX = size * 1f / loc.width();
        scaleY = size * 1f / loc.height();
        float targetScale = 1f;

        //然后进行缩放，使其能够适应相应模式,但都是居中显示
        switch (mImageScaleLimit) {
            case SCALE_FIT_LONG:
                targetScale = Math.min(scaleX, scaleY);
                break;
            case SCALE_FIT_SHORT:
                targetScale = Math.max(scaleX, scaleY);
                break;
        }
        return targetScale;
    }

    private void checkTrans() {
        RectF loc = this.getBmpLocation(curMatrix);
        float dx = 0;
        float dy = 0;

        //较验宽
        if (loc.width() <= centerRect.width()) { //如果宽小于区域，则居中
            dx = centerRect.centerX() - loc.centerX();
        } else { //如果宽大于区域，则不能留空白
            if (loc.left > centerRect.left) { //与左右边界比较，不能超出边界
                dx = centerRect.left - loc.left;
            } else if (loc.right < centerRect.right) {
                dx = centerRect.right - loc.right;
            }
        }

        //较验高，逻辑同上
        if (loc.height() <= centerRect.height()) {
            dy = centerRect.centerY() - loc.centerY();
        } else {
            if (loc.top > centerRect.top) {
                dy = centerRect.top - loc.top;
            } else if (loc.bottom < centerRect.bottom) {
                dy = centerRect.bottom - loc.bottom;
            }
        }
        curMatrix.postTranslate(dx, dy);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode != MeasureSpec.EXACTLY || heightMode != MeasureSpec.EXACTLY) {
            DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
            setMeasuredDimension(displayMetrics.widthPixels, displayMetrics.heightPixels);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画图片
        if (srcBitmap != null) {
            canvas.drawBitmap(srcBitmap, curMatrix, bmpPaint);
        }
        //需要先保存图层，以免后续CLEAR会把图片清除掉
        canvas.saveLayer(0, 0, viewWidth, viewHeight, null, Canvas.ALL_SAVE_FLAG);
        //画蒙版
        canvas.drawColor(maskColor);
        if (isRound) {
            //将中间区域透出
            canvas.drawCircle(centerPoint.x, centerPoint.y, roundRadius, transparentPaint);
            //画边框
            canvas.drawCircle(centerPoint.x, centerPoint.y, roundRadius, linePaint);
        } else {
            //将中间区域透出
            canvas.drawRect(centerRect, transparentPaint);
            //画边框
            canvas.drawRect(centerRect, linePaint);
        }
    }


    /**
     * 进行位置的计算，使得以stdWidth stdHeight为宽高的框框能够居中放置
     * 然后将图片先进行相应的缩放，适应模式，再平移到屏幕中间的位置
     */
    private void measureRect() {
        if (viewWidth == 0 || viewHeight == 0 || stdWidth == 0 || stdHeight == 0 || srcBitmap == null) {
            return;
        }
        float srcRatio = viewWidth * 1f / viewHeight;
        float targetRatio = stdWidth * 1f / stdHeight;
        isCutHeight = srcRatio < targetRatio;
        int l, r, t, b;
        if (isCutHeight) {
            if (borderWidth == 0 || borderWidth > viewWidth) {
                borderWidth = viewWidth;
            }
            borderHeight = (int) (borderWidth * stdHeight * 1f / stdWidth);

            l = (viewWidth - borderWidth) / 2;
            r = viewWidth - l;
            t = (int) ((viewHeight - borderWidth * 1f / targetRatio) / 2);
            b = viewHeight - t;
        } else {
            if (borderHeight == 0 || borderHeight > viewHeight) {
                borderHeight = viewHeight;
            }
            borderWidth = (int) (borderHeight * stdWidth * 1f / stdHeight);

            t = (viewHeight - borderHeight) / 2;
            b = viewHeight - t;
            l = (int) ((viewWidth - borderHeight * 1f * targetRatio) / 2);
            r = viewWidth - l;
        }
        centerRect = new Rect(l, t, r, b);
        centerPoint = new Point(viewWidth / 2, viewHeight / 2);
        roundRadius = Math.min(centerRect.width() / 2, centerRect.height() / 2);

        doTransform();
    }

    private void doTransform() {
        if (srcBitmap == null) {
            return;
        }
        curMatrix = new Matrix();

        int srcBitmapWidth = srcBitmap.getWidth();
        int srcBitmapHeight = srcBitmap.getHeight();
        isWidthLonger = srcBitmapWidth >= srcBitmapHeight;
        float scaleX = borderWidth * 1f / srcBitmapWidth;
        float scaleY = borderWidth * 1f / srcBitmapHeight;
        float targetScale = 1f;

        //然后进行缩放，使其能够适应相应模式,但都是居中显示
        switch (mImageScaleLimit) {
            case SCALE_FIT_LONG:
                targetScale = Math.min(scaleX, scaleY);
                curMatrix.postScale(targetScale, targetScale);
                break;
            case SCALE_FIT_SHORT:
                targetScale = Math.max(scaleX, scaleY);
                curMatrix.postScale(targetScale, targetScale);
                break;
        }

        //平移至屏幕中心位置
        float translateX = (viewWidth - srcBitmapWidth * targetScale) / 2;
        float translateY = (viewHeight - srcBitmapHeight * targetScale) / 2;
        curMatrix.postTranslate(translateX, translateY);

        postInvalidate();
    }

    public Matrix getCropMatrix() {
        Matrix sm = new Matrix();
        float rScale = 1.0f * stdWidth / centerRect.width();
        sm.postConcat(curMatrix);
        sm.postTranslate(-centerRect.left, -centerRect.top);
        sm.postScale(rScale, rScale);
        return sm;
    }

    public boolean saveBitmap(String dirPath) {
        Bitmap bitmap = ImageUtil.generateBitmapByMatrix(srcBitmap, stdWidth, stdHeight, getCropMatrix());
        return ImageUtil.saveBitmap(bitmap, dirPath + System.currentTimeMillis() + ".jpg", Bitmap.CompressFormat.JPEG, 80);
    }
}
