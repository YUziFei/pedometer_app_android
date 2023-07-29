package com.internet.yu_zi_fei_android.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;
import com.internet.yu_zi_fei_android.R;
import com.internet.yu_zi_fei_android.fragment.HistoryFragment;
import com.internet.yu_zi_fei_android.fragment.HomeFragment;
import com.internet.yu_zi_fei_android.fragment.SettingFragment;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    @BindView(R.id.fragment_content)
    FrameLayout fragmentContent;
    @BindView(R.id.img_home)
    ImageView imgHome;
    @BindView(R.id.tv_home_page)
    TextView tvHomePage;
    @BindView(R.id.linear_home_page)
    LinearLayout linearHomePage;
    @BindView(R.id.linear_history)
    LinearLayout linearHistory;
    @BindView(R.id.img_history)
    ImageView imgHistory;
    @BindView(R.id.tv_history)
    TextView tvHistory;
    @BindView(R.id.img_mine)
    ImageView imgMine;
    @BindView(R.id.tv_mine)
    TextView tvMine;
    @BindView(R.id.linear_mine)
    LinearLayout lineaarMine;
    @BindView(R.id.bottom_ll)
    LinearLayout bottomLl;


    FragmentManager fragmentManager;
    HomeFragment homeFragment;
    HistoryFragment historyFragment;
    SettingFragment settingFragment;
    public Context context;
    private long exitTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        ButterKnife.bind(this);
        StatusBarCompat.setStatusBarColor(MainActivity.this, getResources().getColor(R.color.textBlue), true);
        initView();
    }

    private void initView(){
        fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        showFragment(0);
    }

    @Override
    public void onBackPressed() {
        ExitApp();
    }

    public void ExitApp() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            MainActivity.this.finish();
        }
    }
    public void showFragment(int position) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        resetView(transaction);
        switch (position) {
            case 0:
                //切换到消息页面
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    transaction.add(R.id.fragment_content, homeFragment, "001");
                } else {
                    transaction.show(homeFragment);
                }
                break;
            case 1:
                if (historyFragment == null){
                    historyFragment = new HistoryFragment();
                    transaction.add(R.id.fragment_content, historyFragment, "002");
                }
                break;
            case 2:
                if (settingFragment == null) {
                    settingFragment = new SettingFragment();
                    transaction.add(R.id.fragment_content, settingFragment, "003");
                } else {
                    transaction.show(settingFragment);
                }
                break;
        }
        transaction.commit();
    }

    private void resetView(FragmentTransaction transaction) {
        if (homeFragment != null) {
            transaction.hide(homeFragment);
        }

        if (historyFragment != null){
            transaction.hide(historyFragment);
        }

        if (settingFragment != null) {
            transaction.hide(settingFragment);
        }
    }

    @OnClick({R.id.linear_home_page,R.id.linear_history,R.id.linear_mine})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.linear_home_page:
                imgHome.setImageResource(R.mipmap.image_home_ture);
                tvHomePage.setTextColor(context.getResources().getColor(R.color.colorBackground));

                imgHistory.setImageResource(R.mipmap.image_history_false);
                tvHistory.setTextColor(context.getResources().getColor(R.color.textGray));

                imgMine.setImageResource(R.mipmap.image_setting_false);
                tvMine.setTextColor(context.getResources().getColor(R.color.textGray));

                bottomLl.setBackgroundColor(this.getResources().getColor(R.color.gray001));
                showFragment(0);
                StatusBarCompat.setStatusBarColor(MainActivity.this, getResources().getColor(R.color.textBlue), true);

                break;
            case R.id.linear_history:
                imgHome.setImageResource(R.mipmap.image_home_false);
                tvHomePage.setTextColor(context.getResources().getColor(R.color.textGray));

                imgHistory.setImageResource(R.mipmap.image_history_ture);
                tvHistory.setTextColor(context.getResources().getColor(R.color.textBlue));

                imgMine.setImageResource(R.mipmap.image_setting_false);
                tvMine.setTextColor(context.getResources().getColor(R.color.textGray));

                bottomLl.setBackgroundColor(this.getResources().getColor(R.color.white));

                showFragment(1);
                StatusBarCompat.setStatusBarColor(MainActivity.this, getResources().getColor(R.color.white), true);
                break;
            case R.id.linear_mine:
                imgHome.setImageResource(R.mipmap.image_home_false);
                tvHomePage.setTextColor(context.getResources().getColor(R.color.textGray));

                imgHistory.setImageResource(R.mipmap.image_history_false);
                tvHistory.setTextColor(context.getResources().getColor(R.color.textGray));

                imgMine.setImageResource(R.mipmap.image_setting_ture);
                tvMine.setTextColor(context.getResources().getColor(R.color.textBlue));

                bottomLl.setBackgroundColor(this.getResources().getColor(R.color.white));
                showFragment(2);
                StatusBarCompat.setStatusBarColor(MainActivity.this, getResources().getColor(R.color.white), true);
                break;
        }
    }

}
