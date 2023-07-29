package com.internet.yu_zi_fei_android.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.internet.yu_zi_fei_android.R;
import com.internet.yu_zi_fei_android.adapter.HistoryAdapter;
import com.internet.yu_zi_fei_android.application.MyApplication;
import com.internet.yu_zi_fei_android.sqlite.bean.HistoryData;
import com.internet.yu_zi_fei_android.utils.Utils;
import com.superrecycleview.superlibrary.adapter.SuperBaseAdapter;
import com.superrecycleview.superlibrary.recycleview.ProgressStyle;
import com.superrecycleview.superlibrary.recycleview.SuperRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Peripateticism on 2021/08/11
 */
public class HistoryFragment extends Fragment {

    private Unbinder unbinder;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    LinearLayoutManager mCarManager;
    HistoryAdapter adapter;
    List<HistoryData> list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history,container,false);
        unbinder = ButterKnife.bind(this,view);
        Utils.fullScreen(getActivity());
        intRecyclerview();
        return view;
    }

    private void intRecyclerview(){
        list = HistoryData.listAll(HistoryData.class);
        //添加数据
        mCarManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false) ;
        recyclerview.setLayoutManager(mCarManager);

        adapter = new HistoryAdapter(getActivity(),list);
        recyclerview.setAdapter(adapter);

        adapter.setOnItemClickListener(new SuperBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });

    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Utils.fullScreen(getActivity());
    }
}