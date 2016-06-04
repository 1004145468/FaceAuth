package com.xtu.faceauth.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/6/4.
 */
public class Works extends BmobObject{

    private String Content;   //分享的内容说明

    private String worksPath ; //分享作品的网络存储地址

    public Works(String content, String worksPath) {
        Content = content;
        this.worksPath = worksPath;
    }
}
