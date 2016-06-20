package com.xtu.faceauth;

import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.xtu.faceauth.utils.ImageUtils;
import com.xtu.faceauth.utils.ToastUtils;

import java.io.File;

import uk.co.senab.photoview.PhotoView;

public class PhotoActivity extends AppCompatActivity implements View.OnLongClickListener {

    private String mUrl;
    private TextView progressView;
    private View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        mUrl = getIntent().getStringExtra("url");
        initViews();
    }

    private void initViews() {
       rootView =  findViewById(R.id.id_root);
        
        PhotoView mImageView = (PhotoView) findViewById(R.id.id_photo);
        ImageUtils.Display(mUrl, mImageView);
        mImageView.setOnLongClickListener(this);
        
        progressView = (TextView) findViewById(R.id.id_progress);
    }

    @Override
    public boolean onLongClick(View v) {
        Snackbar.make(rootView,"是否下载该图片？",Snackbar.LENGTH_SHORT)
                .setAction("下载", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String fileName = SystemClock.elapsedRealtime() + ".jpg";
                        String path = new File(Environment.getExternalStorageDirectory(), fileName).getAbsolutePath();
                        download(path);
                    }
                })
                .show();
        return true;
    }

    private void download(final String path){
        HttpUtils http = new HttpUtils();
        http.download(mUrl,
                path,
                true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
                true, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
                new RequestCallBack<File>() {

                    @Override
                    public void onStart() {
                        progressView.setVisibility(View.VISIBLE);
                        progressView.setText("图片准备下载...");
                    }

                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
                        progressView.setText("下载进度："+Math.floor(current/total)+"%");
                    }

                    @Override
                    public void onSuccess(ResponseInfo<File> responseInfo) {
                        progressView.setVisibility(View.INVISIBLE);
                        ToastUtils.show("图片保存在"+path);
                    }


                    @Override
                    public void onFailure(HttpException error, String msg) {
                        progressView.setVisibility(View.INVISIBLE);
                        ToastUtils.show("图片下载失败，请重新下载！");
                    }
                });
    }
}
