package com.xtu.faceauth.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.xtu.faceauth.R;

/**
 * Created by Administrator on 2016/6/3.
 */
public class SkinAdapter extends BaseAdapter {

    private Context context;

    public SkinAdapter(Context context){
        this.context = context;
    }

    public static final int[] SKINS = {
            R.mipmap.eyel0, R.mipmap.eyel1, R.mipmap.eyel2,
            R.mipmap.eyel3, R.mipmap.eyel4,R.mipmap.eyel5,
            R.mipmap.eyel6
    };

    public static final int[] SKINSR = {
            R.mipmap.eyer0, R.mipmap.eyer1, R.mipmap.eyer2,
            R.mipmap.eyer3, R.mipmap.eyer4,R.mipmap.eyer5,
            R.mipmap.eyer6
    };

    @Override
    public int getCount() {
        return SKINS.length;
    }

    @Override
    public Object getItem(int position) {
        return SKINS[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView skinView = new ImageView(context);
        skinView.setImageResource(SKINS[position]);
        return skinView;
    }
}
