package com.xtu.faceauth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xtu.faceauth.adapter.WorksAdapter;
import com.xtu.faceauth.base.BaseActivity;
import com.xtu.faceauth.bean.TYUser;
import com.xtu.faceauth.bean.Works;
import com.xtu.faceauth.utils.ImageUtils;
import com.xtu.faceauth.utils.ProgressbarUtils;
import com.xtu.faceauth.utils.ToastUtils;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class UserDetailActivity extends BaseActivity{

    private ImageView headImageView;  //用户的头像
    private TextView nickTextView;   //用户的昵称
    private TextView usernameTextView;  //用户用户名
    private TextView msgTextView;    //显示用户的签名
    private ListView workListView;   //显示用户的作品
    private TYUser mAuthor;
    private BmobQuery<Works> mQuery;
    private List<Works> mDatas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        mAuthor = (TYUser) getIntent().getSerializableExtra("author");
        mDatas = (List<Works>) getIntent().getSerializableExtra("work");
        initView();
    }

    private void initView() {
        headImageView = (ImageView) findViewById(R.id.id_head);
        nickTextView = (TextView) findViewById(R.id.id_nickname);
        usernameTextView = (TextView) findViewById(R.id.id_username);
        msgTextView = (TextView) findViewById(R.id.id_msg);
        workListView = (ListView) findViewById(R.id.id_listview);

        ImageUtils.Display(mAuthor.getIconPath(), headImageView);
        nickTextView.setText(mAuthor.getmNickName());
        usernameTextView.setText(mAuthor.getUsername());
        msgTextView.setText(mAuthor.getmMsg());
        bindDatas();
    }

    //绑定数据
    private void bindDatas() {
        WorksAdapter mAdapter = new WorksAdapter(this, mDatas);
        workListView.setAdapter(mAdapter);
        workListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String worksPath = mDatas.get(position).getWorksPath();
                Intent intent = new Intent(UserDetailActivity.this, PhotoActivity.class);
                intent.putExtra("url",worksPath);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatas.clear();
        mDatas = null;
    }
}
