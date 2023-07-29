package com.internet.yu_zi_fei_android.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.internet.yu_zi_fei_android.R;


/**
 * 参考：https://github.com/gittjy/LoadingDialog
 *
 * 本加载框使用方式
 * LoadingDailog.Builder builder2 = new LoadingDailog.Builder(getActivity())
 *                 .setShowMessage(false)
 *                 .setCancelable(false);
 *
 *         final LoadingDailog dialog2=builder2.create();
 *         dialog2.show();
 *         new Handler().postDelayed(new Runnable() {
 *             public void run() {
 *                 dialog2.dismiss();
 *             }
 *         }, 10000);
 */

public class LoadingDailog extends Dialog {

    public LoadingDailog(Context context) {
        super(context);
    }

    public LoadingDailog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder {

        private Context context;
        private String message;
        private boolean isShowMessage = true;
        private boolean isCancelable = false;
        private boolean isCancelOutside = false;

        public Builder(Context context) {
            this.context = context;
        }

        /**
         * 设置提示信息
         *
         * @param message
         * @return
         */

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        /**
         * 设置是否显示提示信息
         *
         * @param isShowMessage
         * @return
         */
        public Builder setShowMessage(boolean isShowMessage) {
            this.isShowMessage = isShowMessage;
            return this;
        }

        /**
         * 设置是否可以按返回键取消
         *
         * @param isCancelable
         * @return
         */

        public Builder setCancelable(boolean isCancelable) {
            this.isCancelable = isCancelable;
            return this;
        }

        /**
         * 设置是否可以取消
         *
         * @param isCancelOutside
         * @return
         */
        public Builder setCancelOutside(boolean isCancelOutside) {
            this.isCancelOutside = isCancelOutside;
            return this;
        }

        public LoadingDailog create() {

            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.dialog_loading, null);
            LoadingDailog loadingDailog = new LoadingDailog(context, R.style.MyDialogStyle);
            TextView msgText = (TextView) view.findViewById(R.id.tipTextView);
            if (isShowMessage) {
                msgText.setText(message);
            } else {
                msgText.setVisibility(View.GONE);
            }
            loadingDailog.setContentView(view);
            loadingDailog.setCancelable(isCancelable);
            loadingDailog.setCanceledOnTouchOutside(isCancelOutside);
            return loadingDailog;

        }


    }
}
