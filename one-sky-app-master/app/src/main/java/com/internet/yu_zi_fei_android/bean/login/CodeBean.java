package com.internet.yu_zi_fei_android.bean.login;

/**
 * Created by Administrator on 2020/3/13.
 */
//验证码模型
public class CodeBean {

    /**
     * code : 200
     * msg : 档案不存在
     * data : 2
     * success : true
     */

    private int code;
    private String msg;
    private String data;
    private boolean success;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
