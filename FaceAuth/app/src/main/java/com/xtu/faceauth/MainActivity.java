package com.xtu.faceauth;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.xtu.faceauth.utils.BmobUtils;
import com.xtu.faceauth.utils.ProgressbarUtils;
import com.xtu.faceauth.utils.SpUtils;
import com.xtu.faceauth.utils.ToastUtils;

import cn.bmob.v3.listener.ResetPasswordByEmailListener;
import cn.bmob.v3.listener.SaveListener;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    private EditText mUserText,mPsdText;
    private CheckBox mRemenberBox;
    private View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // Log.e("hehe","hehehe");
        //判断用户是否允许自动登录
        if(SpUtils.getAutoLogin()&&BmobUtils.getCurrentUser()!=null){
            //直接跳转登录功能界面
            enterFunctionActivty();
        }
        initToolBar();
        initViews();
    }

    //初始化所有控件
    private void initViews() {
        rootView = findViewById(R.id.id_root);
        mUserText = (EditText) findViewById(R.id.id_user);
        mPsdText = (EditText) findViewById(R.id.id_password);
        mRemenberBox = (CheckBox) findViewById(R.id.id_remenberInfo);
        rootView.setOnTouchListener(this);
    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.id_toolbar);
        setSupportActionBar(toolbar);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.id_toorbarinclude);
        appBarLayout.setExpanded(false);
    }

    //注册用户
    public void onRegist(View view){
        Intent intent = new Intent(this,RegeistActivity.class);
        startActivity(intent);
    }

    //用户登录
    public void onLogin(View view){
        String mUserName = mUserText.getText().toString().trim();
        String mPassword = mPsdText.getText().toString().trim();
        if(TextUtils.isEmpty(mUserName)||TextUtils.isEmpty(mPassword)){
            ToastUtils.show("请将登录信息填写完整！");
            return;
        }

        ProgressbarUtils.showDialog(this,"努力加载用户信息...");
        //用户登录信息确定填写完整，进行用户登录
        BmobUtils.startLogin(mUserName, mPassword, new SaveListener() {
            @Override
            public void onSuccess() {
                ProgressbarUtils.hideDialog();
                //登录成功
                enterFunctionActivty();
            }

            @Override
            public void onFailure(int i, String s) {
                ProgressbarUtils.hideDialog();
                //登录失败
                ToastUtils.show("用户名或密码错误！");
            }
        });
    }

    private void enterFunctionActivty(){
        Intent intent = new Intent(MainActivity.this,FunctionActivity.class);
        startActivity(intent);
        finish();
    }

    private AlertDialog mDialog;
    private TextView mForgetText;
    private Button mForgetBtn;
    //当找回密码
    public void onFindPsw(View view){
        //弹出重置密码的对话框
        if(mDialog==null){
            mDialog = new AlertDialog.Builder(this).create();
            View dialogView = View.inflate(this, R.layout.dialog_findpsw, null);
            mDialog.setView(dialogView,0,0,0,0);
            mDialog.setCancelable(true);
            mForgetText = (TextView) dialogView.findViewById(R.id.id_email);
            mForgetBtn = (Button) dialogView.findViewById(R.id.id_forgetbtn);
            mForgetBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //得到用户注册时的邮箱地址
                    String mEmail = mForgetText.getText().toString().trim();
                    if(TextUtils.isEmpty(mEmail)){
                        ToastUtils.show("邮箱不能为空，请重新输入！");
                        return;
                    }
                    ProgressbarUtils.showDialog(MainActivity.this,"后台重置用户登录密码，请稍后！");
                    //检测邮箱不为空之后，通过指定的邮箱进行密码重置
                    BmobUtils.startFindPassword(mEmail, new ResetPasswordByEmailListener() {
                        @Override
                        public void onSuccess() {
                            ProgressbarUtils.hideDialog();
                            ToastUtils.show("重置密码的邮件发送成功！");
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            ProgressbarUtils.hideDialog();
                            ToastUtils.show("网络不给力或者邮箱未激活！");
                        }
                    });
                }
            });
        }
        mDialog.show();
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
