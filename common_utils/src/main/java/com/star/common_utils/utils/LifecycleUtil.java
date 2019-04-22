package com.star.common_utils.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;

import com.star.common_utils.BuildConfig;

import java.util.HashSet;
import java.util.Set;

public class LifecycleUtil implements Application.ActivityLifecycleCallbacks {

    private static final String TAG = LifecycleUtil.class.getSimpleName();
    private static final boolean DEBUG = BuildConfig.DEBUG;

    private static LifecycleUtil sInstance;
    private Activity curActivity;

    /**
     * 是否在前台，false 代表不在前台，初始化为false ，对应app初始打开
     * 对于exception的activity,打开时认为在后台 ,foreground = false;
     */
    private boolean foreground = false;
    private boolean paused = true;
    private Handler handler = new Handler();
    private static final long CHECK_DELAY = 500L;
    private Application mApp;
    private String pausedClassName;
    private Runnable pauseRunnable = new Runnable() {
        @Override
        public void run() {
            if (!paused) {
                return;
            }
            if (foreground) {
                foreground = false;
                LogUtil.i(TAG, pausedClassName + " went background");
                notifyAppVisibleChanged(foreground);
            }
        }
    };

    private Set<OnAppVisibleListener> mOnAppVisibleListeners = new HashSet<>();

    private LifecycleUtil() {}

    public boolean isForeground() {
        return foreground;
    }

    public boolean isBackground() {
        return !foreground;
    }

    public static void init(Application app) {
        if (sInstance == null) {
            sInstance = new LifecycleUtil();
            sInstance.mApp = app;
            app.registerActivityLifecycleCallbacks(sInstance);
        }
    }

    public static LifecycleUtil get() {
        return sInstance;
    }

    public Activity getCurActivity() {
        return curActivity;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        LogUtil.d(TAG, "onActivityCreated: " + activity.getClass().getSimpleName());
    }

    @Override
    public void onActivityStarted(Activity activity) {
        LogUtil.d(TAG, "onActivityStarted: " + activity.getClass().getSimpleName());
    }

    @Override
    public void onActivityResumed(Activity activity) {
        LogUtil.d(TAG, "onActivityResumed: " + activity.getClass().getSimpleName());
        curActivity = activity;

        paused = false;

        handler.removeCallbacks(pauseRunnable);

        if (!foreground) {
            foregroundSuccess();
        }
    }

    public void foregroundSuccess() {
        foreground = true;
        notifyAppVisibleChanged(foreground);
    }

    public void registerAppVisibleListener(OnAppVisibleListener listener) {
        mOnAppVisibleListeners.add(listener);
    }

    public void removeAppVisibleListener(OnAppVisibleListener listener) {
        mOnAppVisibleListeners.remove(listener);
    }

    public boolean containsAppVisibleListener(OnAppVisibleListener listener) {
        return mOnAppVisibleListeners.contains(listener);
    }

    public void notifyAppVisibleChanged(boolean isForeground) {
        for (OnAppVisibleListener listener : mOnAppVisibleListeners) {
            if (isForeground) {
                listener.onForeground();
            } else {
                listener.onBackground();
            }
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        LogUtil.d(TAG, "onActivityPaused: " + activity.getClass().getSimpleName());
        curActivity = null;
        paused = true;

        handler.removeCallbacks(pauseRunnable);
        pausedClassName = activity.getClass().getSimpleName();
        handler.postDelayed(pauseRunnable, CHECK_DELAY);
    }

    @Override
    public void onActivityStopped(Activity activity) {
        LogUtil.d(TAG, "onActivityStopped: " + activity.getClass().getSimpleName());
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        LogUtil.d(TAG, "onActivitySaveInstanceState: " + activity.getClass().getSimpleName());
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        LogUtil.d(TAG, "onActivityDestroyed: " + activity.getClass().getSimpleName());
    }

    interface OnAppVisibleListener {
        void onForeground();

        void onBackground();
    }
}
