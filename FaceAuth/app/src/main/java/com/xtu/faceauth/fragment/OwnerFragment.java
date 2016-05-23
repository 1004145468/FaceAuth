package com.xtu.faceauth.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xtu.faceauth.MainActivity;
import com.xtu.faceauth.PersonDetail;
import com.xtu.faceauth.R;
import com.xtu.faceauth.SafeActivity;
import com.xtu.faceauth.bean.TYUser;
import com.xtu.faceauth.utils.BmobUtils;
import com.xtu.faceauth.utils.ImageUtils;
import com.xtu.faceauth.utils.SpUtils;

/**
 * Created by Administrator on 2016/5/15.
 */
public class OwnerFragment extends Fragment implements View.OnClickListener {

    private ImageView mHeadIV;   //显示用户头像
    private TextView mNick;     //显示用户昵称
    private TextView mUser;     //显示用户名
    private View mInfoLayout;   //用户信息点击项目
    private View mWorksLayout;  //作品集点击子选项
    private View mSafeLayout;   //...
    private View mIdeaLayout;
    private View mAboutLayout;
    private View mShareLayout;
    private View mExitLayout;

    //获取当前用户信息
    private TYUser iUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mParent = inflater.inflate(R.layout.fragment_owner, container, false);
        initData();
        initViews(mParent);
        return mParent;
    }

    //获取用户信息
    private void initData() {
        iUser = BmobUtils.getCurrentUser();
    }

    private void initViews(View mParent) {
        mHeadIV = (ImageView) mParent.findViewById(R.id.id_head);
        mNick = (TextView) mParent.findViewById(R.id.id_nick);
        mUser = (TextView) mParent.findViewById(R.id.id_user);

        //初始化头部条目的显示
        if (!TextUtils.isEmpty(iUser.getIconPath()))
            ImageUtils.Display(iUser.getIconPath(), mHeadIV);

        mInfoLayout = mParent.findViewById(R.id.id_info);
        mWorksLayout = mParent.findViewById(R.id.id_mworks);
        mSafeLayout = mParent.findViewById(R.id.id_safe);
        mIdeaLayout = mParent.findViewById(R.id.id_idea);
        mAboutLayout = mParent.findViewById(R.id.id_about);
        mShareLayout = mParent.findViewById(R.id.id_share);
        mExitLayout = mParent.findViewById(R.id.id_exit);

        //为每个条目设置点击时间
        mInfoLayout.setOnClickListener(this);
        mWorksLayout.setOnClickListener(this);
        mSafeLayout.setOnClickListener(this);
        mIdeaLayout.setOnClickListener(this);
        mAboutLayout.setOnClickListener(this);
        mShareLayout.setOnClickListener(this);
        mExitLayout.setOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        mNick.setText(iUser.getmNickName());
        mUser.setText(iUser.getUsername());
    }

    @Override
    public void onClick(View v) {
        Activity mActivity = getActivity();
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.id_info:
                intent.setClass(getActivity(), PersonDetail.class);
                mActivity.startActivity(intent);
                break;
            case R.id.id_mworks:

                break;
            case R.id.id_safe:
                intent.setClass(getActivity(), SafeActivity.class);
                mActivity.startActivity(intent);
                break;
            case R.id.id_idea:

                break;
            case R.id.id_about:

                break;
            case R.id.id_share:
                showShareDialog();
                break;
            case R.id.id_exit:
                SpUtils.setAutoLogin(false);
                intent.setClass(getActivity(), MainActivity.class);
                mActivity.startActivity(intent);
                mActivity.finish();
                break;
        }
    }

    private AlertDialog dialog = null;
    //弹出分享弹窗
    private void showShareDialog() {
        if(dialog == null){
            dialog = new AlertDialog.Builder(getActivity()).create();
            View contentView = View.inflate(getActivity(), R.layout.dialog_share, null);
            dialog.setView(contentView,0,0,0,0);
            dialog.setCancelable(true);
        }
        dialog.show();
    }
}
