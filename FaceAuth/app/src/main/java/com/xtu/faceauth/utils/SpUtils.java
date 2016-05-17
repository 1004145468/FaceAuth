package com.xtu.faceauth.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.xtu.faceauth.app.TYApplication;
import com.xtu.faceauth.config.Constants;

/**
 * Created by Administrator on 2016/5/15.
 */
public class SpUtils {

    private static final String AUTOLOGIN = "autologin";
    private static final String FACELOGIN = "facelogin";
    private static SharedPreferences mSP = TYApplication.getContext().getSharedPreferences(Constants.SpName, Context.MODE_PRIVATE);

    //保存配置是否自定登录的参数
    public static void setAutoLogin(Boolean value) {
        SharedPreferences.Editor edit = mSP.edit();
        edit.putBoolean(AUTOLOGIN,value);
        edit.commit();
    }

    //获取当前是否为自动登录
    public static boolean getAutoLogin(){
        return mSP.getBoolean(AUTOLOGIN,false);
    }

    //保存配置是否进行人脸识别
    public static void setFaceLogin(Boolean value){
        SharedPreferences.Editor edit = mSP.edit();
        edit.putBoolean(FACELOGIN,value);
        edit.commit();
    }

    //获取当前是否为允许人脸识别
    public static boolean getFaceLogin(){
        return mSP.getBoolean(FACELOGIN,false);
    }

}
