package com.widget.howard.mywidget.util;

import android.app.Activity;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

/**
 * Created by howard on 2016/11/4.
 */

public class IntentUtil {
    public static void startActivity(Context mContext, Class<?> mActivity) {
        mContext.startActivity(new Intent(mContext, mActivity));
    }

    public static void startActivity(Context mContext, Class<?> mActivity, Intent intent) {
        intent.setClass(mContext, mActivity);
        mContext.startActivity(intent);
    }
}
