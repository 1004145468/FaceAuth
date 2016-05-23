package com.xtu.faceauth;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xtu.faceauth.base.BaseActivity;
import com.xtu.faceauth.utils.BmobUtils;
import com.xtu.faceauth.utils.ProgressbarUtils;
import com.xtu.faceauth.utils.ToastUtils;

import cn.bmob.v3.listener.UpdateListener;

public class ChpswActivity extends BaseActivity implements View.OnFocusChangeListener {

    private EditText mOldPswText;   //旧密码
    private EditText mNewPswText1;  //新密码
    private EditText mNewPswText2;  //确定密码

    private TextView mOldhintText;  //输入旧密码校验文字
    private TextView mNew1hintText; //输入新密码的校验文字
    private TextView mNew2hintText; //输入重复密码的校验文字

    private boolean mFlag1, mFlag2, mFlag3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chpsw);
        initViews();
    }

    private void initViews() {
        TextView titleView = (TextView) findViewById(R.id.id_activity_title);
        titleView.setText("修改密码");

        mOldPswText = (EditText) findViewById(R.id.id_oldpsw);
        mNewPswText1 = (EditText) findViewById(R.id.id_newpsw1);
        mNewPswText2 = (EditText) findViewById(R.id.id_newpsw2);

        mOldhintText = (TextView) findViewById(R.id.id_chpsw_oldhint);
        mNew1hintText = (TextView) findViewById(R.id.id_chpsw_new1hint);
        mNew2hintText = (TextView) findViewById(R.id.id_chpsw_new2hint);

        mOldPswText.setOnFocusChangeListener(this);
        mNewPswText1.setOnFocusChangeListener(this);
        mNewPswText2.setOnFocusChangeListener(this);
    }

    //确定更改密码
    public void onChange(View view) {

        if (mFlag1 && mFlag2 && mFlag3) {
            String oldText = mOldPswText.getText().toString().trim();
            String newText = mNewPswText2.getText().toString().trim();
            ProgressbarUtils.showDialog(this, "图言努力修改密码中，请稍后！");
            BmobUtils.updatePassword(oldText, newText, new UpdateListener() {
                @Override
                public void onSuccess() {
                    ProgressbarUtils.hideDialog();
                    ToastUtils.show("密码修改成功！");
                }

                @Override
                public void onFailure(int i, String s) {
                    ProgressbarUtils.hideDialog();
                    ToastUtils.show("密码修改失败，请重新尝试！");
                }
            });

        } else {
            ToastUtils.show("信息填写错误！");
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            return;
        }

        switch (v.getId()) {
            case R.id.id_oldpsw:
                //当输入旧密码失去焦点时：
                String mOldPsw = mOldPswText.getText().toString().trim();
                if (TextUtils.isEmpty(mOldPsw)) {
                    mOldhintText.setText("请输入当前密码！");
                    mFlag1 = false;
                } else {
                    mOldhintText.setText("");
                    mFlag1 = true;
                }
                break;
            case R.id.id_newpsw1:
                //当输入新密码失去焦点时：
                String mNewPsw1 = mNewPswText1.getText().toString().trim();
                if (TextUtils.isEmpty(mNewPsw1)) {
                    mNew1hintText.setText("新密码不予许为空！");
                    mFlag2 = false;
                } else {
                    mNew1hintText.setText("");
                    mFlag2 = true;
                }
                break;
            case R.id.id_newpsw2:
                //当重复密码失去焦点时：
                String mNewPsw = mNewPswText1.getText().toString().trim();
                String mNewPsw2 = mNewPswText2.getText().toString().trim();
                if (!mNewPsw.equals(mNewPsw2)) {
                    mNew2hintText.setText("两次密码输入不一致！");
                    mFlag3 = false;
                } else {
                    mNew2hintText.setText("");
                    mFlag3 = true;
                }
                break;
        }
    }
}
