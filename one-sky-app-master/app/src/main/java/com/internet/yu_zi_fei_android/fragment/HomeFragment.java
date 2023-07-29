package com.internet.yu_zi_fei_android.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import com.internet.yu_zi_fei_android.sqlite.bean.HistoryData;
import com.internet.yu_zi_fei_android.sqlite.bean.SettingBean;
import com.internet.yu_zi_fei_android.step.UpdateUiCallBack;
import com.internet.yu_zi_fei_android.step.service.StepService;
import com.internet.yu_zi_fei_android.utils.MyScrollView;
import com.internet.yu_zi_fei_android.utils.Utils;
import com.internet.yu_zi_fei_android.R;
import com.internet.yu_zi_fei_android.adapter.DialogListAdapter;
import com.internet.yu_zi_fei_android.application.MyApplication;
import com.pedaily.yc.ycdialoglib.fragment.BottomDialogFragment;
import com.pedaily.yc.ycdialoglib.toast.ToastUtils;
import com.superrecycleview.superlibrary.adapter.SuperBaseAdapter;
import com.superrecycleview.superlibrary.recycleview.SuperRecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by 59102 on 2020/8/25.
 */

public class HomeFragment extends Fragment {

    @BindView(R.id.tv_step_number)
    TextView tvStepNumber;
    @BindView(R.id.chronometer_time)
    Chronometer chronometer_time;
    @BindView(R.id.tv_speed)
    TextView tvSpeed;
    @BindView(R.id.tv_distance)
    TextView tvDistance;
    @BindView(R.id.tv_heat)
    TextView tvHeat;
    @BindView(R.id.btn_start)
    Button btnStart;
    @BindView(R.id.btn_end)
    Button btnEnd;
    @BindView(R.id.myScrollView)
    MyScrollView myScrollView;
    private Unbinder unbinder;

