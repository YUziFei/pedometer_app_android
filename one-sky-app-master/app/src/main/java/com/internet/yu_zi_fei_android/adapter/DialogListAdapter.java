package com.internet.yu_zi_fei_android.adapter;

import android.content.Context;
import com.internet.yu_zi_fei_android.R;
import com.superrecycleview.superlibrary.adapter.BaseViewHolder;
import com.superrecycleview.superlibrary.adapter.SuperBaseAdapter;

import java.util.List;

/**
 * Created by 59102 on 2020/9/17.
 */

public class DialogListAdapter extends SuperBaseAdapter<String> {

    private Context context;
    public DialogListAdapter(Context context, List<String> data) {
        super(context, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, String item, int position) {
        holder.setText(R.id.dialog_list_recycler_item_txet,item);
    }

    @Override
    protected int getItemViewLayoutId(int position, String item) {
        return R.layout.dialog_list_recycler_item;
    }
}
