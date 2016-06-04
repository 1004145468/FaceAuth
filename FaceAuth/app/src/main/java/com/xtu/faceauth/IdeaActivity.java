package com.xtu.faceauth;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xtu.faceauth.base.BaseActivity;
import com.xtu.faceauth.utils.BmobUtils;

public class IdeaActivity extends BaseActivity {


    private EditText mMsgText;  // 意见
    private EditText mPhoneText; //电话号码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idea);
        initViews();
    }

    private void initViews() {
        TextView titleView = (TextView) findViewById(R.id.id_activity_title);
        titleView.setText("意见反馈");
        mMsgText = (EditText) findViewById(R.id.id_msg);
        mPhoneText = (EditText) findViewById(R.id.id_phone);

    }

    //提交意见
    public void onSubmit(View view) {
        String mMsg = mMsgText.getText().toString().trim();
        String mPhone = mPhoneText.getText().toString().trim();
        if(!TextUtils.isEmpty(mMsg)){
            BmobUtils.saveAdvice(mMsg,mPhone);
        }
    }
}
