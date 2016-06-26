package com.xtu.faceauth;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xtu.faceauth.base.BaseActivity;
import com.xtu.faceauth.bean.TYUser;
import com.xtu.faceauth.utils.BmobUtils;
import com.xtu.faceauth.utils.ToastUtils;

import cn.bmob.v3.listener.UpdateListener;

public class ChMsg extends BaseActivity {

    private EditText mMsgEdit;
    private String initMsg;     //原始的个性签名

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chmsg);
        initViews();
    }

    private void initViews() {
        TextView titleView = (TextView) findViewById(R.id.id_activity_title);
        titleView.setText("修改签名");
        mMsgEdit = (EditText) findViewById(R.id.id_chmsg);
        initMsg = BmobUtils.getThingOfUser("mMsg").toString();
        mMsgEdit.setText(initMsg);
    }

    public void onUpdate(View view){
        String currentMsg = mMsgEdit.getText().toString();
        if(currentMsg.equals(initMsg)){
            return;
        }
        TYUser currentUser = BmobUtils.getCurrentUser();
        currentUser.setmMsg(currentMsg);
        BmobUtils.upDateUser(currentUser, new UpdateListener() {
            @Override
            public void onSuccess() {
                ToastUtils.show("更新成功！");
            }

            @Override
            public void onFailure(int i, String s) {
                ToastUtils.show("个性签名更新失败，请重新尝试！");
            }
        });
    }
}
