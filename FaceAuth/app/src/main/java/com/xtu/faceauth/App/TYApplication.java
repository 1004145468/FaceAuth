package com.xtu.faceauth.App;

import android.app.Application;
import android.content.Context;

/**
 * Created by Administrator on 2016/5/14.
 */
public class TYApplication extends Application {

    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }

}
