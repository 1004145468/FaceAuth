package com.xtu.faceauth.app;

import android.app.Application;
import android.content.Context;

import cn.bmob.v3.Bmob;

/**
 * Created by Administrator on 2016/5/14.
 */
public class TYApplication extends Application {

    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this, "76e1c6931e3c4348090ff0e12e3714ff");
        context = getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }

}
