package com.xtu.faceauth.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xtu.faceauth.R;
import com.xtu.faceauth.ShareActivity;
import com.xtu.faceauth.adapter.SkinAdapter;
import com.xtu.faceauth.config.Constants;
import com.xtu.faceauth.utils.FaceUtils;
import com.xtu.faceauth.utils.ProgressbarUtils;
import com.xtu.faceauth.utils.SelectHeadTools;
import com.xtu.faceauth.utils.ToastUtils;

import java.io.File;
import java.io.FileOutputStream;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import uk.co.senab.photoview.PhotoView;

/**
 * Created by Administrator on 2016/5/15.
 */
public class FuncpicFragment extends Fragment implements View.OnClickListener {

    private PhotoView photoImage;
    private ImageView selectPhoto;
    private View titleBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mParent = inflater.inflate(R.layout.fragment_funcpic, container, false);
        titleBar = mParent.findViewById(R.id.id_titlebar);
        photoImage = (PhotoView) mParent.findViewById(R.id.id_funcpic_imageview);
        photoImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (functionMap != null) {
                    showSaveDialog();
                }
                return true;
            }
        });
        selectPhoto = (ImageView) mParent.findViewById(R.id.id_photo);
        ImageView sharePhoto = (ImageView) mParent.findViewById(R.id.id_share);
        selectPhoto.setOnClickListener(this);
        sharePhoto.setOnClickListener(this);
        return mParent;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (functionMap != null) {
            photoImage.setImageBitmap(functionMap);
        }
        ShareSDK.initSDK(getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (functionMap != null) {
            functionMap.recycle();
            functionMap = null;
        }

        ShareSDK.stopSDK(getActivity());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_photo:
                //选择照片
                showPhotoContainer();
                break;
            case R.id.id_share:
                //分享照片
                showShare();
                break;
            case R.id.dialog_camera:
                //通过相机获取图片
                mDialog.dismiss();
                SelectHeadTools.startCamearPicCut(getActivity(), Uri.fromFile(new File(Constants.cachePath)));
                break;
            case R.id.dialog_photostore:
                //通过图库获取图片
                mDialog.dismiss();
                SelectHeadTools.startImageCaptrue(getActivity());
                break;
            case R.id.dialog_skin:
                //换装饰
                mDialog.dismiss();
                showSkins();
                break;
        }
    }

    private void showShare() {
        if (functionMap == null) {
            ToastUtils.show("未发现需要分享的内容！");
            return;
        }

        try {
            File file = new File(Constants.loadup);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            functionMap.compress(Bitmap.CompressFormat.JPEG, 80, fileOutputStream);
            OnekeyShare oks = new OnekeyShare();

            //为弹窗添加自定义的图标
            Bitmap customIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            oks.setCustomerLogo(customIcon, "社区", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //分享至社区
                    share2Community();
                }
            });

            //关闭sso授权
            oks.disableSSOWhenAuthorize();
            //设置标题
            oks.setTitle(getString(R.string.app_name));
            // text是分享文本，所有平台都需要这个字段
            oks.setText("我通过图言，diy一张趣图！");
            //本地图片的URl
            oks.setImagePath(Constants.loadup);
            // 启动分享GUI
            oks.show(getActivity());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //分享至社区
    private void share2Community() {
        //打开内容填写对话框
        Intent intent = new Intent(getActivity(), ShareActivity.class);
        startActivity(intent);
    }


    //左侧浮动的弹窗
    private PopupWindow mSkinWindow;

    private void showSkins() {
        if (mSkinWindow == null) {
            View contentView = View.inflate(getActivity(), R.layout.popwindow_skins, null);
            ListView listview = (ListView) contentView.findViewById(R.id.id_listview);
            SkinAdapter adapter = new SkinAdapter(getActivity());
            listview.setAdapter(adapter);
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //切换装饰
                    FaceUtils.drawImage(position);
                }
            });
            mSkinWindow = new PopupWindow(contentView, 150, 640);
            mSkinWindow.setFocusable(true);
            mSkinWindow.setBackgroundDrawable(new BitmapDrawable());
            mSkinWindow.setAnimationStyle(R.style.SkinDialogAnimation);
        }
        mSkinWindow.showAsDropDown(titleBar, 0, 120);
    }

    public void setImage(Uri uri) {
        photoImage.setImageURI(uri);
        //开始趣图的生成
        dealPicture(uri);
    }

    public static Bitmap functionMap = null;

    private void dealPicture(final Uri uri) {
        ProgressbarUtils.showDialog(getActivity(), "图言努力p图呢！主人");
        FaceUtils.detectFace(getActivity(), uri, new FaceUtils.CallBack() {
            @Override
            public void onSuccess(Bitmap bitmap) {
                functionMap = bitmap;
                photoImage.setImageBitmap(bitmap);
            }

            @Override
            public void onFail(Exception e) {
                ToastUtils.show(e.toString());
                ToastUtils.show("图片格式不正确，请更换图片");
            }
        });
    }

    private AlertDialog mDialog;

    //弹出图片选择对话框
    private void showPhotoContainer() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        View mView = View.inflate(getActivity(), R.layout.dialog_photoselect, null);
        TextView cameraView = (TextView) mView.findViewById(R.id.dialog_camera);
        TextView photoView = (TextView) mView.findViewById(R.id.dialog_photostore);
        TextView skinView = (TextView) mView.findViewById(R.id.dialog_skin);
        cameraView.setOnClickListener(this);
        photoView.setOnClickListener(this);
        skinView.setOnClickListener(this);
        mDialog = mBuilder.setCancelable(true).setView(mView, 0, 0, 0, 0).create();
        mDialog.getWindow().setWindowAnimations(R.style.FunctionDialogAnimation);
        mDialog.show();
    }

    private void showSaveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("是否保存该图片").setCancelable(true).setNegativeButton("否", null)
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            String path = SystemClock.elapsedRealtime() + ".jpg";
                            File file = new File(Environment.getExternalStorageDirectory(), path);
                            FileOutputStream fileOutputStream = new FileOutputStream(file);
                            functionMap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                            ToastUtils.show("图片保存在/Sdcard/" + path);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).show();
    }


}
