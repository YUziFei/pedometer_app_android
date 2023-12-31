package com.internet.yu_zi_fei_android.utils;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.internet.yu_zi_fei_android.R;
import com.internet.yu_zi_fei_android.activity.MainActivity;


public class NotificationHelper {

    private static final String CHANNEL_ID="channel_id";   //通道渠道id
    public static final String CHANEL_NAME="chanel_name"; //通道渠道名称
    public static NotificationManager notificationManager = null;

    @TargetApi(Build.VERSION_CODES.O)
    public static  void  show(Context context, String title , String contont){
        NotificationChannel channel = null;
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            //创建 通知通道  channelid和channelname是必须的
            channel = new NotificationChannel(CHANNEL_ID, CHANEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true);//是否在桌面icon右上角展示小红点
            channel.setLightColor(Color.GREEN);//小红点颜色
            channel.setShowBadge(false); //是否在久按桌面图标时显示此渠道的通知
        }
        Notification notification;


        Intent resultIntent = new Intent(context, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("message","0");
        resultIntent.putExtras(bundle);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);           //添加为栈顶Activity
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context,2,resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //获取Notification实例   获取Notification实例有很多方法处理    在此我只展示通用的方法（虽然这种方式是属于api16以上，但是已经可以了，毕竟16以下的Android机很少了，如果非要全面兼容可以用）
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            //向上兼容 用Notification.Builder构造notification对象
            notification = new Notification.Builder(context,CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentText(contont)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.img_logo)
                    .setColor(Color.parseColor("#FEDA26"))
                    .setContentIntent(resultPendingIntent)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.mipmap.img_logo))
                    .setTicker("000")
                    .build();
        }else {
            //向下兼容 用NotificationCompat.Builder构造notification对象
            notification = new NotificationCompat.Builder(context)
                    .setContentTitle(title)
                    .setContentText(contont)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.img_logo)
                    .setColor(Color.parseColor("#FEDA26"))
                    .setContentIntent(resultPendingIntent)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.mipmap.img_logo))
                    .setTicker("000")
                    .build();
        }

        //发送通知
        int  notifiId=1;
        //创建一个通知管理器
         notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(notifiId,notification);

    }
}
