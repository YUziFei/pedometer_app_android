package com.internet.yu_zi_fei_android.adapter;

import android.content.Context;
import com.internet.yu_zi_fei_android.R;
import com.internet.yu_zi_fei_android.sqlite.bean.HistoryData;
import com.superrecycleview.superlibrary.adapter.BaseViewHolder;
import com.superrecycleview.superlibrary.adapter.SuperBaseAdapter;

import java.util.List;

/**
 * Created by 59102 on 2020/12/8.
 */

public class HistoryAdapter extends SuperBaseAdapter<HistoryData> {

    private Context context;

    public HistoryAdapter(Context context, List<HistoryData> data) {
        super(context, data);
        this.context =  context;
    }


    @Override
    protected void convert(final BaseViewHolder holder, final HistoryData item, int position) {
        holder.setText(R.id.tv_step_number,"步数："+item.stepNumber + " 步");
        holder.setText(R.id.tv_distance,"距离："+item.distance + " 公里");
        holder.setText(R.id.tv_speed,"速度："+item.speed + " km/h");
        holder.setText(R.id.tv_time,"用时："+item.time + " s");
        holder.setText(R.id.tv_heat,"消耗："+item.heat + " 卡");
        holder.setText(R.id.tv_data,"记录时间："+item.data);
    }

    @Override
    protected int getItemViewLayoutId(int position, HistoryData item) {
        return R.layout.adapter_history_item;
    }
}
