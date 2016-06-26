package com.xtu.faceauth.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2016/5/14.
 */
public class TYUser extends BmobUser implements Serializable {

    private String mNickName;     //昵称
    private String mMsg;    //用户个性签名
    private String IconPath; //用户头像网络地址
    private String facePath;  //用户人脸识别模型图片地址
    private Boolean FLOpen;   //是否允许打开人脸登录

    public String getmNickName() {
        return mNickName;
    }

    public void setmNickName(String mNickName) {
        this.mNickName = mNickName;
    }

    public String getmMsg() {
        return mMsg;
    }

    public void setmMsg(String mMsg) {
        this.mMsg = mMsg;
    }

    public String getIconPath() {
        return IconPath;
    }

    public void setIconPath(String iconPath) {
        IconPath = iconPath;
    }

    public String getFacePath() {
        return facePath;
    }

    public void setFacePath(String facePath) {
        this.facePath = facePath;
    }

    public Boolean getFLOpen() {
        return FLOpen;
    }

    public void setFLOpen(Boolean FLOpen) {
        this.FLOpen = FLOpen;
    }

    @Override
    public boolean equals(Object o) {
        String oUserName = ((TYUser)o).getUsername();
        return this.getUsername().equals(oUserName);
    }
}
