package com.internet.yu_zi_fei_android.utils;

import android.app.Activity;
import android.view.WindowManager;

/**
 * Created by 59102 on 2020/7/3.
 */

public class BackgroundChange {

    public static void colorChangeHigh(Activity activity){
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = 0.6f;
        activity.getWindow().setAttributes(lp);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    public static void colorChangeLow(Activity activity){
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = 1.06f;
        activity.getWindow().setAttributes(lp);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }
}
