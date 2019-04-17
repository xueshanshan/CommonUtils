package com.star.commonutils.retention_defs;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author xueshanshan
 * @date 2019/4/17
 */

@StringDef({
        DispatcherType.DISPATCH_EDIT_IMAGE,
        DispatcherType.DISPATCH_SWIPE_DELETE,
        DispatcherType.DISPATCH_CUSTOM_VIEW,
        DispatcherType.DISPATCH_LINE_PAGER_TITLE,
})

@Retention(RetentionPolicy.SOURCE)
public @interface DispatcherType {
    String DISPATCH_EDIT_IMAGE = "edit_img";
    String DISPATCH_SWIPE_DELETE = "swipe_delete";
    String DISPATCH_CUSTOM_VIEW = "custom_view";
    String DISPATCH_LINE_PAGER_TITLE = "line_pager_title";
}
