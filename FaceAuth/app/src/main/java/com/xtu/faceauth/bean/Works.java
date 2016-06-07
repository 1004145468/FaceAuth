package com.xtu.faceauth.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/6/4.
 */
public class Works extends BmobObject{

    private TYUser mAuthor;  //作者

    private String Content;   //分享的内容说明

    private String worksPath; //分享作品的网络存储地址

    public Works(TYUser mAuthor, String content, String worksPath) {
        this.mAuthor = mAuthor;
        Content = content;
        this.worksPath = worksPath;
    }

    public TYUser getmAuthor() {
        return mAuthor;
    }

    public void setmAuthor(TYUser mAuthor) {
        this.mAuthor = mAuthor;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getWorksPath() {
        return worksPath;
    }

    public void setWorksPath(String worksPath) {
        this.worksPath = worksPath;
    }

}
