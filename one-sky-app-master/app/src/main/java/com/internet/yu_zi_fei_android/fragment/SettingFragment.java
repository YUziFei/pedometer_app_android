package com.internet.yu_zi_fei_android.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.internet.yu_zi_fei_android.R;
import com.internet.yu_zi_fei_android.activity.login.LoginActivity;
import com.internet.yu_zi_fei_android.adapter.DialogListAdapter;
import com.internet.yu_zi_fei_android.application.MyApplication;
import com.internet.yu_zi_fei_android.sqlite.bean.SettingBean;
import com.internet.yu_zi_fei_android.utils.Utils;
import com.pedaily.yc.ycdialoglib.fragment.BottomDialogFragment;
import com.pedaily.yc.ycdialoglib.toast.ToastUtils;
import com.superrecycleview.superlibrary.adapter.SuperBaseAdapter;
import com.superrecycleview.superlibrary.recycleview.SuperRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by 59102 on 2020/8/25.
 */

public class SettingFragment extends Fragment {
    private Unbinder unbinder;

    @BindView(R.id.edt_name)
    EditText edt_name;
    @BindView(R.id.tv_sex)
    TextView tv_sex;
    @BindView(R.id.edt_height)
    EditText edt_height;
    @BindView(R.id.edt_weight)
    EditText edt_weight;
    @BindView(R.id.btn_sure)
    Button btn_sure;

    BottomDialogFragment dialogFragment;
    String name = "",sex = "",height = "",weight = "";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine,container,false);
        unbinder = ButterKnife.bind(this,view);
        Utils.fullScreen(getActivity());
        initClick();
        initView();
        return view;
    }

    private void initView() {
        List<SettingBean> listSet = SettingBean.listAll(SettingBean .class); //读取本地 SQLite 数据
        if (listSet != null && listSet.size() > 0){
            edt_name.setText(listSet.get(0).name+"");
            tv_sex.setText(listSet.get(0).sex+"");
            edt_height.setText(listSet.get(0).height+"");
            edt_weight.setText(listSet.get(0).weight+"");
        }
    }

    private void initClick() {
        tv_sex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogFragment();
            }
        });


        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = edt_name.getText()+"";
                sex = tv_sex.getText()+"";
                height = edt_height.getText()+"";
                weight = edt_weight.getText()+"";

                if (name.isEmpty()){
                    ToastUtils.showToast("请输入姓名");
                }else if (sex.isEmpty()){
                    ToastUtils.showToast("请选择性别");
                }else if (height.isEmpty()){
                    ToastUtils.showToast("请输入身高");
                }else if (weight.isEmpty()){
                    ToastUtils.showToast("请输入体重");
                }else {
                    SettingBean settingBean = new SettingBean();
                    settingBean.name = name;
                    settingBean.sex = sex;
                    settingBean.height = height;
                    settingBean.weight = weight;
                    dialog(settingBean);
                }

            }
        });

    }

    private void dialog(final SettingBean bean) {
        //实例化布局
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.set_activity_sign_out,null);
        TextView cancel = view.findViewById(R.id.tv_cancel);
        TextView ok = view.findViewById(R.id.tv_ok);
        TextView text_title = view.findViewById(R.id.text_title);
        text_title.setText("确定保存当前身体数据吗？");
        //创建对话框
        final android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(getActivity()).create();
        dialog.setView(view);//添加布局
        dialog.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //数据保存
                bean.save();
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Utils.fullScreen(getActivity());
    }


    private void showDialogFragment() {
        final List<String> villageStrList = new ArrayList<>();
        villageStrList.add("男");
        villageStrList.add("女");
        dialogFragment = BottomDialogFragment.create(getActivity().getSupportFragmentManager())
                .setViewListener(new BottomDialogFragment.ViewListener() {
                    @Override
                    public void bindView(View v) {
                        SuperRecyclerView recyclerView = (SuperRecyclerView) v.findViewById(R.id.dialog_bottom_layout_list_recyclerView);
                        ImageView ivCancel = (ImageView) v.findViewById(R.id.dialog_bottom_layout_list_cancel);
                        recyclerView.setRefreshEnabled(false);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        DialogListAdapter mAdapter = new DialogListAdapter(getActivity(), villageStrList);
                        recyclerView.setAdapter(mAdapter);
                        mAdapter.setOnItemClickListener(new SuperBaseAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                tv_sex.setText(villageStrList.get(position-1));
                                dialogFragment.dismissDialogFragment();
                            }
                        });

                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogFragment.dismissDialogFragment();
                            }
                        });
                    }
                })
                .setLayoutRes(R.layout.dialog_bottom_layout_list)
                .setDimAmount(0.5f)
                .setTag("BottomDialog")
                .setCancelOutside(true)
                .setHeight(getScreenHeight() / 2);
        dialogFragment.show();
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

}
