package com.internet.yu_zi_fei_android.activity.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.githang.statusbar.StatusBarCompat;
import com.internet.yu_zi_fei_android.R;
import com.internet.yu_zi_fei_android.activity.BaseActivity;
import com.internet.yu_zi_fei_android.activity.MainActivity;
import com.internet.yu_zi_fei_android.application.MyApplication;
import com.internet.yu_zi_fei_android.bean.login.LoginBean;
import com.internet.yu_zi_fei_android.bean.login.LoginData;

import com.internet.yu_zi_fei_android.listener.PermissionListener;
import com.internet.yu_zi_fei_android.network.ApiEngine;
import com.internet.yu_zi_fei_android.utils.Base64Utils;
import com.internet.yu_zi_fei_android.utils.MaxTextLengthFilter;
import com.internet.yu_zi_fei_android.utils.RSAUtils;
import com.internet.yu_zi_fei_android.utils.ToastUtils;
import com.internet.yu_zi_fei_android.utils.Utils;
import com.internet.yu_zi_fei_android.widget.LoadingDailog;

import java.security.PublicKey;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 59102 on 2020/12/11.
 */

public class LoginActivity extends BaseActivity {

    @BindView(R.id.activity_login_edit_inputPhoneNum)
    EditText edit_inputPhoneNum;
    @BindView(R.id.activity_login_edit_inputPwd)
    EditText edit_VerificationCode;
    @BindView(R.id.activity_login_btn_login)
    Button loginBtn;
    @BindView(R.id.activity_login_edit_VerificationCode_visible)
    ImageView image_pwd_visible;
    @BindView(R.id.img_icon_type)
    ImageView img_icon_type;

    public int LOGINTYPE = 1;//验证码登录还是账号登录 1表示账号 2表示验证码

