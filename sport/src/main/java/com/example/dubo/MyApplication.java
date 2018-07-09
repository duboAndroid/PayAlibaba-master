package com.example.dubo;

import android.app.Application;

import com.example.dubo.weiXinPay.MainWeiXinPayActivity;

//当应用配置了多个进程的时候,application对象的onCreate方法就会执行多次
public class MyApplication extends Application {
    private MainWeiXinPayActivity mainActivity;
    public static String weiXinFlag;//微信相关标识码

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public MainWeiXinPayActivity getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(MainWeiXinPayActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public static String getWeiXinFlag() {
        return weiXinFlag;
    }

    public static void setWeiXinFlag(String weiXinFlag) {
        MyApplication.weiXinFlag = weiXinFlag;
    }
}
