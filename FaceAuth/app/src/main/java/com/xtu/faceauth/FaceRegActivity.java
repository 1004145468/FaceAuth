package com.xtu.faceauth;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xtu.faceauth.base.BaseActivity;
import com.xtu.faceauth.utils.SpUtils;

public class FaceRegActivity extends BaseActivity implements View.OnClickListener {

    //开关切换图片
    private final int[] TOGGLE_IMAGES = {R.mipmap.face_reg_on, R.mipmap.face_reg_off};

    private ImageView mToggleImage;
    // 注册时的图片
    private ImageView mHeadView;

    //开关选择的状态
    private boolean isOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setWinFull();
        setContentView(R.layout.activity_face_reg);
        //初始化所有视图控件
        initViews();
    }

    private void initViews() {
        TextView titleView = (TextView) findViewById(R.id.id_activity_title);
        titleView.setText("人脸识别");

        //获取当前的开关状态
        isOpen = SpUtils.getFaceLogin();
        // 开关切换图片
        mToggleImage = (ImageView) findViewById(R.id.id_view);
        if(isOpen)
            mToggleImage.setImageResource(TOGGLE_IMAGES[0]);
        //开启人脸识别的开关
        View mToggleView = findViewById(R.id.id_toggle);
        mToggleView.setOnClickListener(this);

        // 选择图片注册图片
        mHeadView = (ImageView) findViewById(R.id.id_head);
    }

    //点击使用按钮 触发注册按钮
    public void onRegist(View view) {

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.id_toggle:
                //切换开关状态
                isOpen = !isOpen;
                SpUtils.setFaceLogin(isOpen);
                if(isOpen)
                    mToggleImage.setImageResource(TOGGLE_IMAGES[0]);
                else
                    mToggleImage.setImageResource(TOGGLE_IMAGES[1]);
                break;
            case R.id.id_head:
                //选择图片

                break;
            default:
        }
    }
}
