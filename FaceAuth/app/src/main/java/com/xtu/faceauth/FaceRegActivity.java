package com.xtu.faceauth;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.iflytek.cloud.FaceRequest;
import com.iflytek.cloud.RequestListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.xtu.faceauth.base.BaseActivity;
import com.xtu.faceauth.bean.TYUser;
import com.xtu.faceauth.config.Constants;
import com.xtu.faceauth.utils.BmobUtils;
import com.xtu.faceauth.utils.ImageUtils;
import com.xtu.faceauth.utils.NetFileUtils;
import com.xtu.faceauth.utils.ProgressbarUtils;
import com.xtu.faceauth.utils.SelectHeadTools;
import com.xtu.faceauth.utils.SpUtils;
import com.xtu.faceauth.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;

import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class FaceRegActivity extends BaseActivity implements View.OnClickListener {

    //开关切换图片
    private final int[] TOGGLE_IMAGES = {R.mipmap.face_reg_on, R.mipmap.face_reg_off};

    private ImageView mToggleImage;
    // 注册时的图片
    private ImageView mHeadView;
    //显示照片的文本
    private TextView mTextView;

    //开关选择的状态
    private boolean isOpen;
    private Button mRegBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_reg);
        //初始化所有视图控件
        initViews();
    }

    private void initViews() {
        TextView titleView = (TextView) findViewById(R.id.id_activity_title);
        titleView.setText("人脸识别");

        //获取当前的开关状态
        isOpen = SpUtils.getFaceLogin();

        View mToggleView = findViewById(R.id.id_toggle);
        mToggleImage = (ImageView) findViewById(R.id.id_view);
        mTextView = (TextView) findViewById(R.id.id_text);
        mHeadView = (ImageView) findViewById(R.id.id_head);
        mRegBtn = (Button) findViewById(R.id.id_reg);

        mToggleView.setOnClickListener(this);
        mHeadView.setOnClickListener(this);
        mRegBtn.setOnClickListener(this);

        //初始化显示
        turnShow(isOpen);

        //判断是否已经人脸注册了
        String facePath = (String) BmobUtils.getThingOfUser("facePath");
        if (!TextUtils.isEmpty(facePath)) {
            ImageUtils.Display(facePath, mHeadView);
            mRegBtn.setText("已经被注册！");
            mRegBtn.setEnabled(false);
            mHeadView.setEnabled(false);
        }
    }

    //点击使用按钮 触发注册按钮
    private void onRegist() {
        if (mDatas == null) {
            ToastUtils.show("请选择需要注册的人脸！");
            return;
        }
        ProgressbarUtils.showDialog(this, "图片正在努力的注册用户人脸模型...");
        NetFileUtils.uploadFile(new File(Constants.regcropPath), new UploadFileListener() {
            @Override
            public void onFailure(int i, String s) {
                ProgressbarUtils.hideDialog();
                ToastUtils.show("文件上传失败");
            }

            @Override
            public void onSuccess() {
                TYUser currentUser = BmobUtils.getCurrentUser();
                currentUser.setFacePath(NetFileUtils.getNetFilePath());
                BmobUtils.upDateUser(currentUser, new UpdateListener() {
                    @Override
                    public void onFailure(int i, String s) {
                        ProgressbarUtils.hideDialog();
                        ToastUtils.show("上传文件信息更新失败，请重新尝试！");
                    }

                    @Override
                    public void onSuccess() {
                        //开始人脸模型的注册
                        if (mDatas != null) {
                            FaceRequest mFaceRequest = new FaceRequest(FaceRegActivity.this);
                            mFaceRequest.setParameter(SpeechConstant.AUTH_ID, (String) BmobUtils.getThingOfUser("username"));
                            mFaceRequest.setParameter(SpeechConstant.WFR_SST, "reg");
                            mFaceRequest.sendRequest(mDatas, mRequestListener);
                        }
                    }
                });
            }
        });

    }

    //处理视图显示
    private void turnShow(boolean isOpen) {
        if (isOpen) {
            //打开
            mToggleImage.setImageResource(TOGGLE_IMAGES[0]);
            mTextView.setVisibility(View.VISIBLE);
            mHeadView.setVisibility(View.VISIBLE);
            mRegBtn.setVisibility(View.VISIBLE);
        } else {
            //关闭
            mToggleImage.setImageResource(TOGGLE_IMAGES[1]);
            mTextView.setVisibility(View.GONE);
            mHeadView.setVisibility(View.GONE);
            mRegBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_toggle:
                //切换开关状态
                isOpen = !isOpen;
                SpUtils.setFaceLogin(isOpen);
                turnShow(isOpen);
                break;
            case R.id.id_head:
                //选择图片
                File mDir = new File(Constants.saveDir);
                if (!(mDir.isDirectory() && mDir.exists())) {
                    mDir.mkdir();
                }
                File regFile = new File(Constants.regcachePath);
                photoUri = Uri.fromFile(regFile);
                File mCropfile = new File(Constants.regcropPath);
                cropUri = Uri.fromFile(mCropfile);
                SelectHeadTools.openDialog(this, photoUri);
                break;
            case R.id.id_reg:
                //点击注册按钮
                onRegist();
                break;
            default:
                break;
        }
    }

    private Uri photoUri;
    private Uri cropUri;

    private byte[] mDatas;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == Constants.PHOTO_REQUEST_TAKEPHOTO) {
            SelectHeadTools.startPhotoZoom(this, photoUri, cropUri, 600);
        }

        if (requestCode == Constants.PHOTO_REQUEST_GALLERY) {
            if (data == null)
                return;
            SelectHeadTools.startPhotoZoom(this, data.getData(), cropUri, 500);
        }

        if (requestCode == Constants.PHOTO_REQUEST_CUT) {
            mHeadView.setImageURI(cropUri);
            mDatas = ImageUtils.getData(Constants.regcropPath);
        }

    }

    private RequestListener mRequestListener = new RequestListener() {

        @Override
        public void onEvent(int i, Bundle bundle) {
        }

        @Override
        public void onBufferReceived(byte[] buffer) {
            ProgressbarUtils.hideDialog();
            try {
                String result = new String(buffer, "utf-8");
                JSONObject object = new JSONObject(result);
                startRegister(object);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO: handle exception
            }
        }

        @Override
        public void onCompleted(SpeechError speechError) {
            ProgressbarUtils.hideDialog();
            if (speechError != null) {
                ToastUtils.show("该账户已经被注册，请更换后再试");
            }
        }
    };

    private void startRegister(JSONObject obj) throws JSONException {
        int ret = obj.getInt("ret");
        if (ret == 0 && "success".equals(obj.get("rst"))) {
            ToastUtils.show("注册成功");
        } else {
            ToastUtils.show("注册失败，请重新注册！");
        }
    }

}
