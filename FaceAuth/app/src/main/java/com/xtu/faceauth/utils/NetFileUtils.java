package com.xtu.faceauth.utils;

import com.xtu.faceauth.app.TYApplication;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by Administrator on 2016/5/14.
 */
public class NetFileUtils {

    //当前文件对象
    private static BmobFile bmobFile;

    /**
     *  上传文件到Bmob服务器，通过 bmobFile.getFileUrl(context));获得文件的网络存储位置
     * @param file    需要上传的文件
     * @param mListener 状态监听
     */
    public static void uploadFile(File file,UploadFileListener mListener){
        if(!file.exists()||file.length()==0){
            ProgressbarUtils.hideDialog();
            ToastUtils.show("文件不存在或者已损坏！");
            return;
        }
        bmobFile = new BmobFile(file);
        bmobFile.uploadblock(TYApplication.getContext(),mListener);
    }

    public static String getNetFilePath(){
        return bmobFile.getFileUrl(TYApplication.getContext());
    }


}
