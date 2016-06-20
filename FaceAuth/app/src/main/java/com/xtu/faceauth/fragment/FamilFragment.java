package com.xtu.faceauth.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xtu.faceauth.PhotoActivity;
import com.xtu.faceauth.R;
import com.xtu.faceauth.config.Constants;
import com.xtu.faceauth.utils.FaceUtils;
import com.xtu.faceauth.utils.ImageUtils;
import com.xtu.faceauth.utils.ProgressbarUtils;
import com.xtu.faceauth.utils.ToastUtils;

import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 2016/5/15.
 */
public class FamilFragment extends Fragment implements View.OnClickListener {

    private final int[] IDS = {R.id.id_image1, R.id.id_image2, R.id.id_image3, R.id.id_image4, R.id.id_image5, R.id.id_image6};
    //打开图库请求码
    private static int REQUEST_IMAGESTORE = 1;
    //打开裁剪请求码
    private static int REQUEST_CROP = 2;

    private ImageView mePicView;
    private ImageView mStarView;
    private ImageView mToggleView;

    private TextView mNumView;
    private ImageView[] mImagesViews;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mContentView = inflater.inflate(R.layout.fragment_famil, container, false);

        //初始化所有控件
        mePicView = (ImageView) mContentView.findViewById(R.id.id_mepic);
        mePicView.setOnClickListener(this);
        mStarView = (ImageView) mContentView.findViewById(R.id.id_starpic);
        mToggleView = (ImageView) mContentView.findViewById(R.id.id_trans);
        mToggleView.setOnClickListener(this);

        //相似度
        mNumView = (TextView) mContentView.findViewById(R.id.id_familnum);

        //展示脸部的图片
        mImagesViews = new ImageView[IDS.length];
        for (int i = 0; i < IDS.length; i++) {
            mImagesViews[i] = (ImageView) mContentView.findViewById(IDS[i]);
            mImagesViews[i].setOnClickListener(this);
        }
        return mContentView;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //选择图片
            case R.id.id_mepic:
                picImage();
                break;
            //开始分析图片
            case R.id.id_trans:
                searchImage();
                break;
            //点击图片，单现图片
            default:
                if (mUrlList != null) {
                    int position = Integer.parseInt(v.getTag().toString());
                    Intent intent = new Intent(getActivity(), PhotoActivity.class);
                    intent.putExtra("url", mUrlList.get(position));
                    getActivity().startActivity(intent);
                }
                break;
        }
    }

    private List<String> mUrlList;

    //分析图片
    private void searchImage() {
        if (saveUri == null) {
            ToastUtils.show("请选择图片");
            return;
        }

        ProgressbarUtils.showDialog(getActivity(), "正在努力为您匹配相似脸，请耐心等候！");
        //开始分析
        FaceUtils.searchImage(getActivity(), new FaceUtils.SearchListener() {
            @Override
            public void onFail(Exception e) {
                ProgressbarUtils.hideDialog();
                ToastUtils.show("匹配失败！");
                Log.e("TAGTAG", "匹配失败！" + e);
            }

            @Override
            public void onSuccess(List<String> aList, double mValue) {
                ProgressbarUtils.hideDialog();
                mUrlList = aList;
                dealResult(aList, mValue);
            }
        });
    }

    private void dealResult(List<String> aList, double mValue) {
        String mResult = String.format("%.2f", mValue);
        mNumView.setText(mResult + "%");
        ImageUtils.Display(aList.get(0), mStarView);
        for (int i = 0; i < aList.size(); i++) {
            //加载图片到每一个控件
            ImageUtils.Display(aList.get(i), mImagesViews[i]);
        }
    }

    //启动相机设置图片
    private void picImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGESTORE);
    }

    private Uri saveUri;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGESTORE) {
                Uri currentUri = data.getData();
                mePicView.setImageURI(currentUri);
                //开始裁剪图片
                File saveDir = new File(Constants.saveDir);
                if(!(saveDir.exists() && saveDir.isDirectory()))
                    saveDir.mkdir();
                saveUri = Uri.fromFile(new File(Constants.cropPath));
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setData(currentUri);
                // crop为true是设置在开启的intent中设置显示的view可以剪裁
                intent.putExtra("crop", "true");
                // aspectX aspectY 是宽高的比例
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, saveUri);
                startActivityForResult(intent,REQUEST_CROP);
            }
            if(requestCode == REQUEST_CROP){
             if(saveUri != null){
                 searchImage();
             }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mUrlList != null) {
            mUrlList.clear();
            mUrlList = null;
        }

        File file = new File(Constants.cropPath);
        if(file.exists()){
            file.delete();
            saveUri = null;
        }
        //Log.e("TAGTAG", "清空-------------------------------------------》");
    }
}
