package com.xtu.faceauth.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xtu.faceauth.PhotoActivity;
import com.xtu.faceauth.R;
import com.xtu.faceauth.UserDetailActivity;
import com.xtu.faceauth.bean.TYUser;
import com.xtu.faceauth.bean.Works;
import com.xtu.faceauth.utils.ImageUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/6/5.
 */
public class CommunicationAdapter extends BaseAdapter {


    private LayoutInflater mInflater;
    private List<Works> mDatas;
    private Context mContext;

    public CommunicationAdapter(Context mContext, List<Works> mDatas) {
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
        this.mDatas = mDatas;

    }

    //条目的个数
    @Override
    public int getCount() {
        return mDatas.size();
    }

    //每一个条目对象
    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_communication, parent, false);
            holder.headImageView = (ImageView) convertView.findViewById(R.id.id_head);
            holder.userNameView = (TextView) convertView.findViewById(R.id.id_username);
            holder.contentView = (TextView) convertView.findViewById(R.id.id_content);
            holder.shareImageview = (ImageView) convertView.findViewById(R.id.id_pic);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Works mWork = mDatas.get(position);
        //显示分享图片
        //设置用户头像信息
        String iconPath = mWork.getmAuthor().getIconPath();
        ImageUtils.Display(iconPath, holder.headImageView);
        holder.headImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //触发用户详情表页面的跳转
                startUserDetail(mWork.getmAuthor());

            }
        });

        //设置用户昵称
        String mNickName = mWork.getmAuthor().getmNickName();
        holder.userNameView.setText(mNickName);

        //设置分享的文本内容
        holder.contentView.setText(mWork.getContent());

        //设置分享的图片
        ImageUtils.Display(mWork.getWorksPath(), holder.shareImageview);
        holder.shareImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PhotoActivity.class);
                intent.putExtra("url",mWork.getWorksPath());
                mContext.startActivity(intent);
            }
        });

        //设置分享内容
        return convertView;
    }

    public void startUserDetail(TYUser author){
        Intent intent = new Intent(mContext, UserDetailActivity.class);
        intent.putExtra("author",author);
        mContext.startActivity(intent);
    }

    class ViewHolder {
        ImageView headImageView;
        TextView userNameView;
        TextView contentView;
        ImageView shareImageview;

    }


}

