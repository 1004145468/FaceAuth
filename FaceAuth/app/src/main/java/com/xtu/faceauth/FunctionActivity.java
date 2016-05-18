package com.xtu.faceauth;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.xtu.faceauth.factory.FragmentFactory;

public class FunctionActivity extends AppCompatActivity implements View.OnClickListener {

    // 功能界面图标 被选择时 显示
    private final int[] images_selected = new int[]{
            R.mipmap.icon_funcpic, R.mipmap.icon_famil, R.mipmap.icon_friends, R.mipmap.icon_owner};

    // 功能界面图标 没有被选择时 显示
    private final int[] images_normal = new int[]{
            R.mipmap.icon_funpic_no, R.mipmap.icon_famil_no, R.mipmap.icon_friends_no, R.mipmap.icon_owner_no};

    //四个icon显示组件
    private TextView[] functionBtns = new TextView[4];
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_function);
        initViews();
    }

    //初始化所有控件
    private void initViews() {
        functionBtns[0] = (TextView) findViewById(R.id.function_funcpic);
        functionBtns[1] = (TextView) findViewById(R.id.function_famil);
        functionBtns[2] = (TextView) findViewById(R.id.function_friends);
        functionBtns[3] = (TextView) findViewById(R.id.function_owner);
        // 为五个按钮设置点击事件
        for (int i = 0; i < 4; i++) {
            functionBtns[i].setOnClickListener(this);
        }

        //将第一个碎片放在窗口
        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.beginTransaction().add(R.id.id_function_content, FragmentFactory.getFragment(0)).commit();
    }

    private void initBtns(TextView textview) {
        for (int i = 0; i < 4; i++) {
            Drawable drawable = getResources().getDrawable(images_normal[i]);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                    drawable.getMinimumHeight());
            functionBtns[i].setCompoundDrawables(null, drawable, null, null);
        }
        int position = Integer.parseInt(textview.getTag().toString());
        Drawable drawable = getResources().getDrawable(
                images_selected[position]);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                drawable.getMinimumHeight());
        textview.setCompoundDrawables(null, drawable, null, null);
    }

    //当按钮被选择时调用,替换碎片
    @Override
    public void onClick(View view) {
        //更改图标的选择状态
        TextView textview = (TextView) view;
        initBtns(textview);
        //开始事务
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        //处理点击事件
        switch (view.getId()) {
            //趣图碎片
            case R.id.function_funcpic:
                transaction.replace(R.id.id_function_content, FragmentFactory.getFragment(0));
                break;
            //相似脸碎片
            case R.id.function_famil:
                transaction.replace(R.id.id_function_content, FragmentFactory.getFragment(1));
                break;
            //社区
            case R.id.function_friends:
                transaction.replace(R.id.id_function_content, FragmentFactory.getFragment(2));
                break;
            //个人中心
            case R.id.function_owner:
                transaction.replace(R.id.id_function_content, FragmentFactory.getFragment(3));
                break;
        }
        //提交事务
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("确定要退出此应用？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FunctionActivity.this.finish();
                    }
                })
                .setNegativeButton("算了",null)
                .show();
    }
}