    BottomDialogFragment dialogFragment;
    private boolean villageName = true;
    private boolean timer = true;
    List<SettingBean> listSet = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        Utils.fullScreen(getActivity());
        initView();
        return view;
    }

    private void initView(){

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listSet = SettingBean.listAll(SettingBean .class); //读取本地 SQLite 数据
                if(listSet.size() > 0){
                    setupService();
                    btnStart.setVisibility(View.GONE);
                    btnEnd.setVisibility(View.VISIBLE);
                }else {
                    ToastUtils.showToast("请先设置身体数据哦！");
                }

            }
        });


        btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if (intent != null) {
                     chronometer_time.stop();
                     getActivity().stopService(intent);
                     dialog();
                 }
                btnStart.setVisibility(View.VISIBLE);
                btnEnd.setVisibility(View.GONE);
            }
        });

    }


    //保存数据
    private void saveData(){
        HistoryData data = new HistoryData();
        data.stepNumber = tvStepNumber.getText().toString().trim();
        data.time = Utils.getChronometerSeconds(chronometer_time);
        data.data = Utils.getCurrentTime("yyyy-MM-dd HH:mm:ss");
        data.speed = tvSpeed.getText().toString().trim();
        data.distance = tvDistance.getText().toString().trim();
        data.heat = tvHeat.getText().toString().trim();
        data.save();
    }
    //保存数据
    private void cleanData(){
        tvStepNumber.setText("");
        chronometer_time.setText("");
        tvSpeed.setText("");
        tvDistance.setText("");
        tvHeat.setText("");
    }



    private void initTimer(){
        chronometer_time.setBase(SystemClock.elapsedRealtime());//计时器清零
        int hour = (int) ((SystemClock.elapsedRealtime() - chronometer_time.getBase()) / 1000 / 60);
        chronometer_time.setFormat("0"+String.valueOf(hour)+":%s");
        chronometer_time.start();
        timer = false;
    }
    /**
     * 开启计步服务
     */
    private boolean isBind = false;
    Intent intent = null;
    private void setupService() {
        intent = new Intent(getActivity(), StepService.class);
        isBind = getActivity().bindService(intent, conn, Context.BIND_AUTO_CREATE);
        getActivity().startService(intent);
    }

    /**
     * 用于查询应用服务（application Service）的状态的一种interface，
     * 更详细的信息可以参考Service 和 context.bindService()中的描述，
     * 和许多来自系统的回调方式一样，ServiceConnection的方法都是进程的主线程中调用的。
     */
    ServiceConnection conn = new ServiceConnection() {
        /**
         * 在建立起于Service的连接时会调用该方法，目前Android是通过IBind机制实现与服务的连接。
         * @param name 实际所连接到的Service组件名称
         * @param service 服务的通信信道的IBind，可以通过Service访问对应服务
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            StepService stepService = ((StepService.StepBinder) service).getService();
            //设置步数监听回调
            stepService.registerCallback(new UpdateUiCallBack() {
                @Override
                public void updateUi(int stepCount) {
                    tvStepNumber.setText(stepCount+"");
                    Log.d("计步器",stepCount+"");
                    //更新UI 计算距离，速度，和热量
                    //距离，取设置的人体参数。
                    if (stepCount >= 1){
                        if (timer){
                            initTimer();
                        }
                    }



                    if (Integer.valueOf(Utils.getChronometerSeconds(chronometer_time)) >= 1) {
                        distance(stepCount);
                    }

                }
            });
        }

        /**
         * 当与Service之间的连接丢失的时候会调用该方法，
         * 这种情况经常发生在Service所在的进程崩溃或者被Kill的时候调用，
         * 此方法不会移除与Service的连接，当服务重新启动的时候仍然会调用 onServiceConnected()。
         * @param name 丢失连接的组件名称
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    private void distance(int stepCount){

       if (listSet != null && listSet.size() > 0){
           SettingBean bean = listSet.get(0);
           //区分性别 男性：身高×0.415=步距，女性：身高×0.413=步距。
           double stepSize = 0;
           if ("男".equals(bean.sex)){
             stepSize = Utils.mul(Double.valueOf(bean.height),0.415);
           }else {
               stepSize = Utils.mul(Double.valueOf(bean.height),0.413);
           }
           //距离计算
          // Double distance = Double.valueOf(stepCount) * stepSize * 0.00001;
           Double distance = Utils.mul(Double.valueOf(stepCount),stepSize) * 0.00001;
           DecimalFormat df = new DecimalFormat("#.00");
           tvDistance.setText(df.format(distance));

          // 速度计算
          Double time1 = Utils.div(Double.valueOf(Utils.getChronometerSeconds(chronometer_time)),Double.valueOf(120),2);
          Double speed = Utils.div(distance,time1,2);
          tvSpeed.setText(df.format(speed));

          /*热量计算   体重（kg）* 距离（km）* 运动系数（k）
           健走：k=0.8214
           跑步：k=1.036
           自行车：k=0.6142
           轮滑、溜冰：k=0.518
           室外滑雪：k=0.888*/
           Double het = Double.valueOf(bean.weight) * Double.valueOf(distance) * Double.valueOf(0.8214);
           tvHeat.setText(het+"");


       }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        if (isBind) {
            this.getActivity().unbindService(conn);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!MyApplication.VILLAGEID.isEmpty()){

        }
    }

    /**
     * 获取屏幕的高度（单位：px）
     *
     * @return 屏幕高px
     */
    public int getScreenHeight() {
        WindowManager windowManager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Utils.fullScreen(getActivity());
    }



    private void dialog() {
        //实例化布局
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.set_activity_sign_out,null);
        TextView cancel = view.findViewById(R.id.tv_cancel);
        TextView ok = view.findViewById(R.id.tv_ok);
        TextView text_title = view.findViewById(R.id.text_title);
        text_title.setText("确定保存当前计步数据？");
        //创建对话框
        final android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(getActivity()).create();
        dialog.setView(view);//添加布局
        dialog.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                cleanData();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //数据保存
                saveData();
            }
        });

    }

}
