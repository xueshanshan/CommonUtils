package com.star.common_utils.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.IntDef;
import android.text.TextUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author xueshanshan
 * @date 2018/12/26
 */
public class ImageUtil {
    public static final int decodeMode_min_floor = 0; //取小值，并floor，质量最好
    public static final int decodeMode_min_round = 1; //取小值，并四舍五入，质量次之
    public static final int decodeMode_min_ceil = 2; //取小值，并ceil，质量更次
    public static final int decodeMode_max_floor = 3; //取大值，并floor，质量次之
    public static final int decodeMode_max_round = 4; //取大值，并四舍五入，质量更次
    public static final int decodeMode_max_ceil = 5; //取大值，并ceil，质量最差

    @IntDef({decodeMode_min_floor, decodeMode_min_round, decodeMode_min_ceil,
            decodeMode_max_floor, decodeMode_max_round, decodeMode_max_ceil})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ImgDecodeMode {}

    private ImageUtil() {
    }

    public static Bitmap decodeResource(Context context, Integer imgResID, int width, int height) {
        return decodeResource(context, imgResID, width, height, decodeMode_min_floor);
    }

    public static Bitmap decodeResource(Context context, Integer imgResID, int width, int height, @ImgDecodeMode int decodeMode) {
        // 把原图片读成Bitmap，如果太大，就抽样读取，使分辨率小点，Bitmap占内存小点
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), imgResID, options);
        options.inSampleSize = getSampleSize(options.outWidth, options.outHeight, width, height, decodeMode);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(context.getResources(), imgResID, options);
    }

    public static Bitmap decodeFile(String filePath, int width, int height) {
        return decodeFile(filePath, width, height, true, decodeMode_min_floor);
    }

    public static Bitmap decodeFile(String filePath, int width, int height, boolean isRGB565, @ImgDecodeMode int decodeMode) {
        if (TextUtils.isEmpty(filePath) || !new File(filePath).exists()) return null;
        // 把原图片读成Bitmap，如果太大，就抽样读取，使分辨率小点，Bitmap占内存小点
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = getSampleSize(options.outWidth, options.outHeight, width, height, decodeMode);
        options.inJustDecodeBounds = false;
        if (isRGB565) {
            options.inPreferredConfig = Bitmap.Config.RGB_565;
        }
        return BitmapFactory.decodeFile(filePath, options);
    }

    public static int getSampleSize(int resWidth, int resHeight, int targetWidth, int targetHeight, @ImgDecodeMode int decodeMode) {
        int sampleSize = 1;
        if (targetWidth <= 0) targetWidth = resWidth;
        if (targetHeight <= 0) targetHeight = resHeight;
        float widthRatio = 1.0f * resWidth / targetWidth;
        float heightRatio = 1.0f * resHeight / targetHeight;
        switch (decodeMode) {
            case decodeMode_min_floor:
                sampleSize = (int) Math.floor(Math.min(widthRatio, heightRatio));
                break;
            case decodeMode_min_round:
                sampleSize = (int) (Math.min(widthRatio, heightRatio) + 0.6f);
                break;
            case decodeMode_min_ceil:
                sampleSize = (int) Math.ceil(Math.min(widthRatio, heightRatio));
                break;
            case decodeMode_max_floor:
                sampleSize = (int) Math.floor(Math.max(widthRatio, heightRatio));
                break;
            case decodeMode_max_round:
                sampleSize = (int) (Math.max(widthRatio, heightRatio) + 0.6f);
                break;
            case decodeMode_max_ceil:
                sampleSize = (int) Math.ceil(Math.max(widthRatio, heightRatio));
                break;
        }
        return sampleSize;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    public static Bitmap cropImageByCenter(Bitmap origBmp, int destWidth, int destHeight) {
        if (origBmp == null) {
            return null;
        }
        Bitmap destBmp = Bitmap.createBitmap(destWidth, destHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(destBmp);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0,
                Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        int resW = origBmp.getWidth();
        int resH = origBmp.getHeight();

        int l, r, t, b;
        if (resW > destWidth) {
            l = (resW - destWidth) / 2;
            r = resW - l;
        } else {
            l = 0;
            r = resW;
        }

        if (resH > destHeight) {
            t = (resH - destHeight) / 2;
            b = resH - t;
        } else {
            t = 0;
            b = resH;
        }

        Rect src = new Rect(l, t, r, b);
        RectF dst = new RectF(0, 0, r - l, b - t);
        canvas.drawBitmap(origBmp, src, dst, new Paint());

        return destBmp;
    }

    public static Bitmap generateBitmapByMatrix(Bitmap origBmp, int destWidth, int destHeight, Matrix matrix) {
        Bitmap destBmp;
        if (origBmp == null) {
            destBmp = Bitmap.createBitmap(destWidth, destHeight, Bitmap.Config.ARGB_8888);
        } else {
            destBmp = Bitmap.createBitmap(destWidth, destHeight, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(destBmp);
            canvas.setDrawFilter(new PaintFlagsDrawFilter(0,
                    Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
            canvas.drawBitmap(origBmp, matrix, new Paint());
        }
        return destBmp;
    }

    public static boolean saveBitmap(Bitmap bitmap, String filename) {
        return saveBitmap(bitmap, filename, Bitmap.CompressFormat.PNG, 100);
    }

    public static boolean saveBitmap(Bitmap bitmap, String filename, Bitmap.CompressFormat format, int quality) {
        boolean status = true;
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filename);
            bitmap.compress(format, quality, out);
        } catch (FileNotFoundException e) {
            status = false;
        } finally {
            IOUtil.close(out);
        }
        return status;
    }
}
