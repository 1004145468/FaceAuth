package com.xtu.faceauth;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.xtu.faceauth.adapter.WorksAdapter;
import com.xtu.faceauth.base.BaseActivity;
import com.xtu.faceauth.bean.TYUser;
import com.xtu.faceauth.bean.Works;
import com.xtu.faceauth.utils.BmobUtils;
import com.xtu.faceauth.utils.ProgressbarUtils;
import com.xtu.faceauth.utils.ToastUtils;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;

public class WorksActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private ListView mListView;
    private List<Works> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_works);
        initDatas();
        initViews();
    }

    private void initViews() {
        TextView titleView = (TextView) findViewById(R.id.id_activity_title);
        titleView.setText("个人作品");
        mListView = (ListView) findViewById(R.id.id_listview);
        mListView.setOnItemClickListener(this);
    }

    private void initDatas() {
        ProgressbarUtils.showDialog(this,"小言正在更新个人作品！");
        TYUser currentUser = BmobUtils.getCurrentUser();
        BmobQuery<Works> mQuery = new BmobQuery<>();
        mQuery.addWhereEqualTo("mAuthor", currentUser);
        mQuery.order("-createdAt");
        mQuery.findObjects(this, new FindListener<Works>() {
            @Override
            public void onError(int i, String s) {
                ProgressbarUtils.hideDialog();
                ToastUtils.show("作品更新失败！");
            }

            @Override
            public void onSuccess(List<Works> list) {
                mDatas = list;
                ProgressbarUtils.hideDialog();
                WorksAdapter adapter = new WorksAdapter(WorksActivity.this,list);
                mListView.setAdapter(adapter);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("敏感操作")
                .setMessage("该作品一旦被删除无法找回！")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateWorks(position);
                    }
                })
                .setNegativeButton("取消",null)
                .show();
    }

    private void updateWorks(final int position){
        Works works = new Works();
        String objectId = mDatas.get(position).getObjectId();
        works.setObjectId(objectId);
        ProgressbarUtils.showDialog(this,"作品删除中...");

        works.delete(this,works.getObjectId(),new DeleteListener() {
            @Override
            public void onFailure(int i, String s) {
                ProgressbarUtils.hideDialog();
                ToastUtils.show("网络不给力，删除失败！");
            }

            @Override
            public void onSuccess() {
                ProgressbarUtils.hideDialog();
                mDatas.remove(position);
                WorksAdapter mAdapter = new WorksAdapter(WorksActivity.this, mDatas);
                mListView.setAdapter(mAdapter);
            }
        });
    }
}
