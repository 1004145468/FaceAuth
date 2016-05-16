package com.xtu.faceauth;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.FaceRequest;
import com.iflytek.cloud.RequestListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;
import com.xtu.faceauth.config.Constants;
import com.xtu.faceauth.utils.FaceUtils;
import com.xtu.faceauth.utils.ProgressbarUtils;
import com.xtu.faceauth.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class FaceLoginActivity extends AppCompatActivity implements SurfaceHolder.Callback, View.OnClickListener {

    private SurfaceView mSurfaceview;
    private SurfaceHolder mHolder;
    private EditText mUserNameText;
    private FloatingActionButton mFaBtn;
    private Camera mCamera;

    //确定拍照以后回调
    private Camera.PictureCallback mPictureCallBack = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            ToastUtils.show("照片存储");
            //设置不可被点击仅需拍照
            mFaBtn.setClickable(false);
            FileOutputStream fos = null;
            try {
                File mDir = new File(Constants.saveDir);
                if(!mDir.exists())
                    mDir.mkdir();
                File faceFile = new File(Constants.saveFile);
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
    private String mUserName;

    //开始检测
    private void handleFace() {
        ProgressbarUtils.showDialog(this, "图言正在努力的识别，请勿打断！");
        //  离线人脸检测并裁减到人脸
        //人脸比对
        Bitmap mBitmap = BitmapFactory.decodeFile(Constants.saveFile);
        Bitmap sBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight() / 2);
        mBitmap.recycle();
        FaceUtils.saveBitmapToFile(sBitmap);
        // 获取图片的宽和高
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(Constants.cachePath, options);
        // 压缩图片
        options.inSampleSize = Math.max(1, (int) Math.ceil(Math.max(
                (double) options.outWidth / 1024f,
                (double) options.outHeight / 1024f)));
        options.inJustDecodeBounds = false;
        Bitmap mImage = BitmapFactory.decodeFile(Constants.cachePath, options);

        // 若mImageBitmap为空则图片信息不能正常获取
        if (null == mImage) {
            ToastUtils.show("图片信息无法正常获取！");
            return;
        }

        //z
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        //可根据流量及网络状况对图片进行压缩
        mImage.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        byte[] mImageData = baos.toByteArray();
        FaceRequest mFaceRequest = new FaceRequest(this);
        mFaceRequest.setParameter(SpeechConstant.AUTH_ID, mUserName);
        mFaceRequest.setParameter(SpeechConstant.WFR_SST, "verify");
        mFaceRequest.sendRequest(mImageData, mRequestListener);
        mFaBtn.setClickable(true);
    }

    private RequestListener mRequestListener = new RequestListener() {

        @Override
        public void onEvent(int eventType, Bundle params) {
        }

        @Override
        public void onBufferReceived(byte[] buffer) {
            ProgressbarUtils.hideDialog();
            try {
                String result = new String(buffer, "utf-8");
                ToastUtils.show("result-->"+result);
                JSONObject object = new JSONObject(result);
                startVerify(object);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                startShow(mCamera, mHolder);
            } catch (JSONException e) {
            }
        }

        @Override
        public void onCompleted(SpeechError error) {
            ProgressbarUtils.hideDialog();
            if (error != null) {
                startShow(mCamera, mHolder);
                switch (error.getErrorCode()) {
                    case ErrorCode.MSP_ERROR_ALREADY_EXIST:
                        ToastUtils.show("authid已经被注册，请更换后再试");
                        break;

                    default:
                        ToastUtils.show(error.getPlainDescription(true));
                        break;
                }
            }
        }
    };


    private void startVerify(JSONObject obj) throws JSONException {
        int ret = obj.getInt("ret");
        if (ret != 0) {
            ToastUtils.show("验证失败");
            startShow(mCamera, mHolder);
            return;
        }
        if ("success".equals(obj.get("rst")) && obj.getBoolean("verf")) {
            ToastUtils.show("通过验证，欢迎回来！");
        } else {
            ToastUtils.show("验证失败");
            startShow(mCamera, mHolder);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_login);
        SpeechUtility.createUtility(this, "appid=" + Constants.MSKey);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initViews();
    }

    private void initViews() {
        mSurfaceview = (SurfaceView) findViewById(R.id.id_surfaceview);
        mHolder = mSurfaceview.getHolder();
        mHolder.addCallback(this);

        mUserNameText = (EditText) findViewById(R.id.id_username);

        mUserNameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("hehe",s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("hehe",s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("hehe",s.toString());
            }
        });

        mFaBtn = (FloatingActionButton) findViewById(R.id.id_fabbtn);
        mFaBtn.setOnClickListener(this);
    }

    //进行拍照，拍出来data传递的是一个缩略图，不要
    @Override
    public void onClick(View v) {
        Log.d("hehe","hehehe");
        mUserName = mUserNameText.getText().toString().trim();
        if (TextUtils.isEmpty(mUserName)) {
            ToastUtils.show("请填写用户名！");
            return;
        }
        if (mCamera != null) {
            mCamera.takePicture(null, null, mPictureCallBack);
        }
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

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // 当被创建的时候进行画面的展示
        if (mCamera == null) {
            Toast.makeText(this, R.string.cameraisnull, Toast.LENGTH_SHORT).show();
            return;
        }
        startShow(mCamera, holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (mCamera != null) {
            mCamera.stopPreview();
            startShow(mCamera, holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }
}
