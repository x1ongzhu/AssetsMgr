package me.zeo_x.management;

import android.app.Application;


/**
 * Created by xiong on 2016/1/1.
 */
public class MyApplication extends Application {
    public static String domain = "http://172.20.44.99/";   //地址
//    public static String domain = "http://61.155.8.207/";   //地址
    public MyApplication() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
