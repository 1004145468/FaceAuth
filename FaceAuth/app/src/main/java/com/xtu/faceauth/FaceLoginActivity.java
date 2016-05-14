package com.xtu.faceauth;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.xtu.faceauth.utils.ProgressbarUtils;
import com.xtu.faceauth.utils.ToastUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FaceLoginActivity extends AppCompatActivity implements SurfaceHolder.Callback, View.OnClickListener {

    private SurfaceView mSurfaceview;
    private SurfaceHolder mHolder;
    private View mRootview;
    private FloatingActionButton mFaBtn;
    private Camera mCamera;

    //确定拍照以后回调
    private Camera.PictureCallback mPictureCallBack = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            //设置不可被点击仅需拍照
            mFaBtn.setClickable(false);
            FileOutputStream fos = null;
            try {
                File faceFile = new File(getFilesDir(), System.currentTimeMillis() + ".jpeg");
                fos = new FileOutputStream(faceFile);
                fos.write(data);
            } catch (IOException e) {
                e.printStackTrace();
                ToastUtils.show("照片存储失败");
                mFaBtn.setClickable(true);
                startShow(mCamera, mHolder);
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            //调用离线人脸检测
            handleFace();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initViews();
    }

    private void initViews() {
        mSurfaceview = (SurfaceView) findViewById(R.id.id_surfaceview);
        mHolder = mSurfaceview.getHolder();
        mHolder.addCallback(this);

        mFaBtn = (FloatingActionButton) findViewById(R.id.id_fabbtn);
        mFaBtn.setOnClickListener(this);
        mRootview = findViewById(R.id.id_rootlayout);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mCamera == null) {
            mCamera = getCamera();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseCamera(mCamera);
    }

    //进行拍照，拍出来data传递的是一个缩略图，不要
    @Override
    public void onClick(View v) {
        if (mCamera != null) {
            mCamera.takePicture(null, null, mPictureCallBack);
        }
    }

    private Camera getCamera() {
        Camera tmpCamera;
        try {
            tmpCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
            return tmpCamera;   //拿到前置摄像头对象
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //打开预览
    private void startShow(Camera camera, SurfaceHolder holder) {
        try {
            camera.setPreviewDisplay(holder);
            camera.setDisplayOrientation(90);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void releaseCamera(Camera camera) {
        if (camera != null) {
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    private void handleFace() {
        ProgressbarUtils.showDialog(this, "图言正在努力的识别，请勿打断！");
        //  离线人脸检测并裁减到人脸
        //人脸比对

        mFaBtn.setClickable(true);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // 当被创建的时候进行画面的展示
        if (mCamera == null) {
            Toast.makeText(this, R.string.cameraisnull, Toast.LENGTH_SHORT).show();
            return;
        }
        startShow(mCamera, mHolder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (mCamera != null) {
            mCamera.stopPreview();
            startShow(mCamera, mHolder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }
}
