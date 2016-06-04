package com.xtu.faceauth.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/6/1.
 */
public class Advice extends BmobObject{
    private String advicerMsg; //建议内容
    private String advicerPhone;    //建议联系方式


    public Advice(String advicerMsg, String advicerPhone) {
        this.advicerMsg = advicerMsg;
        this.advicerPhone = advicerPhone;
    }
}
