package com.xtu.faceauth;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.xtu.faceauth.bean.Works;
import com.xtu.faceauth.config.Constants;
import com.xtu.faceauth.utils.BmobUtils;
import com.xtu.faceauth.utils.NetFileUtils;
import com.xtu.faceauth.utils.ProgressbarUtils;
import com.xtu.faceauth.utils.ToastUtils;

import java.io.File;

import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

public class ShareActivity extends AppCompatActivity {

    private EditText contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_communication);
        contentView = (EditText) findViewById(R.id.id_share2content);
    }


    //取消分享
    public void canelShare(View view){
        finish();
    }

    //确定分享
    public void okShare(View view){
        ProgressbarUtils.showDialog(this,"看，有灰机！");
        final String mContent = contentView.getText().toString();
        File file = new File(Constants.saveDir, "upload.jpg");
        NetFileUtils.uploadFile(file, new UploadFileListener() {
            @Override
            public void onFailure(int i, String s) {
                ProgressbarUtils.hideDialog();
                ToastUtils.show("网络错误，图片上传失败！");
            }

            @Override
            public void onSuccess() {
                String netFilePath = NetFileUtils.getNetFilePath();
                Works works = new Works((String) BmobUtils.getThingOfUser("username"),mContent,netFilePath);
                works.save(ShareActivity.this, new SaveListener() {
                    @Override
                    public void onFailure(int i, String s) {
                        ProgressbarUtils.hideDialog();
                        ToastUtils.show("网络不给力，信息发布失败！");
                    }

                    @Override
                    public void onSuccess() {
                        ProgressbarUtils.hideDialog();
                        ToastUtils.show("信息发布成功！");
                        finish();
                    }
                });
            }
        });
    }

}
