package com.star.common_utils.utils;

import android.text.TextUtils;
import android.util.Log;

/**
 * @author xueshanshan
 * @date 2018/12/11
 */
public class LogUtil {
    //默认log是没有方法栈的简单log
    private static boolean sIsSimpleLog = true;
    //是否是debug模式
    private static boolean sIsDebug = false;
    //全局tag, 如果不设置，则默认是调用者的类名
    private static String sGlobalTag;
    private static final String LEVEL_V = "V", LEVEL_D = "D", LEVEL_I = "I", LEVEL_E = "E";

    public static class Builder {
        private boolean mIsSimpleLog = true;
        private boolean mIsDebug = false;
        private String mGlobalTag;

        public Builder simpleLog(boolean isSimpleLog) {
            mIsSimpleLog = isSimpleLog;
            return this;
        }

        public Builder debug(boolean isDebug) {
            mIsDebug = isDebug;
            return this;
        }

        public Builder globalTag(String globalTag) {
            mGlobalTag = globalTag;
            return this;
        }

        public void init() {
            sIsSimpleLog = mIsSimpleLog;
            sIsDebug = mIsDebug;
            sGlobalTag = mGlobalTag;
        }
    }

    public static void v(String msg) {
        v(sIsSimpleLog, msg);
    }

    public static void v(boolean isSimple, String msg) {
        v(isSimple, sGlobalTag, msg);
    }

    public static void v(String tag, String msg) {
        v(sIsSimpleLog, tag, msg);
    }

    public static void v(boolean isSimple, String tag, String msg) {
        v(isSimple, tag, msg, null);
    }

    public static void v(String tag, String msg, Exception e) {
        v(sIsSimpleLog, tag, msg, e);
    }

    public static void v(boolean isSimpleLog, String tag, String msg, Exception e) {
        if (sIsDebug) {
            if (!TextUtils.isEmpty(msg)) {
                printLog(isSimpleLog, LEVEL_V, tag, msg, e);
            }
        }
    }

    public static void d(String msg) {
        d(sIsSimpleLog, msg);
    }

    public static void d(boolean isSimple, String msg) {
        d(isSimple, sGlobalTag, msg);
    }

    public static void d(String tag, String msg) {
        d(sIsSimpleLog, tag, msg);
    }

    public static void d(boolean isSimple, String tag, String msg) {
        d(isSimple, tag, msg, null);
    }

    public static void d(String tag, String msg, Exception e) {
        d(sIsSimpleLog, tag, msg, e);
    }

    public static void d(boolean isSimpleLog, String tag, String msg, Exception e) {
        if (sIsDebug) {
            if (!TextUtils.isEmpty(msg)) {
                printLog(isSimpleLog, LEVEL_D, tag, msg, e);
            }
        }
    }

    public static void i(String msg) {
        i(sIsSimpleLog, msg);
    }

    public static void i(boolean isSimple, String msg) {
        i(isSimple, sGlobalTag, msg);
    }

    public static void i(String tag, String msg) {
        i(sIsSimpleLog, tag, msg);
    }

    public static void i(boolean isSimple, String tag, String msg) {
        i(isSimple, tag, msg, null);
    }

    public static void i(String tag, String msg, Exception e) {
        i(sIsSimpleLog, tag, msg, e);
    }

    public static void i(boolean isSimpleLog, String tag, String msg, Exception e) {
        if (sIsDebug) {
            if (!TextUtils.isEmpty(msg)) {
                printLog(isSimpleLog, LEVEL_I, tag, msg, e);
            }
        }
    }

    public static void e(String msg) {
        e(sIsSimpleLog, msg);
    }

    public static void e(String msg, Exception e) {
        e(sGlobalTag, msg, e);
    }

    public static void e(boolean isSimple, String msg) {
        e(isSimple, sGlobalTag, msg);
    }

    public static void e(String tag, String msg) {
        e(sIsSimpleLog, tag, msg);
    }

    public static void e(boolean isSimple, String tag, String msg) {
        e(isSimple, tag, msg, null);
    }

    public static void e(String tag, String msg, Exception e) {
        e(sIsSimpleLog, tag, msg, e);
    }

    public static void e(boolean isSimpleLog, String tag, String msg, Exception e) {
        if (sIsDebug) {
            if (!TextUtils.isEmpty(msg)) {
                printLog(isSimpleLog, LEVEL_E, tag, msg, e);
            }
        }
    }

    //获取目标栈的方法的信息
    private static StackTraceElement getTargetStackTraceElement() {
        StackTraceElement targetStackTrace = null;
        boolean shouldTrace = false;
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement stackTraceElement : stackTrace) {
            boolean isLogMethod = stackTraceElement.getClassName().equals(LogUtil.class.getName());
            if (shouldTrace && !isLogMethod) {
                targetStackTrace = stackTraceElement;
                break;
            }
            shouldTrace = isLogMethod;
        }
        return targetStackTrace;
    }

    private static void printLog(boolean isSimpleLog, String level, String tag, String msg, Exception e) {
        if (TextUtils.isEmpty(level)) {
            return;
        }
        StackTraceElement element = getTargetStackTraceElement();
        if (TextUtils.isEmpty(tag)) {
            tag = element.getClassName();
        }
        switch (level) {
            case LEVEL_V:
                Log.v(tag, isSimpleLog ? msg : getWrapMsg(msg, element), e);
                break;
            case LEVEL_D:
                Log.d(tag, isSimpleLog ? msg : getWrapMsg(msg, element), e);
                break;
            case LEVEL_I:
                Log.i(tag, isSimpleLog ? msg : getWrapMsg(msg, element), e);
                break;
            case LEVEL_E:
                Log.e(tag, isSimpleLog ? msg : getWrapMsg(msg, element), e);
                break;
            default:
                break;
        }
    }

    //只有符合(classname.java:行号)这种格式在AndroidStudio里面才会点击跳转
    private static String getWrapMsg(String msg, StackTraceElement element) {
        return getWrapMsg(msg, element, null);
    }

    //只有符合(classname.java:行号)这种格式在AndroidStudio里面才会点击跳转
    private static String getWrapMsg(String msg, StackTraceElement element, Exception e) {
        return new StringBuffer().append(Thread.currentThread().getName()).append("\t")
                .append("(").append(element.getFileName()).append(":")
                .append(element.getLineNumber()).append(")").append("\t")
                .append(element.getMethodName()).append("()").append("\t").append(e == null ? "" : e.getMessage()).append("\n")
                .append(msg).toString();
    }
}
