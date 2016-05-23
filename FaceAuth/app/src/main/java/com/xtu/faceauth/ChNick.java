package com.xtu.faceauth;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xtu.faceauth.base.BaseActivity;
import com.xtu.faceauth.bean.TYUser;
import com.xtu.faceauth.utils.BmobUtils;
import com.xtu.faceauth.utils.ToastUtils;

import cn.bmob.v3.listener.UpdateListener;

public class ChNick extends BaseActivity {

    private EditText mNickText;
    private String currentNickName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chnick);
        initViews();
    }

    private void initViews() {
        TextView titleView = (TextView) findViewById(R.id.id_activity_title);
        titleView.setText("更换昵称");
        currentNickName = (String) BmobUtils.getThingOfUser("mNickName");
        mNickText = (EditText) findViewById(R.id.id_chnick);
        mNickText.setText(currentNickName);
    }

    // 更新用户昵称
    public void onUpdate(View view) {
        String mNick = mNickText.getText().toString().trim();
        if (TextUtils.isEmpty(mNick)) {
            ToastUtils.show("昵称不能为空！");
            return;
        }

        if (mNick.equals(currentNickName)) {
            return;
        }
        TYUser currentUser = BmobUtils.getCurrentUser();
        currentUser.setmNickName(mNick);
        BmobUtils.upDateUser(currentUser, new UpdateListener() {
            @Override
            public void onSuccess() {
                ToastUtils.show("更新成功！");
            }

            @Override
            public void onFailure(int i, String s) {
                ToastUtils.show("昵称更新失败，请重新尝试！");
            }
        });
    }
}
