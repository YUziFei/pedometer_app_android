package com.internet.yu_zi_fei_android.adapter;

/**
 * Created by 59102 on 2020/9/3.
 */
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.internet.yu_zi_fei_android.R;

import java.util.ArrayList;

import me.iwf.photopicker.utils.AndroidLifecycleUtils;

public class BasePhotoAdapter extends RecyclerView.Adapter<BasePhotoAdapter.PhotoViewHolder> {

    private ArrayList<String> photoPaths = new ArrayList<String>();
    private LayoutInflater inflater;

    private Context mContext;

    public static final int TYPE_ADD = 1;
    public static final int TYPE_PHOTO = 2;

    public int MAX = 4;
    //是否选择的监听
    private OnEditListener mOnEditListener;

    public OnEditListener getOnEditListener() {
        return mOnEditListener;
    }

    public void setOnEditListener(OnEditListener mOnEditListener) {
        this.mOnEditListener = mOnEditListener;
    }
    /**
     * 和Activity通信的接口
     */
    public interface OnEditListener {
        void onEdit(int pos);
        void onDelete(int pos);
    }
    public BasePhotoAdapter(Context mContext, ArrayList<String> photoPaths) {
        this.photoPaths = photoPaths;
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);

    }

    private boolean isShow;
    //改变显示删除的imageview，通过定义变量isShow去接收变量isManager
    public void changetShowDelImage(boolean isShow) {
        this.isShow = isShow;
        notifyDataSetChanged();
    }


    public void changetShowAddImage(int MAX) {
        this.MAX = MAX;
        notifyDataSetChanged();
    }


    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        switch (viewType) {
            case TYPE_ADD:
                itemView = inflater.inflate(R.layout.item_add, parent, false);
                break;
            case TYPE_PHOTO:
                itemView = inflater.inflate(R.layout.__picker_item_photo, parent, false);
                break;
        }
        return new PhotoViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final PhotoViewHolder holder, final int position) {

        if (getItemViewType(position) == TYPE_PHOTO) {
            // Uri uri = Uri.fromFile(new File(photoPaths.get(position)));

            boolean canLoadImage = AndroidLifecycleUtils.canLoadImage(holder.ivPhoto.getContext());

            if (canLoadImage) {
                final RequestOptions options = new RequestOptions();
                options.centerCrop()
                        .placeholder(me.iwf.photopicker.R.drawable.__picker_ic_photo_black_48dp)
                        .error(me.iwf.photopicker.R.drawable.__picker_ic_broken_image_black_48dp);
                Glide.with(mContext)
                        .load(photoPaths.get(position))
                        .apply(options)
                        .thumbnail(0.1f)
                        .into(holder.ivPhoto);
                if(isShow) {
                    holder.image_delete.setVisibility(View.VISIBLE);
                }else {
                    holder.image_delete.setVisibility(View.GONE);
                }

                holder.ivPhoto.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (null != mOnEditListener) {
                            //表示是否选择的监听
                            mOnEditListener.onEdit(holder.getAdapterPosition());
                            Log.i("StoreEnvironment","ivPhoto的onTouch被点击了");
                        }
                        return false;
                    }
                });

                //用onClick的话，会出现点击一次其实点击了两次的问题
                holder.image_delete.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        Log.i("StoreEnvironment","event = "+event.getAction());
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            if (null != mOnEditListener) {
                                //表示是否选择的监听
                                mOnEditListener.onDelete(holder.getAdapterPosition());
                                Log.i("StoreEnvironment", "image_delete的onTouch被点击了");
                            }
                            return false;
                        }
                        return false;
                    }
                });


            }

        }
    }


    @Override
    public int getItemCount() {
        int count = photoPaths.size() + 1;
        if (count > MAX) {
            count = MAX;
        }
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == photoPaths.size() && position != MAX) ? TYPE_ADD : TYPE_PHOTO;
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivPhoto;
        private View vSelected;
        private ImageView image_delete;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            ivPhoto = (ImageView) itemView.findViewById(me.iwf.photopicker.R.id.iv_photo);
            vSelected = itemView.findViewById(me.iwf.photopicker.R.id.v_selected);
            image_delete = (ImageView) itemView.findViewById(me.iwf.photopicker.R.id.v_delete);
            if (vSelected != null) {
                vSelected.setVisibility(View.GONE);
            }
        }
    }


}