    private SharedPreferences sp;
    private boolean eyeOpen = false;

    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.CAMERA",
            "android.permission.WRITE_SETTINGS",
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= 23) {//判断当前系统是不是Android6.0
            requestRuntimePermissions(PERMISSIONS_STORAGE, new PermissionListener() {
                @Override
                public void granted() {
                    //权限申请通过
                }

                @Override
                public void denied(List<String> deniedList) {
                    //权限申请未通过
                    /*for (String denied : deniedList) {
                        if (denied.equals("android.permission.ACCESS_FINE_LOCATION")) {
                            ToastUtils.showCenter(LoginActivity.this,"定位失败，请检查是否打开定位权限！");
                        } else {
                            ToastUtils.showCenter(LoginActivity.this,"没有文件读写权限,请检查是否打开！");
                        }
                    }*/
                }
            });
        }

        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.lavender), true);

        SharedPreferences sp = getSharedPreferences("phone", Context.MODE_PRIVATE);
        String phone =  sp.getString("phone","");
        edit_inputPhoneNum.setText(phone);
        edit_inputPhoneNum.setFilters(new InputFilter[]{new MaxTextLengthFilter(LoginActivity.this,12)});
        edit_VerificationCode.setFilters(new InputFilter[]{new MaxTextLengthFilter(LoginActivity.this,17)});
        Selection.setSelection(edit_inputPhoneNum.getText(),edit_inputPhoneNum.getText().toString().length());
        initCodeSet();
        initListener();
        useCodeLogin();
    }
    private void initCodeSet() {
        edit_VerificationCode.addTextChangedListener(new TextChange());
    }

    private void initListener() {
        //隐藏密码
        edit_VerificationCode.setTransformationMethod(PasswordTransformationMethod.getInstance());
        loginBtn.setBackground(getResources().getDrawable(R.drawable.button_login_able));
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit_inputPhoneNum.length() > 0 && edit_VerificationCode.length() > 0) {
                    if (Utils.isPhoneNumber(edit_inputPhoneNum.getText().toString())) {
                        login(edit_inputPhoneNum.getText().toString(),edit_VerificationCode.getText().toString());
                    } else {
                        ToastUtils.showCenter(LoginActivity.this, "手机号格式不正确");
                    }
                }else{
                    if (LOGINTYPE == 1){
                        ToastUtils.showCenter(LoginActivity.this,"未输入手机号或密码");
                    }else{
                        ToastUtils.showCenter(LoginActivity.this,"未输入手机号或验证码");
                    }

                }
                // startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        });

        image_pwd_visible.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                if (eyeOpen){
                    edit_VerificationCode.setTransformationMethod(PasswordTransformationMethod.getInstance());//密码不可见
                    Selection.setSelection(edit_VerificationCode.getText(),edit_VerificationCode.getText().toString().length());
                    //密码 TYPE_CLASS_TEXT 和 TYPE_TEXT_VARIATION_PASSWORD 必须一起使用
                    //  edit_inputinputPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    image_pwd_visible.setImageResource( R.drawable.password_icon_hide);
                    eyeOpen = false ;
                }else {
                    //明文
                    // edit_inputinputPwd.setInputType( InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD );
                    edit_VerificationCode.setTransformationMethod(HideReturnsTransformationMethod.getInstance()); //密码可见
                    Selection.setSelection(edit_VerificationCode.getText(),edit_VerificationCode.getText().toString().length());
                    image_pwd_visible.setImageResource( R.drawable.password_icon_show);
                    eyeOpen = true ;
                }
            }
        });
    }

    private void usePwdLogin() {
        img_icon_type.setBackground(getResources().getDrawable(R.mipmap.icon_mobile));
        edit_VerificationCode.setText("");
        edit_VerificationCode.setHint("请输入密码");
        edit_inputPhoneNum.setHint("请输入账号");
        image_pwd_visible.setVisibility(View.VISIBLE);
        LOGINTYPE = 1;
        //隐藏密码
        edit_VerificationCode.setTransformationMethod(PasswordTransformationMethod.getInstance());
        edit_VerificationCode.setFilters(new InputFilter[]{new MaxTextLengthFilter(LoginActivity.this,17)});
    }

    private void useCodeLogin() {
        img_icon_type.setBackground(getResources().getDrawable(R.mipmap.icon_mobile));
        edit_VerificationCode.setText("");
        edit_VerificationCode.setHint("请输入验证码");
        edit_inputPhoneNum.setHint("请输入手机号");

        image_pwd_visible.setVisibility(View.INVISIBLE);
        LOGINTYPE = 2;
        //显示密码
        edit_VerificationCode.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        edit_VerificationCode.setFilters(new InputFilter[]{new MaxTextLengthFilter(LoginActivity.this,7)});
    }

    //获取登录接口
    private void login(final String account, String password) {
            //加载框
            LoadingDailog.Builder builder = new LoadingDailog.Builder(LoginActivity.this)
                    .setShowMessage(false)
                    .setCancelable(true);
            final LoadingDailog dialog = builder.create();
            dialog.show();
            String passWord = password + "," +System.currentTimeMillis();
            String encryptionPassword = encryption(passWord);
            ApiEngine.getInstance().getApiService().login(new LoginData(account,encryptionPassword))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<LoginBean>() {

                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onNext(LoginBean login) {
                            if (MyApplication.RESULT_CODE == login.code) {
                                MyApplication.TOKEN = login.data;
                                SharedPreferences sp_login = getSharedPreferences("phone", Context.MODE_PRIVATE);
                                sp_login.edit().putString("phone", edit_inputPhoneNum.getText().toString()).commit();
                                SharedPreferences sp_token = getSharedPreferences("token", Context.MODE_PRIVATE);
                                sp_token.edit().putString("token", login.data).commit();
                                if (!MyApplication.USER.equals(edit_inputPhoneNum.getText().toString())){
                                    MyApplication.VILLAGENAME = "";
                                    MyApplication.VILLAGEID = "";//切换账号登录时将之前的清空
                                }
                                MyApplication.USER = edit_inputPhoneNum.getText().toString();

                                finish();
                                startActivity(new Intent(activity, MainActivity.class));
                            }
                            ToastUtils.showCenter(LoginActivity.this,login.msg+"");

                        }

                        @Override
                        public void onError(Throwable e) {
                            ToastUtils.showCenter(LoginActivity.this,"连接不上服务器，请检查手机网络");
                            dialog.dismiss();
                        }

                        @Override
                        public void onComplete() {
                            dialog.dismiss();
                        }
                    });
    }
    //加密账号密码
    public static String encryption(String encryptionStr){
        String returnStr = "";
        try {
            // 从字符串中得到公钥
            PublicKey publicKey = RSAUtils.loadPublicKey(RSAUtils.PUBLIC_KEY);
            // 加密
            byte[] encryptByte = RSAUtils.encryptData(encryptionStr.getBytes(), publicKey);
            // 为了方便观察吧加密后的数据用base64加密转一下，要不然看起来是乱码,所以解密是也是要用Base64先转换
            returnStr = Base64Utils.encode(encryptByte);
            //returnStr = RSAUtils.encryptDataByPublicKey(encryptionStr.getBytes(), publicKey);

            Log.d("RSA加密：",returnStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnStr;
    }

    //设置edittext的输入监听
    class TextChange implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (edit_inputPhoneNum.length() >0 && edit_VerificationCode.length()>0){
                loginBtn.setTextColor(getResources().getColor(R.color.home_black));
                loginBtn.setBackground(getResources().getDrawable(R.drawable.button_login_able));
            }else{
                loginBtn.setBackground(getResources().getDrawable(R.drawable.button_login_enable));
            }
        }
    }

}
