package com.internet.yu_zi_fei_android.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.githang.statusbar.StatusBarCompat;
import com.internet.yu_zi_fei_android.R;
import com.internet.yu_zi_fei_android.activity.login.LoginActivity;
import com.internet.yu_zi_fei_android.application.ActivityCollector;
import com.internet.yu_zi_fei_android.listener.PermissionListener;
import com.internet.yu_zi_fei_android.utils.BackgroundChange;
import com.pedaily.yc.ycdialoglib.fragment.CustomDialogFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2020/3/3.
 */

public class BaseActivity extends AppCompatActivity {
    private static PermissionListener mListener;
    public static Activity activity ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //v7包下去除标题栏代码：
        getSupportActionBar().hide();
       // MyApplication.setWindowStatusBarColor(this, R.color.tab_textColorSelect);
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.white), true);
        activity = this ;
        ActivityCollector.addActivity(this);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);

    }

    public  void showCustomDialog(String title, String content, String ok, String cancel) {
        final CustomDialogFragment dialog = new CustomDialogFragment();

        dialog.setFragmentManager(getSupportFragmentManager());
        dialog.setTitle(title);
        dialog.setContent(content);
        dialog.setCancelContent(cancel);
        dialog.setOkContent(ok);
        dialog.setDimAmount(0.0f);
        dialog.setTag("BottomDialog");
        dialog.setCancelOutside(true);
        dialog.setCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialogFragment.dismissDialogFragment();
                BackgroundChange.colorChangeLow(BaseActivity.this);

            }
        });
        dialog.setOkListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialogFragment.dismissDialogFragment();
                BackgroundChange.colorChangeLow(BaseActivity.this);
                startActivity(new Intent(BaseActivity.this, LoginActivity.class));
                //com.pedaily.yc.ycdialoglib.toast.MyToastUtils.showRoundRectToast("确定了");
            }
        });
        //这个高度可以自己设置，十分灵活
        //dialog.setHeight(getScreenHeight() / 2);
        dialog.show();
    }
    /**
     * 申请权限
     */
    public static void requestRuntimePermissions(
            String[] permissions, PermissionListener listener) {
        mListener = listener;
        List<String> permissionList = new ArrayList<>();
        // 遍历每一个申请的权限，把没有通过的权限放在集合中
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) !=
                    PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            } else {
                mListener.granted();
            }
        }
        // 申请权限
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(activity,
                    permissionList.toArray(new String[permissionList.size()]), 1);
        }
    }

    /**
     * 申请后的处理
     */
    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0) {
            List<String> deniedList = new ArrayList<>();
            // 遍历所有申请的权限，把被拒绝的权限放入集合
            for (int i = 0; i < grantResults.length; i++) {
                int grantResult = grantResults[i];
                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    mListener.granted();
                } else {
                    deniedList.add(permissions[i]);
                }
            }
            if (!deniedList.isEmpty()) {
                mListener.denied(deniedList);
            }
        }
    }


}
