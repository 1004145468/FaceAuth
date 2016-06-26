package com.xtu.faceauth.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xtu.faceauth.R;
import com.xtu.faceauth.bean.Works;
import com.xtu.faceauth.utils.ImageUtils;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Administrator on 2016/6/6.
 */
public class WorksAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Works> mDatas;

    private int[] mStat = null;

    //当前的月份
    private int mCurrentMonth;
    //当前的日期
    private int mCurrentDay;

    //是否已经绘制过今天
    private boolean showToday;

    //是否展示过昨天
    private boolean showYesterday;

    public WorksAdapter(Context mContext, List<Works> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        mInflater = LayoutInflater.from(mContext);
        mStat = new int[mDatas.size()];
        Calendar mCalendar = Calendar.getInstance();
        mCurrentMonth = mCalendar.get(Calendar.MONTH) + 1;
        mCurrentDay = mCalendar.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

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
            convertView = mInflater.inflate(R.layout.item_works, parent, false);
            holder.showTextView = (TextView) convertView.findViewById(R.id.id_show);
            holder.dayTextView = (TextView) convertView.findViewById(R.id.id_day);
            holder.monthTextView = (TextView) convertView.findViewById(R.id.id_month);
            holder.imageView = (ImageView) convertView.findViewById(R.id.id_image);
            holder.contentTextView = (TextView) convertView.findViewById(R.id.id_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //初始化条目的可见性
        holder.showTextView.setVisibility(View.INVISIBLE);
        holder.monthTextView.setVisibility(View.INVISIBLE);
        holder.dayTextView.setVisibility(View.INVISIBLE);

        int state = mStat[position];
        //对每一个条目进行数据初始化
        Works currentWorks = mDatas.get(position);
        try {
            //2016-06-06 15:30:55
            String createdAt = currentWorks.getCreatedAt();
            Integer month = Integer.valueOf(createdAt.substring(5, 7));
            Integer day = Integer.valueOf(createdAt.substring(8, 10));

            //条目处于为绑定状态
            if (state == 0) {
                //展示日期标签
                if (month == mCurrentMonth && day == mCurrentDay) {
                    //绑定为今天
                    state = showToday ? 4 : 1;
                    showToday = true;
                } else if (month == mCurrentMonth && day == mCurrentDay - 1) {
                    //绑定为昨天
                    state = showYesterday ? 4 : 2;
                    showYesterday = true;
                } else {
                    //绑定为昨天
                    state = 3;
                }
                mStat[position] = state;
            }

            if (state == 1) {
                //展示今天
                holder.showTextView.setText("今天");
                holder.showTextView.setVisibility(View.VISIBLE);
            } else if (state == 2) {
                //展示昨天
                holder.showTextView.setText("昨天");
                holder.showTextView.setVisibility(View.VISIBLE);
            } else if (state == 3) {
                //展示普通日期
                String tmpDay = String.format("%02d", day);
                holder.dayTextView.setText(tmpDay);
                holder.dayTextView.setVisibility(View.VISIBLE);
                holder.monthTextView.setText(month + "月");
                holder.monthTextView.setVisibility(View.VISIBLE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
       ImageUtils.Display(currentWorks.getWorksPath(), holder.imageView);
        holder.contentTextView.setText(currentWorks.getContent());
        return convertView;
    }

    class ViewHolder {
        TextView showTextView;  //显示： 今天 明天
        TextView dayTextView;  //显示日期
        TextView monthTextView; // 显示月份
        ImageView imageView;  //显示用户头像
        TextView contentTextView;  //显示分享的内容
    }

}
