package com.internet.yu_zi_fei_android.utils;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by Administrator on 2020/4/27 0027.
 */
public class MaxTextLengthFilter implements InputFilter {

    private final Toast toast;
    private int mMaxLength;

    public MaxTextLengthFilter(Context context, int max){
        mMaxLength = max - 1;

        toast = Toast.makeText(context,"输入不能超过"+(max-1)+"位", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
    }

    public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dstart , int dend){
        int keep = mMaxLength - (dest.length() - (dend - dstart));
        if(keep < (end - start)){
            toast.show();
        }
        if(keep <= 0){
            return "";
        }else if(keep >= end - start){
            return null;
        }else{
            return source.subSequence(start,start + keep);
        }
    }
}