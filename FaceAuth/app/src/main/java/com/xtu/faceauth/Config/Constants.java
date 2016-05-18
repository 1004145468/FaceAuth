package com.xtu.faceauth.config;

import android.os.Environment;

/**
 * Created by Administrator on 2016/5/14.
 */
public class Constants {

    public static final String SpName = "config";
    public static final String BmobKey = "76e1c6931e3c4348090ff0e12e3714ff";
    public static final String MSKey = "56cd583c";
    public static final String saveDir = Environment.getExternalStorageDirectory()+"/tuyan";
    public static final String saveFile = Environment.getExternalStorageDirectory()+"/tuyan/user.jpg";
    public static final String cachePath = Environment.getExternalStorageDirectory()+"/tuyan/tmp.jpg";
    public static final String regcachePath = Environment.getExternalStorageDirectory()+"/tuyan/reg.jpg";
    public static final String regcropPath = Environment.getExternalStorageDirectory()+"/tuyan/regcrop.jpg";
    public static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    public static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    public static final int PHOTO_REQUEST_CUT = 3;// 结果
}
