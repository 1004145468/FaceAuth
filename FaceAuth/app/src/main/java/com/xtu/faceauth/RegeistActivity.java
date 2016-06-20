package com.xtu.faceauth;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xtu.faceauth.base.BaseActivity;
import com.xtu.faceauth.utils.BmobUtils;
import com.xtu.faceauth.utils.ProgressbarUtils;
import com.xtu.faceauth.utils.ToastUtils;

import cn.bmob.v3.listener.SaveListener;

public class RegeistActivity extends BaseActivity {

    //注册用户名 ，密码，昵称，邮箱
    private EditText mUserText,mPswText,mNickText,mEmailText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regeist);
        initViews();
    }

    private void initViews() {
        TextView titleView = (TextView) findViewById(R.id.id_activity_title);
        titleView.setText("用户注册");
        mUserText = (EditText) findViewById(R.id.id_username);
        mPswText = (EditText) findViewById(R.id.id_password);
        mNickText = (EditText) findViewById(R.id.id_nickname);
        mEmailText = (EditText) findViewById(R.id.id_email);
    }

    //点击注册按钮
    public void onRegist(View view){
        String mUserName = mUserText.getText().toString().trim();
        String mPassword = mPswText.getText().toString().trim();
        String mNickName = mNickText.getText().toString().trim();
        String mEmail = mEmailText.getText().toString().trim();
        if(TextUtils.isEmpty(mUserName)||TextUtils.isEmpty(mPassword)
                ||TextUtils.isEmpty(mNickName)||TextUtils.isEmpty(mEmail)){
            ToastUtils.show("请将信息填写完整！");
            return;
        }
        ProgressbarUtils.showDialog(this,"用户信息注册中...");
        BmobUtils.startRegister(mUserName, mPassword, mEmail, mNickName, "我为图言代言！O(∩_∩)O~",new SaveListener() {
            @Override
            public void onSuccess() {
                ProgressbarUtils.hideDialog();
                AlertDialog.Builder builder = new AlertDialog.Builder(RegeistActivity.this);
                builder.setTitle("温馨提示")
                        .setMessage("请到注册邮箱进行邮箱激活，方便以后找回密码！")
                        .setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .show();
            }

            @Override
            public void onFailure(int i, String s) {
                ProgressbarUtils.hideDialog();
                if(i==202){
                    ToastUtils.show("该用户名已存在！");
                }else if(i==203){
                    ToastUtils.show("该邮箱已被注册！");
                }else{
                    ToastUtils.show("注册失败，请重新尝试！");
                }
            }
        });
    }
}
