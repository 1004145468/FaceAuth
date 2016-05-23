package com.xtu.faceauth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xtu.faceauth.base.BaseActivity;

public class SafeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe);
        initViews();
    }

    private void initViews() {
        TextView titleView = (TextView) findViewById(R.id.id_activity_title);
        titleView.setText("账号安全");
    }

    //人脸认证
    public void onFaceAuth(View view){
        Intent intent = new Intent(this,FaceRegActivity.class);
        startActivity(intent);
    }

    // 邮箱激活
    public void onActiveEmail(View view){

    }

    //更改密码
    public void onChangePsw(View view){
        Intent intent = new Intent(this,ChpswActivity.class);
        startActivity(intent);
    }
}
