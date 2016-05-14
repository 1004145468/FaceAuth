package com.xtu.faceauth;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    private Toolbar toolbar;
    private EditText mUserText,mPsdText;
    private CheckBox mRemenberBox;
    private TextView mRegUser;
    private View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolBar();
        initViews();
    }

    //初始化所有控件
    private void initViews() {
        rootView = findViewById(R.id.id_root);
        mUserText = (EditText) findViewById(R.id.id_user);
        mPsdText = (EditText) findViewById(R.id.id_password);
        mRemenberBox = (CheckBox) findViewById(R.id.id_remenberInfo);
        mRegUser = (TextView) findViewById(R.id.id_reguser);
        rootView.setOnTouchListener(this);
    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.id_toolbar);
        setSupportActionBar(toolbar);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.id_toorbarinclude);
        appBarLayout.setExpanded(false);
    }

    //用户登录
    public void onLogin(View view){

    }


    private int startX;
    //处理acticvity左划事件
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                startX = (int) event.getX();
                break;
            case MotionEvent.ACTION_UP:
                int tmpX = (int) event.getX();
                if(startX-tmpX>100){
                    //达到侧滑阈值
                    Intent intent= new Intent(this,FaceLoginActivity.class);
                    startActivity(intent);
                }
                break;
        }
        return true;
    }
}
