package com.xtu.faceauth.config;

import android.os.Environment;

/**
 * Created by Administrator on 2016/5/14.
 */
public class Constants {

    public static final String KEY = "9fbbca998b5a516a5cf27b24b2b4335b";
    public static final String SCRETE = "mmqF6v927otZpxk7zo3HSi6zdOyyrzbG ";
    public static final String SpName = "config";
    public static final String BmobKey = "3333c2dd6ee023de5b2f6bf685d884c1";
    public static final String MSKey = "56cd583c";
    public static final String saveDir = Environment.getExternalStorageDirectory() + "/tuyan";
    public static final String loadup = Environment.getExternalStorageDirectory() + "/tuyan/upload.jpg";
    public static final String saveFile = Environment.getExternalStorageDirectory() + "/tuyan/user.jpg";
    public static final String cachePath = Environment.getExternalStorageDirectory() + "/tuyan/tmp.jpg";
    public static final String cropPath = Environment.getExternalStorageDirectory() + "/tuyan/tmp2.jpg";
    public static final String regcachePath = Environment.getExternalStorageDirectory() + "/tuyan/reg.jpg";
    public static final String regcropPath = Environment.getExternalStorageDirectory() + "/tuyan/regcrop.jpg";
    public static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    public static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    public static final int PHOTO_REQUEST_CUT = 3;// 结果
    public static final String default_Url = "http://bmob-cdn-2150.b0.upaiyun.com/2016/06/08/ff95654cec8c4a3db6b289c6e3dd2cd6.jpg";
}
