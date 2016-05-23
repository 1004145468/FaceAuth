package com.xtu.faceauth;

import android.os.Bundle;
import android.widget.TextView;

import com.xtu.faceauth.base.BaseActivity;

public class ChEmail extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chemail);
        initViews();
    }

    private void initViews() {
        TextView titleView = (TextView) findViewById(R.id.id_activity_title);
        titleView.setText("更换昵称");
    }
}
