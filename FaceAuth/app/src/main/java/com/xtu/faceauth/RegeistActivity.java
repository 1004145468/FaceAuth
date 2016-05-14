package com.xtu.faceauth;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xtu.faceauth.base.BaseActivity;

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

    }
}
