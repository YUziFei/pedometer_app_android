package com.internet.yu_zi_fei_android.network;

import com.internet.yu_zi_fei_android.bean.login.CodeBean;
import com.internet.yu_zi_fei_android.bean.login.LoginBean;
import com.internet.yu_zi_fei_android.bean.login.LoginByCode;
import com.internet.yu_zi_fei_android.bean.login.LoginData;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
 //public static String BASE_URL = "http://192.168.1.12:30320/";//测试地址1
 public static String BASE_URL = "";//线上地址

 public static String URL_BUSINESS = BASE_URL + "bff/app/";  // 业务类
 public static String downloadUrl = URL_BUSINESS + "downloadFile/";


 //登录
 @POST(URL_BUSINESS + "account/login")
 Observable<LoginBean> login(@Body LoginData loginData);

 //获取短信
 @GET(URL_BUSINESS + "account/login/sendLoginCode")
 Observable<CodeBean> sendLoginSMS(@Query("phone") String phone);

 //验证码 登录
 @POST(URL_BUSINESS + "account/loginByPhone")
 Observable<LoginBean> loginByCode(@Body LoginByCode code);

}