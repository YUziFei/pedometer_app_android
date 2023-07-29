package com.internet.yu_zi_fei_android.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.internet.yu_zi_fei_android.R;
import com.internet.yu_zi_fei_android.activity.BaseActivity;
import com.internet.yu_zi_fei_android.utils.SaveUtils;



/*
   *  描述： 启动页
   */

public class LaunchActivity extends BaseActivity {

        /**
         * 1.延时2000ms
         * 2.判断程序是否第一次运行
         * 3.Activity全屏主题
         */

        //闪屏业延时
        private static final int HANDLER_SPLASH = 1001;
        //判断程序是否是第一次运行
        private static final String SHARE_IS_FIRST = "isFirst";


        private Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case HANDLER_SPLASH:
                        //判断程序是否是第一次运行
                     /*   if (isFirst()) {
                            startActivity(new Intent(LaunchActivity.this, GuideActivity.class));
                        } else {
                            startActivity(new Intent(LaunchActivity.this, MainActivity.class));
                        }*/
                        startActivity(new Intent(LaunchActivity.this, LoginActivity.class));
                        finish();
                        break;
                    default:
                        break;
                }
                return false;
            }

        });
    private boolean isFirstGuide ;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_launch);
            initView();

        }

        //初始化View
        private void initView() {
            //延时2000ms
            handler.sendEmptyMessageDelayed(HANDLER_SPLASH, 1000);
        }

        //判断程序是否第一次运行
        private boolean isFirst() {
            // 首次进入App，展示引导页。
            isFirstGuide = SaveUtils.readSharedSetting(LaunchActivity.this, SHARE_IS_FIRST, true);
            Log.i("Guide","isFirstGuide = "+isFirstGuide);
            if (isFirstGuide) {
                //是第一次运行
                return true;
            } else {

                return false;
            }

        }

        @Override
        public void onResume() {
            super.onResume();
        }

        @Override
        public void onPause() {
            super.onPause();
        }


}
