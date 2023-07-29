package com.internet.yu_zi_fei_android.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Chronometer;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2020/3/10.
 */

public class Utils {

    /**
     * 隐藏软键盘
     *
     * @param  :上下文环境，一般为Activity实例
     * @param view    :一般为EditText
     */
    public static void hideKeyboard(View view) {
        InputMethodManager manager = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 判断手机号是否符合规范
     * @param phoneNo 输入的手机号
     * @return
     */
    public static boolean isPhoneNumber(String phoneNo) {
        if (TextUtils.isEmpty(phoneNo)) {
            return false;
        }
        if (phoneNo.length() == 11) {
            for (int i = 0; i < 11; i++) {
                if (!PhoneNumberUtils.isISODigit(phoneNo.charAt(i))) {
                    return false;
                }
            }
            String pattern = "^(13[0-9]|15[012356789]|17[013678]|18[0-9]|14[57]|19[89]|166)[0-9]{8}";
            Pattern p = Pattern.compile(pattern);
           /* Pattern p = Pattern.compile("^((13[^4,\\D])" + "|(134[^9,\\D])" +
                    "|(14[5,7])" +
                    "|(15[^4,\\D])" +
                    "|(17[3,6-8])" +
                    "|(18[0-9]))\\d{8}$");*/
            Matcher m = p.matcher(phoneNo);
            return m.matches();
        }
        return false;
    }
    //隐藏身份证中间几位
    public static String hideId(String id) {
        if (id != null) {
            StringBuilder stringBuilder = new StringBuilder(id);
            if (id.length() > 14) {
                stringBuilder.replace(6, id.length(), "************");
            }
            return stringBuilder.toString();
        } else {
            return null;
        }
    }

    //隐藏手机号中间几位
    public static String hidePhone(String phone){
        StringBuilder stringBuilder = new StringBuilder(phone);
        stringBuilder.replace(3, 7, "****");
        return stringBuilder.toString();
    }

    //根据身份证号输出年龄
    public static int IdNOToAge(String IdNO){
        int leh = IdNO.length();
        String dates="";
        if (leh == 18) {
         //   int se = Integer.valueOf(IdNO.substring(leh - 1)) % 2;
            dates = IdNO.substring(6, 10);
            SimpleDateFormat df = new SimpleDateFormat("yyyy");
            String year=df.format(new Date());
            int u=Integer.parseInt(year)-Integer.parseInt(dates);
            return u;
        }else{
            dates = IdNO.substring(6, 8);
            return Integer.parseInt(dates);
        }

    }

    //判断一个字符串是否为空
    public static String isNull(String string){
         String data = "null";
        if (string != null){
            data = string;
        }
         return data;
    }
//-distanceDay代表要获取当前N天前的日期，
 //          +distanceDay代表要获取当前N天后的日期
    public static String getOldDate(int distanceDay) {
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate = new Date();
        Calendar date = Calendar.getInstance();
        date.setTime(beginDate);
        date.set(Calendar.DATE, date.get(Calendar.DATE) - distanceDay);
        Date endDate = null;
        try {
            endDate = dft.parse(dft.format(date.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dft.format(endDate);
    }

    //获取当前日期
    public static String getCurrentDate(){
        SimpleDateFormat    formatter    =   new    SimpleDateFormat    ("yyyy-MM-dd");//设置格式
        Date    curDate    =   new    Date(System.currentTimeMillis());//获取当前时间
        String    str    =    formatter.format(curDate);
        return  str;
    }

  public static String getBirth(String date){
        String birth = "";
       if (date.length() == 8){
           birth = date.substring(0,4)+"-"+date.substring(4,6)+"-"+date.substring(6,8);
       }
       return birth;
  }


    /**
     * 获取时间
     */
    public static String getCurrentTime(String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }

    /**
     * 判断是否在时间限内
     */
    public static boolean timeLimit(String nowTime, String startTime, String endTime, String format) {
        boolean status;
        DateFormat fmt = new SimpleDateFormat(format);
        Date strbeginDate = null;//起始时间
        Date strendDate = null;//结束时间
        Date currentTime = null;//结束时间
        try {
            strbeginDate = fmt.parse(startTime);//将时间转化成相同格式的Date类型
            strendDate = fmt.parse(endTime);
            currentTime = fmt.parse(nowTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if ((currentTime.getTime() - strbeginDate.getTime()) > 0 && (strendDate.getTime() - currentTime.getTime()) > 0) {
            //使用.getTime方法把时间转化成毫秒数,然后进行比较
            status = true;
            Log.d("tainanl", "当前时间在范围内");
        } else {
            status = false;
            Log.d("tainanl", "您的操作时间已到期,请重新申请操作时间");
        }
        return status;
    }



    /**
     * 通过设置全屏，设置状态栏透明
     *
     * @param activity
     */
    public static void fullScreen(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
                Window window = activity.getWindow();
                View decorView = window.getDecorView();
                //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(option);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
                //导航栏颜色也可以正常设置
//                window.setNavigationBarColor(Color.TRANSPARENT);
            } else {
                Window window = activity.getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
                attributes.flags |= flagTranslucentStatus;
//                attributes.flags |= flagTranslucentNavigation;
                window.setAttributes(attributes);
            }
        }
    }


    /**
     * 文件转Base64.
     * @param filePath
     * @return
     */
    public static String file2Base64(String filePath) {
        FileInputStream objFileIS = null;
        try {
            objFileIS = new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream objByteArrayOS = new ByteArrayOutputStream();
        byte[] byteBufferString = new byte[1024];
        try {
            for (int readNum; (readNum = objFileIS.read(byteBufferString)) != -1; ) {
                objByteArrayOS.write(byteBufferString, 0, readNum);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String videodata = Base64.encodeToString(objByteArrayOS.toByteArray(), Base64.DEFAULT).replaceAll("[\\s*\t\n\r]", "");
        Log.d("base64",videodata);
      //  videodata.replaceAll("[\\s*\t\n\r]", "");
        return videodata;
    }





    /**
     * 生成一个0 到 count 之间的随机数
     * @param endNum
     * @return
     */
    public static int getNum(int endNum){
        if(endNum > 0){
            Random random = new Random();
            return random.nextInt(endNum);
        }
        return 0;
    }
    /**
     * 生成一个startNum 到 endNum之间的随机数(不包含endNum的随机数)
     * @param startNum
     * @param endNum
     * @return
     */
    public static int getNum(int startNum,int endNum){
        if(endNum > startNum){
            Random random = new Random();
            return random.nextInt(endNum - startNum) + startNum;
        }
        return 0;
    }
    /**
     * 生成随机大写字母
     * @return
     */
    public static String getLargeLetter(){
        Random random = new Random();
        return String.valueOf ((char) (random.nextInt(27) + 'A'));
    }
    /**
     * 生成随机大写字母字符串
     * @return
     */
    public static String getLargeLetter(int size){
        StringBuffer buffer = new StringBuffer();
        Random random = new Random();
        for(int i=0; i<size;i++){
            buffer.append((char) (random.nextInt(27) + 'A'));
        }
        return buffer.toString();
    }
    /**
     * 生成随机小写字母
     * @return
     */
    public static String getSmallLetter(){
        Random random = new Random();
        return String.valueOf ((char) (random.nextInt(27) + 'a'));
    }
    /**
     * 生成随机小写字母字符串
     * @return
     */
    public static String getSmallLetter(int size){
        StringBuffer buffer = new StringBuffer();
        Random random = new Random();
        for(int i=0; i<size;i++){
            buffer.append((char) (random.nextInt(27) + 'a'));
        }
        return buffer.toString();
    }
    /**
     * 数字与小写字母混编字符串
     * @param size
     * @return
     */
    public static String getNumSmallLetter(int size){
        StringBuffer buffer = new StringBuffer();
        Random random = new Random();
        for(int i=0; i<size;i++){
            if(random.nextInt(2) % 2 == 0){//字母
                buffer.append((char) (random.nextInt(27) + 'a'));
            }else{//数字
                buffer.append(random.nextInt(10));
            }
        }
        return buffer.toString();
    }
    /**
     * 数字与大写字母混编字符串
     * @param size
     * @return
     */
    public static String getNumLargeLetter(int size){
        StringBuffer buffer = new StringBuffer();
        Random random = new Random();
        for(int i=0; i<size;i++){
            if(random.nextInt(2) % 2 == 0){//字母
                buffer.append((char) (random.nextInt(27) + 'A'));
            }else{//数字
                buffer.append(random.nextInt(10));
            }
        }
        return buffer.toString();
    }
    /**
     * 数字与大小写字母混编字符串
     * @param size
     * @return
     */
    public static String getNumLargeSmallLetter(int size){
        StringBuffer buffer = new StringBuffer();
        Random random = new Random();
        for(int i=0; i<size;i++){
            if(random.nextInt(2) % 2 == 0){//字母
                if(random.nextInt(2) % 2 == 0){
                    buffer.append((char) (random.nextInt(27) + 'A'));
                }else{
                    buffer.append((char) (random.nextInt(27) + 'a'));
                }
            }else{//数字
                buffer.append(random.nextInt(10));
            }
        }
        return buffer.toString();
    }



    /**
     *
     * @param cmt  Chronometer控件
     * @return 小时+分钟+秒数  的所有秒数
     */
    public  static String getChronometerSeconds(Chronometer cmt) {
        int totalss = 0;
        String string = cmt.getText().toString();
       // if(string.length()==7){

            String[] split = string.split(":");
            String string2 = split[0];
            int hour = Integer.parseInt(string2);
            int Hours =hour*3600;
            String string3 = split[1];
            int min = Integer.parseInt(string3);
            int Mins =min*60;
            int  SS =Integer.parseInt(split[2]);
            totalss = Hours+Mins+SS;
            return String.valueOf(totalss);
       /* } else if(string.length()==5){

            String[] split = string.split(":");
            String string3 = split[0];
            int min = Integer.parseInt(string3);
            int Mins =min*60;
            int  SS =Integer.parseInt(split[1]);

            totalss =Mins+SS;
            return String.valueOf(totalss);
        }*/
       // return String.valueOf(totalss);
    }



    //提供精确的加法运算
    public static double add(double v1, double v2) {
        BigDecimal b1 = BigDecimal.valueOf(v1);
        BigDecimal b2 = BigDecimal.valueOf(v2);
        return b1.add(b2).doubleValue();
    }
    //精确的减法运算
    public static double sub(double v1, double v2) {
        BigDecimal b1 = BigDecimal.valueOf(v1);
        BigDecimal b2 = BigDecimal.valueOf(v2);
        return b1.subtract(b2).doubleValue();
    }
    //精确的乘法运算
    public static double mul(double v1, double v2) {
        BigDecimal b1 = BigDecimal.valueOf(v1);
        BigDecimal b2 = BigDecimal.valueOf(v2);
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * 定精度，以后的数字四舍五入。
     * @param v1 被除数
     * @param v2 除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }






}
