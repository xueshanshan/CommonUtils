package com.star.common_utils.utils.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.star.common_utils.BuildConfig;
import com.star.common_utils.utils.AppInfoUtil;
import com.star.common_utils.utils.LogUtil;

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
                .defaultBitmapConfig(Bitmap.Config.RGB_565)
                .build();
        sPicasso = picasso;
        Picasso.setSingletonInstance(picasso);
    }

    public static Picasso getDefaultImageLoader() {
        return Picasso.with(AppInfoUtil.sContext);
    }

    public static void into(String url, ImageView view) {
        if (TextUtils.isEmpty(url)) {
            LogUtil.d(TAG, "url is empty!!!");
            return;
        }
        Picasso.with(AppInfoUtil.sContext).load(url).fit().into(view);
    }

    public static void into(String url, ImageView view, int placeholder) {
        if (TextUtils.isEmpty(url)) {
            LogUtil.d(TAG, "url is empty!!!");
            return;
        }
        Picasso.with(AppInfoUtil.sContext).load(url).placeholder(placeholder).fit().into(view);
    }

    public static void intoTransformCircle(String url, ImageView view) {
        if (TextUtils.isEmpty(url)) {
            LogUtil.d(TAG, "url is empty!!!");
            return;
        }
        Picasso.with(AppInfoUtil.sContext).load(url).transform(CircleTransform.getInstance()).fit().into(view);
    }

    public static void intoTransformCircle(String url, ImageView view, int placeholder) {
        if (TextUtils.isEmpty(url)) {
            LogUtil.d(TAG, "url is empty!!!");
            return;
        }
        Picasso.with(AppInfoUtil.sContext).load(url).placeholder(placeholder).transform(CircleTransform.getInstance()).fit().into(view);
    }

    public static void into(String url, ImageView view, Target target) {
        if (TextUtils.isEmpty(url)) {
            LogUtil.d(TAG, "url is empty!!!");
            return;
        }
        view.setTag(target);
        Picasso.with(AppInfoUtil.sContext).load(url).into(target);
    }
}
