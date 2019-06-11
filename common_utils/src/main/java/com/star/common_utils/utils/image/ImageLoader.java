package com.star.common_utils.utils.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.widget.ImageView;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;
import com.star.common_utils.BuildConfig;
import com.star.common_utils.utils.AppInfoUtil;

import okhttp3.OkHttpClient;

/**
 * @author xueshanshan
 * @date 2019/4/16
 */
public class ImageLoader {
    private static final String TAG = ImageLoader.class.getSimpleName();
    public static Picasso sPicasso;

    private ImageLoader() {}

    public static void initImageLoader(Context context) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Picasso picasso = new Picasso.Builder(context)
                .loggingEnabled(BuildConfig.DEBUG)
                .indicatorsEnabled(BuildConfig.DEBUG) //绿色(内存缓存) 蓝色(磁盘缓存) 红色(网络)
                .memoryCache(new LruCache(context))
                .downloader(new OkHttp3Downloader(okHttpClient))
                .defaultBitmapConfig(Bitmap.Config.ARGB_8888)
                .build();
        sPicasso = picasso;
        Picasso.setSingletonInstance(picasso);
    }

    public static Picasso getDefaultImageLoader() {
        return Picasso.with(AppInfoUtil.sContext);
    }

    public static void into(String url, ImageView view) {
        into(url, view, 0);
    }

    public static void into(String url, ImageView view, int placeholder) {
        if (url == null) {
            view.setImageResource(placeholder);
            return;
        }
        Picasso.with(AppInfoUtil.sContext).load(url).placeholder(placeholder).into(view);
    }

    public static void intoTransformCircle(String url, ImageView view) {
        intoTransformCircle(url, view, 0);
    }

    public static void intoTransformCircle(String url, ImageView view, int placeholder) {
        if (url == null) {
            view.setImageResource(placeholder);
            return;
        }
        Picasso.with(AppInfoUtil.sContext).load(url).placeholder(placeholder).transform(CircleTransform.getInstance()).into(view);
    }

    //将图片裁剪成圆形图片
    public static Bitmap circleBitmap(Bitmap source) {
        //获取Bitmap的宽度
        int width = source.getWidth();
        //返回一个正方形的Bitmap对象
        Bitmap bitmap = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
        //提供指定宽高的canvas
        Canvas canvas = new Canvas(bitmap);
        //提供画笔
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        //背景：在画布上绘制一个圆
        canvas.drawCircle(width / 2, width / 2, width / 2, paint);

        //设置图片相交情况下的处理方式
        //setXfermode：设置当绘制的图像出现相交情况时候的处理方式的,它包含的常用模式有哪几种
        //PorterDuff.Mode.SRC_IN 取两层图像交集部门,只显示上层图像,注意这里是指取相交叉的部分,然后显示上层图像
        //PorterDuff.Mode.DST_IN 取两层图像交集部门,只显示下层图像
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //前景：在画布上绘制一个bitmap
        canvas.drawBitmap(source, 0, 0, paint);
        source.recycle();
        return bitmap;
    }

    //对图片进行缩放
    public static Bitmap zoom(Bitmap source, float width, float height) {
        Matrix matrix = new Matrix();
        float scaleX = width / source.getWidth();
        float scaleY = height / source.getHeight();
        matrix.postScale(scaleX, scaleY);

        Bitmap bitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
        source.recycle();
        return bitmap;
    }
}
