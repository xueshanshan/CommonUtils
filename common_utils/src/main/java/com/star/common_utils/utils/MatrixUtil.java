package com.star.common_utils.utils;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;

public class MatrixUtil {
    private MatrixUtil() {
    }

    /**
     * 根据矩阵中的值计算缩放平移之后的点的坐标
     *
     * @param src 原来的点
     * @param p   矩阵中的值
     * @return 返回点的坐标
     */
    public static PointF getPoint(PointF src, float[] p) {
        PointF dst = new PointF(0, 0);

        dst.x = p[0] * src.x + p[1] * src.y + p[2];
        dst.y = p[3] * src.x + p[4] * src.y + p[5];
        return dst;
    }

    /**
     * 根据矩阵计算缩放平移之后的点的坐标
     *
     * @param src 原来的点
     * @param m   矩阵
     * @return 返回点的坐标
     */
    public static PointF getPoint(PointF src, Matrix m) {
        float[] p = new float[9];
        m.getValues(p);

        return getPoint(src, p);
    }

    /**
     * 批量计算缩放旋转后点的坐标
     *
     * @param srcs 要计算的点
     * @param m    变换的矩阵
     * @return 返回对应的所有点的坐标
     */
    public static PointF[] getPointFs(PointF[] srcs, Matrix m) {
        float[] p = new float[9];
        m.getValues(p);

        PointF[] dsts = new PointF[srcs.length];
        for (int i = 0; i < srcs.length; i++) {
            dsts[i] = getPoint(srcs[i], p);
        }
        return dsts;
    }

    /**
     * 获取多个点组成的最大的矩形
     *
     * @param points 多个点的坐标
     * @return 返回组成的最大的矩形
     */
    public static RectF getMaxRectF(PointF[] points) {
        RectF rect = new RectF();
        float l = points[0].x, t = points[0].y, r = points[0].x, b = points[0].y;
        for (PointF point : points) {
            if (point.x < l) l = point.x;
            if (point.x > r) r = point.x;
            if (point.y < t) t = point.y;
            if (point.y > b) b = point.y;
        }
        rect.left = l;
        rect.top = t;
        rect.right = r;
        rect.bottom = b;
        return rect;
    }

    public static float[] anyMatrixToRect(Matrix matrix, int bmpWidth, int bmpHeight, int dstWidth, int dstHeight) {
        float[] p = new float[9];
        matrix.getValues(p);
        //旋转角度
        int degrees = 0;
        if (p[1] == 0 && p[0] < 0) degrees = 180;
        else if (p[0] == 0 && p[1] < 0) degrees = 90;
        else if (p[0] == 0 && p[1] > 0) degrees = 270;

        Matrix m2 = new Matrix();
        m2.postConcat(getInverseRotateMatrix(bmpWidth, bmpHeight, degrees));
        m2.postConcat(matrix);
        m2.getValues(p); //此时的m2即去旋转后的matrix
        //缩放比例
        float scale = p[0];
        //位移
        float dx = p[2];
        float dy = p[5];

        float width = 1f * dstWidth / scale;
        float height = 1f * dstHeight / dstWidth * width;
        float px = -1f * dx / scale;
        float py = -1f * dy / scale;

        return new float[]{degrees, px, py, width, height};
    }

    public static Matrix createMatrixByRect(float[] values, int bmpWidth, int bmpHeight, int dstWidth, int dstHeight) {
        Matrix matrix = new Matrix();
        float degrees = values[0];
        float px = values[1];
        float py = values[2];
        float width = values[3];
//		float height = values[4];

        matrix.postConcat(getRotateMatrix(bmpWidth, bmpHeight, (int) degrees));

        float scale = 1f * dstWidth / width;
        float dx = -1f * px * scale;
        float dy = -1f * py * scale;

        Matrix m2 = new Matrix();
        m2.postScale(scale, scale);
        m2.postTranslate(dx, dy);
        matrix.postConcat(m2);

        return matrix;
    }

    /*
    一张图片旋转，要求旋转后还是左上角在原点，则要按下以下中心点旋转：
    float wc = bmpWidth/2;
    float hc = bmpHeight/2;
    m.postRotate(90, hc, hc);
    m.postRotate(90, wc, wc);
    m.postRotate(90, hc, hc);
    m.postRotate(90, wc, wc);
     */
    private static Matrix getRotateMatrix(int bmpWidth, int bmpHeight, int degrees) {
        float wc = bmpWidth / 2;
        float hc = bmpHeight / 2;
        Matrix m = new Matrix();
        switch (degrees) {
            case 90:
                m.postRotate(90, hc, hc);
                break;
            case 180:
                m.postRotate(90, hc, hc);
                m.postRotate(90, wc, wc);
                break;
            case 270:
                m.postRotate(90, hc, hc);
                m.postRotate(90, wc, wc);
                m.postRotate(90, hc, hc);
                break;
            default:
                break;
        }
        return m;
    }

    private static Matrix getInverseRotateMatrix(int bmpWidth, int bmpHeight, int degrees) {
        float wc = bmpWidth / 2;
        float hc = bmpHeight / 2;
        Matrix m = new Matrix();
        switch (degrees) {
            case 90:
                m.postRotate(90, wc, wc);
                m.postRotate(90, hc, hc);
                m.postRotate(90, wc, wc);
                break;
            case 180:
                m.postRotate(90, hc, hc);
                m.postRotate(90, wc, wc);
                break;
            case 270:
                m.postRotate(90, wc, wc);
                break;
            default:
                break;
        }
        return m;
    }
}
