package com.internet.yu_zi_fei_android.application;

/**
 * Created by user on 2019/7/17.
 */

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.orm.SugarContext;
import com.pedaily.yc.ycdialoglib.toast.ToastUtils;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MyApplication extends MultiDexApplication {

    public static Application app;
    private static Context context;
    public static int BACK_CODE = 100;
    public static int BACK_CHECK = 1003;
    public static int RESULT_CODE = 200;
    public static int MORE_LOGIN = 1;
    public static int REQUEST_CODE = 1001;
    public static int REQUEST_CODE_LOGIN = 1002;
    public static int ERRORLOGIN = -3;

    public static String TOKEN = null;//token值
    public static String USER = null;//用户名

    public static String VILLAGEID = "";//小区id
    public static String VILLAGENAME= "";//小区名称


    // @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        disableAPIDialog();
        app = this;
        ToastUtils.init(this);
        SugarContext.init(this); // 数据库
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }

    /**
     * 反射 禁止弹窗
     */
    private void disableAPIDialog(){
        if (Build.VERSION.SDK_INT < 28)return;
        try {
            Class clazz = Class.forName("android.app.ActivityThread");
            Method currentActivityThread = clazz.getDeclaredMethod("currentActivityThread");
            currentActivityThread.setAccessible(true);
            Object activityThread = currentActivityThread.invoke(null);
            Field mHiddenApiWarningShown = clazz.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static Context getContext(){
        return context;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }



}