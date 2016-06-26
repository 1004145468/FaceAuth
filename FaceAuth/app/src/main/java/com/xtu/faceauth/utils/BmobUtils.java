package com.xtu.faceauth.utils;

import com.xtu.faceauth.app.TYApplication;
import com.xtu.faceauth.bean.Advice;
import com.xtu.faceauth.bean.TYUser;
import com.xtu.faceauth.config.Constants;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.EmailVerifyListener;
import cn.bmob.v3.listener.ResetPasswordByEmailListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2016/5/14.
 */
public class BmobUtils {

    /**
     * 完成用户注册
     *
     * @param UserName 用户名
     * @param Password 密码
     * @param Email    注册邮箱
     * @param NickName 昵称
     * @param mListener  结果监听
     */
    public static void startRegister(String UserName, String Password, String Email,
                                        String NickName,SaveListener mListener) {
        TYUser mUser = new TYUser();
        mUser.setUsername(UserName);
        mUser.setPassword(Password);
        mUser.setEmail(Email);
        mUser.setmNickName(NickName);
        mUser.setmMsg("我为图言代言！O(∩_∩)O~");
        mUser.setFLOpen(false);
        mUser.setIconPath(Constants.default_Url);
        mUser.signUp(TYApplication.getContext(),mListener);
    }

    /**
     *  用户登录验证
     * @param UserName  用户名
     * @param Password  密码
     * @param mListener 结果监听
     */
    public static void startLogin(String UserName,String Password,SaveListener mListener){
        TYUser mUser = new TYUser();
        mUser.setUsername(UserName);
        mUser.setPassword(Password);
        mUser.login(TYApplication.getContext(),mListener);
    }

    /**
     *  更新用户信息
     * @param mUser 数据更改后的用户
     * @param mListener 结果监听
     */
    public static void upDateUser(TYUser mUser, UpdateListener mListener){
        mUser.update(TYApplication.getContext(), mUser.getObjectId(), mListener);
    }

    /**
     * 获取当前用户
     * @return
     */
    public static TYUser getCurrentUser(){
        return BmobUser.getCurrentUser(TYApplication.getContext(),TYUser.class);
    }

    /**
     *  获取到当前用户指定的字段值
     * @param key 字段名
     * @return
     */
    public static Object getThingOfUser(String key){
        return BmobUser.getObjectByKey(TYApplication.getContext(), key);
    }

    /**
     *  找回密码，通过邮箱重置密码
     * @param mEmail     邮箱
     * @param mListener  结果监听
     */
    public static void startFindPassword(String mEmail,ResetPasswordByEmailListener mListener){
        BmobUser.resetPasswordByEmail(TYApplication.getContext(), mEmail, mListener);
    }

    /**
     *  强制用户进行邮箱认证
     * @param mEmail    邮箱
     * @param mListener 结果监听
     */
    public static void requestEmailVerify(String mEmail,EmailVerifyListener mListener){
        BmobUser.requestEmailVerify(TYApplication.getContext(), mEmail, mListener);
    }


    /**
     *  用户更新密码
     * @param oldPsw    当前密码
     * @param newPsw    新密码
     * @param listener  结果监听
     */
    public static void updatePassword(String oldPsw,String newPsw, UpdateListener listener){
        BmobUser.updateCurrentUserPassword(TYApplication.getContext(), oldPsw,newPsw,listener);
    }

    public static void saveAdvice(String msg,String phone){
        Advice mAdvice = new Advice(msg,phone);
        mAdvice.save(TYApplication.getContext());
        ToastUtils.show("您的建议图言定铭记在心！");
    }
}
