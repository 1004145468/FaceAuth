package com.xtu.faceauth.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.xtu.faceauth.R;
import com.xtu.faceauth.adapter.CommunicationAdapter;
import com.xtu.faceauth.bean.Works;
import com.xtu.faceauth.utils.ProgressbarUtils;
import com.xtu.faceauth.utils.ToastUtils;
import com.xtu.faceauth.views.RefreshView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2016/5/15.
 */
public class FriendsFragment extends Fragment implements AdapterView.OnItemClickListener, RefreshView.OnRefreshListener {

    //显示控件
    private RefreshView mWorksListViews;
    //显示的适配器
    //private
    //显示数据
    private List<Works> mWorks ;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            mWorksListViews.completeRefresh();
            ToastUtils.show("刷新完成");
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View mParent = inflater.inflate(R.layout.fragment_friends, container, false);
        mWorksListViews = (RefreshView) mParent.findViewById(R.id.id_listview);
        mWorksListViews.setOnRefreshListener(this);
        mWorksListViews.setOnItemClickListener(this);
        //在创建的时间加载一次数据
        initDatas();
        return mParent;
    }

    // 加载数据
    private void initDatas() {
        ProgressbarUtils.showDialog(getActivity(),"小言正在为您加载社区资源...");
        BmobQuery<Works> mQuery = new BmobQuery<Works>();
        mQuery.order("-createdAt");
        mQuery.include("mAuthor");
        mQuery.setLimit(50);
        mQuery.findObjects(getActivity(), new FindListener<Works>() {
            @Override
            public void onError(int i, String s) {
                ProgressbarUtils.hideDialog();
                ToastUtils.show("网络不稳定，资源获取失败！");
            }

            @Override
            public void onSuccess(List<Works> list) {
                ProgressbarUtils.hideDialog();
                mWorks = list;
                bindCommDate();
            }
        });
    }

    //更新数据显示
    private void bindCommDate(){
        CommunicationAdapter mAdapter = new CommunicationAdapter(getActivity(),mWorks);
        mWorksListViews.setAdapter(mAdapter);

    }


    //设置点击条目监听
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void OnPullRefreshing() {
        new Thread(){
            @Override
            public void run() {
                SystemClock.sleep(5000);
                mHandler.sendEmptyMessage(0x11);
            }
        }.start();
    }
}
