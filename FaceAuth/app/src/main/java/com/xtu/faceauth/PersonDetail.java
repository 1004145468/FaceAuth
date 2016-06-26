package com.xtu.faceauth;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xtu.faceauth.base.BaseActivity;
import com.xtu.faceauth.bean.TYUser;
import com.xtu.faceauth.config.Constants;
import com.xtu.faceauth.utils.BmobUtils;
import com.xtu.faceauth.utils.ImageUtils;
import com.xtu.faceauth.utils.NetFileUtils;
import com.xtu.faceauth.utils.ProgressbarUtils;
import com.xtu.faceauth.utils.SelectHeadTools;
import com.xtu.faceauth.utils.ToastUtils;

import org.w3c.dom.Text;

import java.io.File;

import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class PersonDetail extends BaseActivity implements View.OnClickListener {

    private TextView mNickText;         // 昵称字段
    private TextView mUserNameText;     // 用户名
    private TextView mEmailText;       //邮箱
    private TextView mMsgText;        //个人签名

    private ImageView mHeadImage;       // 头像

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_detail);
        initViews();
    }

    private void initViews() {

        TextView titleView = (TextView) findViewById(R.id.id_activity_title);
        titleView.setText("个人中心");
        //显示控件细节
        mHeadImage = (ImageView) findViewById(R.id.id_persondetail_head);
        mNickText = (TextView) findViewById(R.id.id_persondetail_nick);
        mUserNameText = (TextView) findViewById(R.id.id_persondetail_username);
        mEmailText = (TextView) findViewById(R.id.id_persondetail_email);
        mMsgText = (TextView) findViewById(R.id.id_persondetail_msg);


        String iconPath = (String) BmobUtils.getThingOfUser("IconPath");
        if(!TextUtils.isEmpty(iconPath)){
            ImageUtils.Display(iconPath, mHeadImage);
        }
        View firstView = findViewById(R.id.id_persondetail_first);
        View secondView = findViewById(R.id.id_persondetail_second);
        View fiveView = findViewById(R.id.id_persondetail_five);

        firstView.setOnClickListener(this);
        secondView.setOnClickListener(this);
        fiveView.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mNickText.setText((String) BmobUtils.getThingOfUser("mNickName"));
        mUserNameText.setText((String) BmobUtils.getThingOfUser("username"));
        mEmailText.setText((String) BmobUtils.getThingOfUser("email"));
        mMsgText.setText((String) BmobUtils.getThingOfUser("mMsg"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_persondetail_first:
                //更改用户头像，头像
                SelectHeadTools.startImageCaptrue(this);
                break;
            case R.id.id_persondetail_second:
                // 更改用户昵称
                Intent intent = new Intent(this,ChNick.class);
                startActivity(intent);
                break;
            case R.id.id_persondetail_five:
                //更改用户签名
                Intent intent2 = new Intent(this,ChMsg.class);
                startActivity(intent2);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == Constants.PHOTO_REQUEST_GALLERY){
                Uri mImageUri = data.getData();
                Uri mUri = Uri.fromFile(new File(Constants.cachePath));
                SelectHeadTools.startPhotoZoom(this,mImageUri,mUri,500);
            }
            if(requestCode == Constants.PHOTO_REQUEST_CUT){
                //开始上传图片
                File file = new File(Constants.cachePath);
                ProgressbarUtils.showDialog(this,"开始更新用户头像...");
                NetFileUtils.uploadFile(file, new UploadFileListener() {
                    @Override
                    public void onSuccess() {
                        String netFilePath = NetFileUtils.getNetFilePath();
                        TYUser currentUser = BmobUtils.getCurrentUser();
                        currentUser.setIconPath(netFilePath);
                        BmobUtils.upDateUser(currentUser, new UpdateListener() {
                            @Override
                            public void onSuccess() {
                                ProgressbarUtils.hideDialog();
                                Uri mUri = Uri.fromFile(new File(Constants.cachePath));
                                mHeadImage.setImageURI(mUri);
                            }

                            @Override
                            public void onFailure(int i, String s) {
                                ProgressbarUtils.hideDialog();
                                ToastUtils.show("用户头像信息更新失败，请重新尝试！");                            }
                        });
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        ProgressbarUtils.hideDialog();
                        ToastUtils.show("头像更新失败！");
                    }
                });
            }
        }
    }
}
